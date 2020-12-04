package com.blogspot.soyamr.notforgotagain.model.db.tables

import com.blogspot.soyamr.notforgotagain.model.net.pojo.NewTask

data class FullNoteData(
    var cId: Long,
    val cName: String,
    val pId: Long,
    val pName: String,
    val pColor: String,
    val nId:Long,
    val nTitle: String,
    val nDescription: String?,
    val nDone: Boolean,
    val nDeadline: Long?,
    val nCreated: Long?,
) {
    fun toNetTask(
        title: String = this.nTitle,
        description: String? = this.nDescription,
        done: Int = if (this.nDone) 1 else 0,
        deadline: Long? = this.nDeadline,
        parentCategoryId: Long = this.cId,
        parentPriorityId: Long = this.pId,
    ) = NewTask(
        title,
        description,
        done,
        deadline,
        parentCategoryId,
        parentPriorityId
    )
}