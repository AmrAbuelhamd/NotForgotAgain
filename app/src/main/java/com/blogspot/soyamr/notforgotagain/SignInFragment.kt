package com.blogspot.soyamr.notforgotagain

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.blogspot.soyamr.notforgotagain.model.NotesDataBase
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        checkSignedIn()
        setActionBar(view)
        setOnClickListeners()
    }

    private fun checkSignedIn() {
        val userDao = NotesDataBase.getDatabase(requireActivity()).userDao()
        val id = userDao.getSignedInUser()
        Log.i("dIdSign ", "" + id)
        if (id != 0)
            findNavController().navigate(
                SignInFragmentDirections.actionSignInFragmentToNotesBoardFragment()
            )
    }

    private fun setActionBar(view: View) {
        // Set the Toolbar as your fragment's ActionBar
        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar);
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(
            requireActivity() as AppCompatActivity,
            findNavController()
        )
        toolbar.title = getString(R.string.sign_in)
    }

    private fun setOnClickListeners() {
        signInButtonView.setOnClickListener {
            val email = emailTextInputLayout.editText?.text.toString().trim()
            val password = passwordTextInputLayout.editText?.text.toString().trim()
            val userDao = NotesDataBase.getDatabase(requireActivity()).userDao()
            val id = userDao.doWeHaveSuchUser(email, password)
            Log.i("dId ",""+id)
            Log.i("demail ",""+email)
            Log.i("dpassword ",""+password)
            if (id == 1)
                findNavController().navigate(
                    SignInFragmentDirections.actionSignInFragmentToNotesBoardFragment()
                )
            else
            {
                emailTextInputLayout.error = "doesn't exist"
            }
        }

        createAccountTextView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            )
        )
    }

    override fun onCreateView( //lidia - now my code is divided inside oncreate view and onciewcreated
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }
}