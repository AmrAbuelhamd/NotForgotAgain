package com.blogspot.soyamr.notforgotagain.view.addnote

import android.content.Context
import android.graphics.Color
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import com.blogspot.soyamr.notforgotagain.domain.GeneralData

//lidia spinner adapter that accepts interface which can accept priority and category class type
// it's used to populate both spinners and to add the first element as hint
class SpinnerAdapter(
    context: Context,
    textViewResourceId: Int,
    private val dataValues: List<GeneralData>
) :
    ArrayAdapter<GeneralData>(context, textViewResourceId, dataValues) {

    override fun isEnabled(position: Int): Boolean {
        // Disable item with id equal -1
        return dataValues[position].getID() != -1L
    }

    override fun getItemId(position: Int): Long {
        return dataValues[position]?.getID()
    }


    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getDropDownView(position, convertView, parent) as TextView
        if (dataValues[position].getID() == -1L)
            label.setTextColor(Color.GRAY)
        else
            label.setTextColor(Color.BLACK)
        label.text = dataValues[position].getItemName()
        return label;
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val label = super.getView(position, convertView, parent) as TextView
        if (dataValues[position].getID() == -1L)
            label.setTextColor(Color.GRAY)
        else
            label.setTextColor(Color.BLACK)
        label.text = dataValues[position].getItemName()
        return label;
    }


    fun addItems(items: List<GeneralData>, spinner: Spinner) {
        addAll(items)
        notifyDataSetChanged()
        spinner.setSelection(getPosition(items[0]))
    }

    override fun getPosition(item: GeneralData?): Int {
        dataValues.forEachIndexed() { index, element ->
            if (element.getID() == item?.getID())
                return index
        }
        return super.getPosition(item)
    }
}