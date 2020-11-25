package com.blogspot.soyamr.notforgotagain.model.db.tables

data class FullNoteData(
    var cId: Long,
    val cName: String,
    val pId: Long,
    val pName: String,
    val pColor: String,
    val nId:Long,
    val nTitle: String,
    val nDescription: String?,
    val nDone: Boolean = false,
    val nDeadline: Long?,
    val nCreated: Long?,
)