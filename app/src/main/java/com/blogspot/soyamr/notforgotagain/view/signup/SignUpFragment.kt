package com.blogspot.soyamr.notforgotagain.view.signup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.model.NotesDataBase
import com.blogspot.soyamr.notforgotagain.model.User
import com.blogspot.soyamr.notforgotagain.view.SignUpFragmentDirections
import kotlinx.android.synthetic.main.fragment_sign_up.*


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
        signUpButtonView.setOnClickListener {
            val user = User(
                nameSignUpTextInputLayout.editText?.text.toString().trim(),
                emailSignUpTextInputLayout.editText?.text.toString().trim(),
                passwordSignUpTextInputLayout.editText?.text.toString().trim()
            )
            val userDao = NotesDataBase.getDatabase(requireActivity()).userDao()
            Navigation.createNavigateOnClickListener(
                SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            )
            userDao.insertUser(user)
            findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
        }

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