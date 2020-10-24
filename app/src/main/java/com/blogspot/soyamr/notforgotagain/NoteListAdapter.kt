package com.blogspot.soyamr.notforgotagain


import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_note_details.view.*
import kotlinx.android.synthetic.main.item_note_header.view.*


private const val DETAILS_TYPE = 1
private const val HEADER_TYPE = 2
class FavAdapter(val recipes: List<Recipe>, private val listener: (Int) -> Unit) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    class ViewHolderHeader(val viewItem: View, private val listener: (Int) -> Unit) :
        RecyclerView.ViewHolder(viewItem) {
        val headerTextView = viewItem.headerTextView

        fun setCallDetails(recipe: Recipe) {
            headerTextView.text = recipe.str1
        }

        init {
            viewItem.setOnClickListener { listener(adapterPosition) }
        }
    }
    class ViewHolderDetails(val viewItem: View, private val listener: (Int) -> Unit) :
        RecyclerView.ViewHolder(viewItem) {
        fun setNoteDetails(recipe: Recipe) {
           titleTextView.text = recipe.name
            subtitleTextView.text = recipe.str1

        }

        val titleTextView = viewItem.titleTextView
        val subtitleTextView = viewItem.subTitleTextView
//        val textView3 = viewItem.favTV3

        init {
            viewItem.setOnClickListener { listener(adapterPosition) }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (recipes[position].str1=="4 порции") {
            DETAILS_TYPE;
        } else {
            HEADER_TYPE;
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view1: View
        return if (viewType == DETAILS_TYPE) { // for call layout
            view1 = LayoutInflater.from(parent.context).inflate(R.layout.item_note_details, parent, false)
            ViewHolderDetails(view1,listener)
        } else { // for email layout
            view1 = LayoutInflater.from(parent.context).inflate(R.layout.item_note_header, parent, false)
            ViewHolderHeader(view1,listener)
        }

    }


    override fun getItemCount(): Int {
        return recipes.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (getItemViewType(position) == HEADER_TYPE) {
            (holder as ViewHolderHeader).setCallDetails(recipes[position])
        } else {
            (holder as ViewHolderDetails).setNoteDetails(recipes[position])
        }
    }

}