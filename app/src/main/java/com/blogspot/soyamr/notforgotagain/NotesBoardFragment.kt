package com.blogspot.soyamr.notforgotagain

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_notes_board.*


class NotesBoardFragment : Fragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolBar(view)
        setClickListener()
        setupRecyclerView();
    }

    private fun setUpToolBar(view: View) {
        val toolBar = view.findViewById<Toolbar>(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.title = getString(R.string.app_name)
    }

    private fun setClickListener() {
        addNoteFloatingActionButton.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToAddNoteFragment()
            )
        )
    }

    private fun setupRecyclerView() {
        val recipe = listOf(
            Recipe("Тыквенный суп со вкусом карри", R.drawable.fav1, "4 порции", "25 мин"),
            Recipe("Салат Цезарь с чесночным соусом", R.drawable.fav2, "4 порции", "35 мин"),
            Recipe("Классический чизкейк", R.drawable.fav3, "12 порции", "1ч 45 мин"),
            Recipe("Фрикадельки по-шведски", R.drawable.fav4, "4 порции", "25 мин"),
            Recipe("Имбирное печенье с ванильным сахаром", R.drawable.fav5, "4 порции", "25 мин"),
            Recipe("Тыквенный суп со вкусом карри", R.drawable.fav1, "4 порции", "25 мин"),
            Recipe("Тыквенный суп со вкусом карри", R.drawable.fav1, "4 порции", "25 мин"),
            Recipe("Салат Цезарь с чесночным соусом", R.drawable.fav2, "4 порции", "35 мин"),
            Recipe("Классический чизкейк", R.drawable.fav3, "12 порции", "1ч 45 мин"),
            Recipe("Фрикадельки по-шведски", R.drawable.fav4, "4 порции", "25 мин"),
            Recipe("Имбирное печенье с ванильным сахаром", R.drawable.fav5, "4 порции", "25 мин"),
            Recipe("Тыквенный суп со вкусом карри", R.drawable.fav1, "4 порции", "25 мин"),
            Recipe("Тыквенный суп со вкусом карри", R.drawable.fav1, "4 порции", "25 мин"),
            Recipe("Тыквенный суп со вкусом карри", R.drawable.fav1, "4 порции", "25 мин"),
            Recipe("Салат Цезарь с чесночным соусом", R.drawable.fav2, "4 порции", "35 мин"),
            Recipe("Классический чизкейк", R.drawable.fav3, "12 порции", "1ч 45 мин"),
            Recipe("Фрикадельки по-шведски", R.drawable.fav4, "4 порции", "25 мин"),
            Recipe("Имбирное печенье с ванильным сахаром", R.drawable.fav5, "4 порции", "25 мин"),
            Recipe("Тыквенный суп со вкусом карри", R.drawable.fav1, "4 порции", "25 мин")
        )

        val myAdapter = FavAdapter(recipe) { position ->
//            val intent = Intent(context, LoggedInFavoriteItemFragment::class.java).apply {
//                putExtra("recipe", recipe[position])
//            }
//            startActivity(intent)
//            Navigation.createNavigateOnClickListener(
//                NotesBoardFragmentDirections.actionNotesBoardFragmentToNoteDetailsFragment()
//            )
            findNavController().navigate(R.id.action_notesBoardFragment_to_noteDetailsFragment)
        }

        val myViewManager = LinearLayoutManager(context)
        val recyclerView = notesRecycler.apply {
            setHasFixedSize(true)
            layoutManager = myViewManager
            adapter = myAdapter
        }


    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment


        return inflater.inflate(R.layout.fragment_notes_board, container, false);
    }

}