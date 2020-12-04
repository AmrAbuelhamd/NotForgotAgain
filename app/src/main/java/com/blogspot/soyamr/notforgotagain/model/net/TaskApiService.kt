package com.blogspot.soyamr.notforgotagain.model.net

import com.blogspot.soyamr.notforgotagain.model.net.pojo.*
import retrofit2.http.*

interface TaskApiService {

    @POST("login")
    @Headers("No-Authentication: true")
    suspend fun login(@Body loginUser: LoginUser): UserToken

    @GET("tasks")
    suspend fun getTasks(): List<Task>

    @GET("categories")
    suspend fun getCategories(): List<Category>

    @GET("priorities")
    suspend fun getPriorities(): List<Priority>

    @PATCH("tasks/{id}")
    suspend fun updateTask(@Body updatedTask: UpdatedTask, @Path("id") id: Long): Task

    @POST("categories")
    suspend fun addNewCategory(@Body newCategory: NewCategory): Category

    @POST("tasks")
    suspend fun addNewTask(@Body newTask: NewTask): Task

    @POST("register")
    @Headers("No-Authentication: true")
    suspend fun registerUser(@Body newUser: NewUser): User

    @DELETE("tasks/{id}")
    suspend fun deleteNote(@Path("id") id: Int):DeletedTaskResponse


}