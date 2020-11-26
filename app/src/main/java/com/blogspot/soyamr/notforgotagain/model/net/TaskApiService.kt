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

    @PATCH("tasks/{id}")
    suspend fun updateTask(@Body updatedTask: UpdatedTask, @Path("id") id: Long): Task


}