package com.blogspot.soyamr.notforgotagain.view.signin

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
import androidx.navigation.ui.NavigationUI
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.view.dialogfragments.LoadingDialogFragment
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(), SignInView {

    private lateinit var presenter: SignInPresenter
//    lateinit var loadingDialogFragment: LoadingDialogFragment
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter = SignInPresenter(this)
//        loadingDialogFragment = LoadingDialogFragment(requireContext())
        presenter.checkSignedIn()

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
        signInButtonView.setOnClickListener {
            val email = emailTextInputLayout.editText?.text.toString().trim()
            val password = passwordTextInputLayout.editText?.text.toString().trim()
            presenter.signIn(email, password)
        }

        createAccountTextView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
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

    override fun getRequiredContext(): Context {
        return requireContext()
    }

    override fun moveToNoteBoard(userId: Long) {
        findNavController().navigate(
            SignInFragmentDirections.actionSignInFragmentToNotesBoardFragment(userId)
        )
    }

    override fun setSignInError() {
        emailTextInputLayout.error = "wrong user name or password"
    }

    override fun showProgressBar() {
//        loadingDialogFragment.show()
    }

    override fun hidProgressBar() {
//        loadingDialogFragment.hide()
    }
}