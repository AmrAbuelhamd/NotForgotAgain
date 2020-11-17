package com.blogspot.soyamr.notforgotagain.domain

class Priority(val name: String, val id: Long) : GeneralData {

    override fun getItemName(): String {
        return name
    }

    override fun getID(): Long {
        return id
    }
}