package com.blogspot.soyamr.notforgotagain.view.signup

import android.app.AlertDialog
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
import com.blogspot.soyamr.notforgotagain.R
import com.blogspot.soyamr.notforgotagain.databinding.FragmentSignUpBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_sign_up.*

@AndroidEntryPoint
class SignUpFragment : Fragment() {

//    private val repository: NoteRepository by lazy { NoteRepository(requireContext()) }
    private val viewModel: SignUpViewModel by viewModels()// { SignUpViewModelFactory(repository) }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setListeners()
        setActionBar(view)

        setUpViewModelCalls()
    }

    private fun setUpViewModelCalls() {
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        viewModel.success.observe(viewLifecycleOwner, Observer {
            if (it) {
                findNavController().navigate(
                    SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
                )
            }
        })

        viewModel.errorMessageSnakeBar.observe(viewLifecycleOwner, Observer {
            showError(it)
        })
    }

    private fun setActionBar(view: View) {
        // Set the Toolbar as your fragment's ActionBar
        val toolbar = view.findViewById<Toolbar>(R.id.my_toolbar);
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)
        toolbar.title = getString(R.string.createaccount)
    }

    private fun setListeners() {
        signInTextView.setOnClickListener(
            Navigation.createNavigateOnClickListener(
                SignUpFragmentDirections.actionSignUpFragmentToSignInFragment()
            )
        )
    }
    private fun showError(errorMessage: String) {
        if (errorMessage.isNullOrEmpty())
            return
        val alertDialog: AlertDialog = AlertDialog.Builder(requireContext()).create()
        alertDialog.setTitle("Error")
        alertDialog.setMessage(errorMessage)
        alertDialog.setButton(
            AlertDialog.BUTTON_NEUTRAL, "OK"
        ) { dialog, _ -> dialog.dismiss() }
        alertDialog.show()
    }

    lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sign_up, container, false)

        return binding.root
    }
}