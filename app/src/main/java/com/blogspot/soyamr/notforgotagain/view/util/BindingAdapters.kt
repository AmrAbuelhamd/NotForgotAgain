package com.blogspot.soyamr.notforgotagain.view.util

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("app:errorText")
fun setErrorText(view: TextInputLayout, errorMessage: String) {
    println("inside adapter")
    println("error $errorMessage .")
    if (errorMessage.isEmpty())
        view.error = null
    else
        view.error = errorMessage;
}

//@BindingAdapter("android:text")
//fun validateText(text: String) {
////    view.error = errorMessage;
//}