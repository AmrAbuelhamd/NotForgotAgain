package com.blogspot.soyamr.notforgotagain.view

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.model.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.NoteHeader
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.view.recycler_view_components.NoteAdapter
import com.blogspot.soyamr.notforgotagain.view.recycler_view_components.Recipe
import kotlinx.android.synthetic.main.fragment_notes_board.*


class NotesBoardFragment : Fragment() {
    var notes = ArrayList<NoteBoss>()
    var currentUserId: Long? = 0L
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val args = arguments?.let { NotesBoardFragmentArgs.fromBundle(it) }
        currentUserId = args?.uId
        Log.i("please: ", " )))) " + currentUserId)
        setUpToolBar(view)
        setClickListener()
        setupRecyclerView();
    }

    private fun setUpToolBar(view: View) {
        val toolBar = view.findViewById<Toolbar>(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.title = getString(R.string.app_name)
        toolBar.post {
            toolBar.inflateMenu(R.menu.sign_out)
        }
        val repository = NoteRepository(requireContext())
        toolBar.setOnMenuItemClickListener {
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToSignInFragment()
            )
            repository.signMeOut(currentUserId!!)
            true
        }

    }

    private fun setClickListener() {
        addNoteFloatingActionButton.setOnClickListener {
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToAddNoteFragment(currentUserId!!)
            )

        }
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

        val myAdapter = NoteAdapter(recipe) { position ->
//            val intent = Intent(context, LoggedInFavoriteItemFragment::class.java).apply {
//                putExtra("recipe", recipe[position])
//            }
//            startActivity(intent)
//            Navigation.createNavigateOnClickListener(
//                NotesBoardFragmentDirections.actionNotesBoardFragmentToNoteDetailsFragment()
//            )
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToNoteDetailsFragment(
                    0L
                )
            )
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