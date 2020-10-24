package com.blogspot.soyamr.notforgotagain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setActionBar(view)
        setOnClickListeners()
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
        //Lidia i am making use of all functions of nav component
        // -  type-safe navigation between destinations.
        //  the new way of navigation also it guarantees compile-time safety.

        signInButtonView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SignInFragmentDirections.actionSignInFragmentToNotesBoardFragment()
            )
        )

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