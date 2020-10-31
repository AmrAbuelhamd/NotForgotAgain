package com.blogspot.soyamr.notforgotagain.view.addnote

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResultListener
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.setupWithNavController
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.model.tables.Note
import kotlinx.android.synthetic.main.fragment_add_note.*


class AddNoteFragment : Fragment(), AddNoteView {
    private lateinit var presenter: AddNotePresenter
    lateinit var categorySpinnerAdapter: ArrayAdapter<String>
    lateinit var prioritySpinnerAdapter: ArrayAdapter<String>

    companion object {
        const val REQUEST_CATEGORY_KEY = "getDate"
        const val REQUEST_DATE_KEY = "getCategory"
    }

    var currentUserId: Long? = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = AddNotePresenter(this)
        val args = arguments?.let { AddNoteFragmentArgs.fromBundle(it) }
        currentUserId = args?.uid

        setUpToolBar(view)
        setUpSpinner(view)
        setListeners()
    }

    private fun setListeners() {
        saveDataButtonView.setOnClickListener {
            presenter.saveNote(
                currentUserId,
                categorySpinner.selectedItem.toString(),
                prioritySpinner.selectedItem.toString(),
                dateText.editText?.text.toString(),
                headerTextLayout.editText?.text.toString(),
                descriptionTextLayout.editText?.text.toString()
            )
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
            presenter.addNewCategory(currentUserId, newCategory)
        }

    }

    private fun setUpSpinner(view: View) {
        val categorySpinner: Spinner = view.findViewById(R.id.categorySpinner)
        val prioritySpinner: Spinner = view.findViewById(R.id.prioritySpinner)
        // Create an ArrayAdapter using the string array and a default spinner layout
        categorySpinnerAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArrayList<String>()
        )
        prioritySpinnerAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            ArrayList<String>()
        )
        categorySpinner.adapter = categorySpinnerAdapter
        prioritySpinner.adapter = prioritySpinnerAdapter
        presenter.getSpinnersData()
    }

    private fun setUpToolBar(view: View) {
        val toolBar = view.findViewById<Toolbar>(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)

        toolBar.setupWithNavController(findNavController())

        toolBar.title = getString(R.string.add_note)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_note, container, false);
    }

    override fun getRequiredContext(): Context {
        return requireContext()
    }

    override fun moveToNoteBoard(userId: Long) {
        findNavController().navigate(
            AddNoteFragmentDirections.actionAddNoteFragmentToNotesBoardFragment(
                userId
            )
        )
    }

    override fun setSignInError() {


    }

    override fun showProgressBar() {
    }

    override fun hidProgressBar() {
    }

    override fun populateCategorySpinnerData(categories: ArrayList<String>) {
        categorySpinnerAdapter.addAll(categories)
        categorySpinnerAdapter.notifyDataSetChanged()
    }

    override fun populatePrioritySpinnerData(priorities: ArrayList<String>) {
        prioritySpinnerAdapter.addAll(priorities)
        prioritySpinnerAdapter.notifyDataSetChanged()
    }

    override fun addNewNote(note: Note) {

    }

    override fun showError() {
        headerTextLayout.error = "something went wrong"
    }


}