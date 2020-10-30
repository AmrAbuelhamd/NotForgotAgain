package com.blogspot.soyamr.notforgotagain.view

import android.os.Bundle
import android.util.Log
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
import kotlinx.android.synthetic.main.fragment_add_note.*


class AddNoteFragment : Fragment() {

    companion object {
        const val REQUEST_CATEGORY_KEY = "getDate"
        const val REQUEST_DATE_KEY = "getCategory"
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolBar(view)
        setUpSpinner(view);
        setListeners()
    }

    private fun setListeners() {
        saveDataButtonView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                AddNoteFragmentDirections.actionAddNoteFragmentToNotesBoardFragment(-5)
            )
        )
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
            val category = bundle.getString("categoryInputKey")
            //todo send to databas
            Log.i("ah", " " + category);
        }

    }


    private fun setUpSpinner(view: View) {
        val spinner: Spinner = view.findViewById(R.id.spinner3)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.planets_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner.adapter = adapter
        }
        val spinner2: Spinner = view.findViewById(R.id.spinner4)
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.planets_array2,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner
            spinner2.adapter = adapter
        }
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


}