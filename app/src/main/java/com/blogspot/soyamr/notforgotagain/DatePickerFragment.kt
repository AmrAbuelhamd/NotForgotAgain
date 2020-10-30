package com.blogspot.soyamr.notforgotagain


import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.app.Dialog
import android.os.Bundle
import android.widget.DatePicker
import androidx.annotation.Nullable
import androidx.core.os.bundleOf
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.setFragmentResult
import java.text.SimpleDateFormat
import java.util.*


class DatePickerFragment : DialogFragment(), DatePickerDialog.OnDateSetListener {
    override fun onCreateDialog(@Nullable savedInstanceState: Bundle?): Dialog {
        val c = Calendar.getInstance()
        val year = c[Calendar.YEAR]
        val month = c[Calendar.MONTH]
        val day = c[Calendar.DAY_OF_MONTH]
        return DatePickerDialog(requireActivity(), this, year, month, day)
    }

    @SuppressLint("NewApi")
    override fun onDateSet(p0: DatePicker?, p1: Int, p2: Int, p3: Int) {
        val date = Calendar.getInstance();
        date.set(Calendar.YEAR, p1);
        date.set(Calendar.MONTH, p2);
        date.set(Calendar.DAY_OF_MONTH, p3);

        val format = SimpleDateFormat("yyyy-MM-dd")

        setFragmentResult(
            AddNoteFragment.REQUEST_DATE_KEY,
            bundleOf("userChosenDateKey" to format.format(date.time))
        )
    }
}