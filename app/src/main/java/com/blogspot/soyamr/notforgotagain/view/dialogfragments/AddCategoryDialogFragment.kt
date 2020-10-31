package com.blogspot.soyamr.notforgotagain.view.dialogfragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.view.addnote.AddNoteFragment
import kotlinx.android.synthetic.main.dialogfragment_add_category.*

class AddCategoryDialogFragment : DialogFragment() {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        saveCtegoryButtonView.setOnClickListener {
            if (categoryTextInputLayout.editText?.text.toString().isNotEmpty()) {
                setFragmentResult(
                    AddNoteFragment.REQUEST_CATEGORY_KEY,
                    bundleOf("categoryInputKey" to categoryTextInputLayout.editText?.text.toString())
                )
            }
            findNavController().popBackStack()
        }

        cancelButtonView.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.dialogfragment_add_category, container, false)
    }
}