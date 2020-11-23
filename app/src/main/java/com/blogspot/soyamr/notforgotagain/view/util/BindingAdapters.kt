package com.blogspot.soyamr.notforgotagain.view.util

import android.view.View
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout


@BindingAdapter("app:errorText")
fun setErrorText(view: TextInputLayout, errorMessage: String) {
    if (errorMessage.isEmpty())
        view.error = null
    else
        view.error = errorMessage;
}

//@BindingAdapter("app:visibleGone")
//fun setVisibleGone(view: View, show: Boolean) {
//    view.visibility = if (show) View.VISIBLE else View.GONE
//    print("here here here")
//}

//@BindingAdapter("android:text")
//fun validateText(text: String) {
////    view.error = errorMessage;
//}