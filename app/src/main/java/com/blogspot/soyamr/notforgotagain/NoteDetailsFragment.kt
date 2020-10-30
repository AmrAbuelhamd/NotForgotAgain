package com.blogspot.soyamr.notforgotagain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import kotlinx.android.synthetic.main.fragment_note_details.*


class NoteDetailsFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setClicks()
        setUpToolBar(view)
    }

    private fun setUpToolBar(view: View) {
        val toolBar = view.findViewById<Toolbar>(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)

        toolBar.setupWithNavController(findNavController())

        toolBar.title = getString(R.string.note)
    }

    private fun setClicks() {
        editNoteImageView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                NoteDetailsFragmentDirections.actionNoteDetailsFragmentToAddNoteFragment()//todo send id
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_note_details, container, false)
    }


}