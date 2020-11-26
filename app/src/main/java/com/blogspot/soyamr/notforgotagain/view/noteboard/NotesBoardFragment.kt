package com.blogspot.soyamr.notforgotagain.view.noteboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.databinding.FragmentNotesBoardBinding
import com.blogspot.soyamr.notforgotagain.domain.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.view.recycler_view_components.NoteAdapter
import kotlinx.android.synthetic.main.fragment_notes_board.*


class NotesBoardFragment : Fragment() {
    var notes = ArrayList<NoteBoss>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolBar(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setClickListener()
        setupRecyclerView();

        viewModel.notes.observe(viewLifecycleOwner, Observer { myNotes ->
            myAdapter.updateData(myNotes)
        })
    }

    private val repository: NoteRepository by lazy { NoteRepository(requireContext()) }
    private val viewModel: NoteBoardViewModel by viewModels { NoteBoardViewModelFactory(repository) }

    private fun setUpToolBar(view: View) {
        val toolBar = view.findViewById<Toolbar>(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.title = getString(R.string.app_name)
        toolBar.post {
            toolBar.inflateMenu(R.menu.sign_out)
        }

        toolBar.setOnMenuItemClickListener {
            viewModel.logOutUser()
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToSignInFragment()
            )
            true
        }

    }

    private fun setClickListener() {
        addNoteFloatingActionButton.setOnClickListener {
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToAddNoteFragment()
            )
        }
    }


    lateinit var myAdapter: NoteAdapter
    private fun setupRecyclerView() {
        myAdapter = NoteAdapter(notes, { position ->
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToNoteDetailsFragment(notes[position].note?.nId!!)
            )
        }, { position, checked ->
            viewModel.onCheckBoxClicked(notes[position].note?.nId!!, checked)
        })

        val myViewManager = LinearLayoutManager(context)
        val recyclerView = notesRecycler.apply {
            setHasFixedSize(true)
            layoutManager = myViewManager
            adapter = myAdapter
        }


    }


    lateinit var binding: FragmentNotesBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes_board, container, false)
        return binding.root

    }

}