package com.blogspot.soyamr.notforgotagain.view.recycler_view_components


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.model.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.tables.Category
import com.blogspot.soyamr.notforgotagain.model.tables.Note
import kotlinx.android.synthetic.main.item_note_details.view.*
import kotlinx.android.synthetic.main.item_note_header.view.*


private const val DETAILS_TYPE = 1
private const val HEADER_TYPE = 2

class NoteAdapter(val notes: ArrayList<NoteBoss>, private val listener: (Long) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolderHeader(val viewItem: View) :
        RecyclerView.ViewHolder(viewItem) {
        val headerTextView = viewItem.headerTextView

        fun setHeader(category: Category) {
            headerTextView.text = category.name
            //viewItem.setBackgroundColor(viewItem.resources.getColor(R.color.design_default_color_secondary_variant))
        }
    }

    class ViewHolderDetails(val viewItem: View, private val listener: (Long) -> Unit) :
        RecyclerView.ViewHolder(viewItem) {
        fun setNoteDetails(note: Note) {
            titleTextView.text = note.title
            subtitleTextView.text = note.description

            val cardView = viewItem.cardView as CardView
            when (note.pid){
                1L-> cardView.setCardBackgroundColor(viewItem.resources.getColor(R.color.green))
                2L-> cardView.setCardBackgroundColor(viewItem.resources.getColor(R.color.red))
                3L-> cardView.setCardBackgroundColor(viewItem.resources.getColor(R.color.textBlue))
            }

            viewItem.setOnClickListener { listener(note.nid) }
        }

        val titleTextView = viewItem.titleTextView
        val subtitleTextView = viewItem.subTitleTextView

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
            ViewHolderDetails(view1, listener)
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

}
