package com.blogspot.soyamr.notforgotagain.view.addnote

import android.content.Context
import android.os.Bundle
import android.util.Log
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
import com.blogspot.soyamr.notforgotagain.domain.GeneralData
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.model.db.tables.Note
import kotlinx.android.synthetic.main.fragment_add_note.*


class AddNoteFragment : Fragment(), AddNoteView {
    private lateinit var presenter: AddNotePresenter
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
        viewModel.categories.observe(viewLifecycleOwner, Observer { cats ->
            categorySpinnerAdapter.addItems(cats, categorySpinner)
        })
        viewModel.priorities.observe(viewLifecycleOwner, Observer { prts ->
            prioritySpinnerAdapter.addItems(prts, prioritySpinner)
        })
    }


    private fun setListeners() {
        //todo move to fragment.xml
        saveDataButtonView.setOnClickListener {
            presenter.saveNote(
                currentNoteId,
                categorySpinner.selectedItemId,
                prioritySpinner.selectedItemId,
                dateText.editText?.text.toString(),
                headerTextLayout.editText?.text.toString(),
                descriptionTextLayout.editText?.text.toString()
            )
            Log.i("hie: ", " )))) " + categorySpinner.selectedItem)
            Log.i("hie: ", " )))) " + prioritySpinner.selectedItem)
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
            presenter.addNewCategory(newCategory!!)
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

    override fun getRequiredContext(): Context {
        return requireContext()
    }

    override fun moveToNoteBoard(userId: Long) {
        findNavController().navigate(
            AddNoteFragmentDirections.actionAddNoteFragmentToNotesBoardFragment(
//                userId
            )
        )
    }

    override fun setSignInError() {


    }

    override fun showProgressBar() {
    }

    override fun hidProgressBar() {
    }

    override fun populateCategorySpinnerData(categories: List<GeneralData>) {
        categorySpinnerAdapter.addItems(categories, categorySpinner)
    }

    override fun populatePrioritySpinnerData(priorities: List<GeneralData>) {
        prioritySpinnerAdapter.addItems(priorities, prioritySpinner)
    }

    override fun addNewNote(note: Note) {

    }

    override fun showError() {
        headerTextLayout.error = "something went wrong"
    }


}