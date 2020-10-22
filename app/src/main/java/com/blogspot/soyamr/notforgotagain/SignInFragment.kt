package com.blogspot.soyamr.notforgotagain

import android.content.Intent
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
        setButtonsClicks()
    }

    private fun setActionBar(view: View) {
        // Set the Toolbar as your fragment's ActionBar
        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar);
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        NavigationUI.setupActionBarWithNavController(
            requireActivity() as AppCompatActivity,
            findNavController()
        )
        toolbar.title = "sign in"
    }

    private fun setButtonsClicks() {
        signInButtonView.setOnClickListener() {
            startActivity(Intent(requireContext(), LoggedInHostActivity::class.java))
            activity?.finish()//LIDIA here i am finishing the previous loggedOutHostActivity and
            //  removing it from stack, so that pressing back will not take me
            //  back to log or sign in screens
        }

        signUpTextView.setOnClickListener(
            Navigation.createNavigateOnClickListener(//Lidia i am making use of all functions of nav component
                SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sign_in, container, false)
    }
}