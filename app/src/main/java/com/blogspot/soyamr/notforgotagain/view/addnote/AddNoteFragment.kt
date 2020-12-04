package com.blogspot.soyamr.notforgotagain.view.addnote

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.databinding.FragmentAddNoteBinding
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_add_note.*


class AddNoteFragment : Fragment() {
    lateinit var categorySpinnerAdapter: SpinnerAdapter
    lateinit var prioritySpinnerAdapter: SpinnerAdapter

    companion object {
        const val REQUEST_CATEGORY_KEY = "getDate"
        const val REQUEST_DATE_KEY = "getCategory"
    }

    private val repository: NoteRepository by lazy { NoteRepository(requireContext()) }
    private val viewModel: AddNoteViewModel by viewModels {
        AddNoteViewModelFactory(
            repository,
            currentNoteId!!
        )
    }


    private fun showError(errorMessage: String) {
        if (errorMessage.isNullOrEmpty())
            return
        val alertDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("Error")
        alertDialog.setMessage(errorMessage)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }


    var currentNoteId: Long? = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        presenter = AddNotePresenter(this)
        val args = arguments?.let { AddNoteFragmentArgs.fromBundle(it) }
        currentNoteId = args?.noteId

        binding.lifecycleOwner = this
        binding.viewModel = viewModel


        setUpToolBar(view)
        setUpSpinner(view)
        setListeners()
        setUpObservers()
    }

    private fun setUpObservers() {
        toolBar.title = viewModel.toolbarTitle
        viewModel.categories.observe(viewLifecycleOwner, { cats ->
            categorySpinnerAdapter.addItems(cats, categorySpinner)
        })
        viewModel.priorities.observe(viewLifecycleOwner, { prts ->
            prioritySpinnerAdapter.addItems(prts, prioritySpinner)
        })

        viewModel.selectedPriority.observe(viewLifecycleOwner, { item ->
            prioritySpinner.setSelection(prioritySpinnerAdapter.getPosition(item), false)
        })
        viewModel.selectedCategory.observe(viewLifecycleOwner, { item ->
            categorySpinner.setSelection(categorySpinnerAdapter.getPosition(item), false)
        })
        viewModel.success.observe(viewLifecycleOwner, { item ->
            if (item)
                findNavController().navigate(R.id.action_addNoteFragment_to_notesBoardFragment)
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer {
            showError(it)
        })

        viewModel.snackBarMessage.observe(viewLifecycleOwner, Observer {
            showErrorSnackBar(it)
        })

    }

    private fun showErrorSnackBar(errorMessage: String) {
        if (errorMessage.isNullOrEmpty())
            return
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    private fun setListeners() {
        //todo move to fragment.xml
        saveDataButtonView.setOnClickListener {
            viewModel.addNewNote(categorySpinner.selectedItemId, prioritySpinner.selectedItemId)
        }
        // Use the Kotlin extension in the fragment-ktx artifact
        setFragmentResultListener(REQUEST_DATE_KEY) { key, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val date = bundle.getString("userChosenDateKey")
            dateEditText.setText(date)
        }

        showCalendarButtonView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                AddNoteFragmentDirections.actionAddNoteFragmentToDatePickerFragment()
            )
        )

        addCategoryButtonView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                AddNoteFragmentDirections.actionAddNoteFragmentToAddCategoryDialogFragment()
            )
        )

        setFragmentResultListener(REQUEST_CATEGORY_KEY) { key, bundle ->
            // We use a String here, but any type that can be put in a Bundle is supported
            val newCategory = bundle.getString("categoryInputKey")
            viewModel.addNewCategory(newCategory!!)
        }

    }

    private fun setUpSpinner(view: View) {
        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
        val prioritySpinner: Spinner = view.findViewById(R.id.prioritySpinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        categorySpinnerAdapter = SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArrayList()
        )
        prioritySpinnerAdapter = SpinnerAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArrayList()
        )
        categorySpinner.adapter = categorySpinnerAdapter
        prioritySpinner.adapter = prioritySpinnerAdapter
    }

    lateinit var toolBar: Toolbar
    private fun setUpToolBar(view: View) {
        toolBar = view.findViewById(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)

        toolBar.setupWithNavController(findNavController())


    }

    lateinit var binding: FragmentAddNoteBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_note, container, false)
        return binding.root

    }



}