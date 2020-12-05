package com.blogspot.soyamr.notforgotagain.view.note_details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.databinding.FragmentNoteDetailsBinding
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import kotlinx.android.synthetic.main.fragment_note_details.*


class NoteDetailsFragment : Fragment() {


    private val repository: NoteRepository by lazy { NoteRepository(requireContext()) }
    private val viewModel: NoteDetailsViewModel by viewModels {
        NoteDetailsViewModelFactory(
            repository,
            currentNote!!
        )
    }


    var currentNote: Long? = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments?.let { NoteDetailsFragmentArgs.fromBundle(it) }
        currentNote = args?.nid

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


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
                NoteDetailsFragmentDirections.actionNoteDetailsFragmentToAddNoteFragment(currentNote!!)
            )
        )
    }

    lateinit var binding: FragmentNoteDetailsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_note_details, container, false)
        return binding.root

    }


}