package com.blogspot.soyamr.notforgotagain.view.recycler_view_components


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.domain.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.db.tables.Category
import com.blogspot.soyamr.notforgotagain.model.db.tables.FullNoteData
import kotlinx.android.synthetic.main.item_note_details.view.*
import kotlinx.android.synthetic.main.item_note_header.view.*


private const val DETAILS_TYPE = 1
private const val HEADER_TYPE = 2

class NoteAdapter(
    val notes: ArrayList<NoteBoss>,
    private val listener: (Int) -> Unit,
    private val listenerCheckBox: (Int, Boolean) -> Unit
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolderHeader(private val viewItem: View) :
        RecyclerView.ViewHolder(viewItem) {

        fun setHeader(category: Category) {
            viewItem.headerTextView.text = category.name
        }
    }

    class ViewHolderDetails(
        private val viewItem: View, private val listener: (Int) -> Unit,
        private val listenerCheckBox: (Int, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(viewItem) {
        fun setNoteDetails(fullNoteData: FullNoteData) {

            viewItem.titleTextView.text = fullNoteData.nTitle
            viewItem.subTitleTextView.text = fullNoteData.nDescription

            viewItem.cardView.setCardBackgroundColor(Color.parseColor(fullNoteData.pColor))
            viewItem.doneCheckBox.isChecked = fullNoteData.nDone

        }
        init {
            viewItem.setOnClickListener { listener(adapterPosition) }
            viewItem.doneCheckBox.setOnClickListener {
                listenerCheckBox(
                    adapterPosition,
                    (it as CheckBox).isChecked
                )
            }
        }

    }

    override fun getItemViewType(position: Int): Int {
        return if (notes[position].category == null) {
            DETAILS_TYPE;
        } else {
            HEADER_TYPE;
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view1: View
        return if (viewType == DETAILS_TYPE) { // for call layout
            view1 = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note_details, parent, false)
            ViewHolderDetails(view1, listener, listenerCheckBox)
        } else { // for email layout
            view1 = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_note_header, parent, false)
            ViewHolderHeader(view1)
        }

    }


    override fun getItemCount(): Int {
        return notes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == HEADER_TYPE) {
            (holder as ViewHolderHeader).setHeader(notes[position].category!!)
        } else {
            (holder as ViewHolderDetails).setNoteDetails(notes[position].note!!)
        }
    }

    fun updateData(myNotes: List<NoteBoss>?) {
        notes.clear()
        try {
            notes.addAll(myNotes!!)
        } catch (e: Exception) {
//            println("AAmr -> problem from note adapter $e")
        }
        notifyDataSetChanged()
    }

}
