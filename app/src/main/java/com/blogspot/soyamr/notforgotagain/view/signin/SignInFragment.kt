package com.blogspot.soyamr.notforgotagain.view.signin

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.databinding.FragmentSignInBinding
import com.blogspot.soyamr.notforgotagain.model.NoteRepository
import kotlinx.android.synthetic.main.fragment_sign_in.*


class SignInFragment : Fragment(), SignInView {

    // private lateinit var presenter: SignInPresenter
    private val repository: NoteRepository by lazy { NoteRepository(requireContext()) }
    private val viewModel: SignInViewModel by viewModels { SignInViewModelFactory(repository) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        // presenter = SignInPresenter(this)
        //presenter.checkSignedIn()

        setUpViewModelCalls()


        setActionBar(view)
        setOnClickListeners()
    }

    private fun setUpViewModelCalls() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        viewModel.doWeHaveAlreadySignedInUser.observe(this, Observer {
            if (it) {
                findNavController().navigate(
                    SignInFragmentDirections.actionSignInFragmentToNotesBoardFragment(
                        0
                    )
                )
            }
        })
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
//        signInButtonView.setOnClickListener {
//            val email = emailTextInputLayout.editText?.text.toString().trim()
//            val password = passwordTextInputLayout.editText?.text.toString().trim()
//            presenter.signIn(email, password)
//        }

        createAccountTextView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SignInFragmentDirections.actionSignInFragmentToSignUpFragment()
            )
        )
    }

    lateinit var binding: FragmentSignInBinding
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_in, container, false)

        return binding.root
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