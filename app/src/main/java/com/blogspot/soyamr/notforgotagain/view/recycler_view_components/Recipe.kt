package com.blogspot.soyamr.notforgotagain.view.recycler_view_components

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(val name: String, val img: Int, val str1: String, val str2: String): Parcelable