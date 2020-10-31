package com.blogspot.soyamr.notforgotagain.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.model.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.view.recycler_view_components.NoteAdapter
import kotlinx.android.synthetic.main.fragment_notes_board.*


class NotesBoardFragment : Fragment() {
    var notes = ArrayList<NoteBoss>()
    var currentUserId: Long? = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments?.let { NotesBoardFragmentArgs.fromBundle(it) }
        currentUserId = args?.uId
        Log.i("please: ", " )))) " + currentUserId)
        setUpToolBar(view)
        setClickListener()
        setupRecyclerView2();
    }

    private fun setUpToolBar(view: View) {
        val toolBar = view.findViewById<Toolbar>(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.title = getString(R.string.app_name)
        toolBar.post {
            toolBar.inflateMenu(R.menu.sign_out)
        }
        val repository = NoteRepository(requireContext())
        toolBar.setOnMenuItemClickListener {
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToSignInFragment()
            )
            repository.signMeOut(currentUserId!!)
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
        val repository = NoteRepository(requireContext())

        val cats = repository.getCategories()
        val nots = repository.getnotes(currentUserId)

        notes.clear()
        cats.forEach { ob ->
            notes.add(NoteBoss(null, ob))
            nots.forEach {
                if (ob.cid == it.cid)
                    notes.add(NoteBoss(it, null))
            }
        }
        myAdapter.notifyDataSetChanged()

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_notes_board, container, false);
    }

}