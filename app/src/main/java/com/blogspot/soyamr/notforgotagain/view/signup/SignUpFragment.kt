package com.blogspot.soyamr.notforgotagain.view.signup

import android.content.Context
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
import kotlinx.android.synthetic.main.fragment_sign_up.*


class SignUpFragment : Fragment(), SignUpView {
    private lateinit var presenter: SignUpPresenter


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = SignUpPresenter(this)
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

            val name = nameSignUpTextInputLayout.editText?.text.toString().trim()
            val email = emailSignUpTextInputLayout.editText?.text.toString().trim()
            val password = passwordSignUpTextInputLayout.editText?.text.toString().trim()

            presenter.insertUser(name, email, password)
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

    override fun getRequiredContext(): Context {
        return requireContext()
    }

    override fun goToLogInScreen() {
        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToSignInFragment())
    }
}