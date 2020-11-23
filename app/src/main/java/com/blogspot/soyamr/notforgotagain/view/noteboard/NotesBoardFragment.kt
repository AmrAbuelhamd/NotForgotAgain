package com.blogspot.soyamr.notforgotagain.view.noteboard

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.databinding.FragmentNotesBoardBinding
import com.blogspot.soyamr.notforgotagain.domain.NoteBoss2
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.view.recycler_view_components.NoteAdapter
import kotlinx.android.synthetic.main.fragment_notes_board.*


class NotesBoardFragment : Fragment() {
    var notes = ArrayList<NoteBoss2>()
    var currentUserId: Long? = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments?.let { NotesBoardFragmentArgs.fromBundle(it) }
        currentUserId = args?.uId
        setUpToolBar(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setClickListener()
        setupRecyclerView2();


        val repository = NoteRepository(requireContext())
        notes.clear()
        val cats = repository.getCategoriesNet()
        Log.e("board ", " " + cats.toString())

        val nots = repository.getNotesNet()

        cats?.forEach { ca ->
            Log.e("notboard: ", " category-: " + ca.toString())
            notes.add(NoteBoss2(null, ca))
            nots?.forEach {
                Log.e("notboard2: ", " task-: " + it.toString())
                if (it.category?.id == ca.id)
                    notes.add(NoteBoss2(it, null))
            }
        }
        myAdapter.notifyDataSetChanged()

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
        val repository = NoteRepository(requireContext())
        toolBar.setOnMenuItemClickListener {
            viewModel.logOutUser()
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToSignInFragment()
            )
//            repository.signMeOut(currentUserId!!)
            true
        }

    }

    private fun setClickListener() {
        addNoteFloatingActionButton.setOnClickListener {
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToAddNoteFragment(currentUserId!!)
            )

        }
    }


    lateinit var myAdapter: NoteAdapter
    private fun setupRecyclerView2() {
        myAdapter = NoteAdapter(notes) { noteId ->
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToNoteDetailsFragment(
                    noteId
                )
            )
        }

        val myViewManager = LinearLayoutManager(context)
        val recyclerView = notesRecycler.apply {
            setHasFixedSize(true)
            layoutManager = myViewManager
            adapter = myAdapter
        }


    }

    override fun onResume() {
        super.onResume()
//        val repository = NoteRepository(requireContext())
//
//        notes.clear()
//        val cats = repository.getCategories()
//        cats.forEach {
//            val notesOfCategory = repository.getFullNoteDataRelatedToCategory(it.id);
//            notes.add(NoteBoss(null, it))
//            notesOfCategory.forEach {
//                notes.add(NoteBoss(it, null))
//            }
//        }

//        val repository = NoteRepository(requireContext())
//        notes.clear()
//        val cats = repository.getCategoriesNet()
//        val nots = repository.getNotesNet()
//        cats?.forEach{ca->
//            notes.add(NoteBoss2(null, ca))
//            nots?.forEach {
//                if(it.category?.id==ca.id)
//                    notes.add(NoteBoss2(it, null))
//            }
//        }
//
//
//
//        myAdapter.notifyDataSetChanged()
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