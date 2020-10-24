package com.blogspot.soyamr.notforgotagain

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.fragment_sign_up.*
import kotlinx.android.synthetic.main.fragment_sign_up.view.*


class SignUpFragment : Fragment() {


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        setActionBar(view)
    }
    private fun setActionBar(view: View) {
        // Set the Toolbar as your fragment's ActionBar
        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar);
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.createaccount)
    }

    private fun setListeners() {
        signUpButtonView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            )
        )

        signInTextView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            )
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sign_up, container, false);
    }
}