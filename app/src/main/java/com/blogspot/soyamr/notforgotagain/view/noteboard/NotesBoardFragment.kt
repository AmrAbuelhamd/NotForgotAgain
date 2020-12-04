package com.blogspot.soyamr.notforgotagain.view.noteboard

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.databinding.FragmentNotesBoardBinding
import com.blogspot.soyamr.notforgotagain.domain.NoteBoss
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import com.blogspot.soyamr.notforgotagain.view.recycler_view_components.NoteAdapter
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_notes_board.*


class NotesBoardFragment : Fragment() {
    var notes = ArrayList<NoteBoss>()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setUpToolBar(view)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        setClickListener()
        setupRecyclerView();

        viewModel.notes.observe(viewLifecycleOwner, { myNotes ->
            myAdapter.updateData(myNotes)
        })

        viewModel.isLoading.observe(viewLifecycleOwner, { refresh ->
            swiperefresh.isRefreshing = refresh
        })

        viewModel.errorMessageSnakeBar.observe(viewLifecycleOwner, {
            showError(it)
        })
    }

    private fun showError(errorMessage: String) {
        if (errorMessage.isNullOrEmpty())
            return
        Snackbar.make(binding.root, errorMessage, Snackbar.LENGTH_SHORT).show()
    }

    private val repository: NoteRepository by lazy { NoteRepository(requireContext()) }
    private val viewModel: NoteBoardViewModel by viewModels { NoteBoardViewModelFactory(repository) }

    private fun setUpToolBar(view: View) {
        val toolBar = view.findViewById<Toolbar>(R.id.my_toolbar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolBar)
        toolBar.title = getString(R.string.app_name)
        toolBar.post {
            toolBar.inflateMenu(R.menu.sign_out)
        }

        toolBar.setOnMenuItemClickListener {
            if (it.itemId == R.id.signOut) {
                viewModel.logOutUser()
                findNavController().navigate(
                    NotesBoardFragmentDirections.actionNotesBoardFragmentToSignInFragment()
                )
            } else {
                viewModel.refresh()
            }

            true
        }

        swiperefresh.setOnRefreshListener {
            viewModel.refresh()
        }

    }

    private fun setClickListener() {
        addNoteFloatingActionButton.setOnClickListener {
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToAddNoteFragment(-1L)
            )
        }
    }


    lateinit var myAdapter: NoteAdapter
    private fun setupRecyclerView() {
        myAdapter = NoteAdapter(notes, { position ->
            findNavController().navigate(
                NotesBoardFragmentDirections.actionNotesBoardFragmentToNoteDetailsFragment(notes[position].note?.nId!!)
            )
        }, { position, checked ->
            viewModel.onCheckBoxClicked(notes[position].note?.nId!!, checked)
        })

        val myViewManager = LinearLayoutManager(context)
        val recyclerView = notesRecycler.apply {
            setHasFixedSize(true)
            layoutManager = myViewManager
            adapter = myAdapter
        }

        val simpleItemTouchCallback: ItemTouchHelper.SimpleCallback =
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun getSwipeThreshold(viewHolder: RecyclerView.ViewHolder): Float {
                    return if (viewHolder is NoteAdapter.ViewHolderHeader) 1f else super.getSwipeThreshold(
                        viewHolder
                    )
                }

                override fun getSwipeDirs(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder
                ): Int {
                    if (viewHolder is NoteAdapter.ViewHolderHeader) return 0
                    return super.getSwipeDirs(recyclerView, viewHolder)
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, swipeDir: Int) {
                    val position = viewHolder.adapterPosition
                    viewModel.removeItem(notes[position].note?.nId!!)
                }
            }

        val itemTouchHelper = ItemTouchHelper(simpleItemTouchCallback)
        itemTouchHelper.attachToRecyclerView(recyclerView)
    }


    lateinit var binding: FragmentNotesBoardBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_notes_board, container, false)
        return binding.root

    }

}