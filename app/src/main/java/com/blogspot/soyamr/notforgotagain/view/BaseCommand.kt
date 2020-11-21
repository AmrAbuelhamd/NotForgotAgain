package com.blogspot.soyamr.notforgotagain.view

sealed class BaseCommand {
    class Error(val errorString: String): BaseCommand()

    class Success(val toastMessage: String?): BaseCommand()
}