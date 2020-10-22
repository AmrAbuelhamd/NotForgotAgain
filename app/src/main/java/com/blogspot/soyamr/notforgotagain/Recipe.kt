package com.blogspot.soyamr.notforgotagain

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Recipe(val name: String, val img: Int, val str1: String, val str2: String): Parcelable