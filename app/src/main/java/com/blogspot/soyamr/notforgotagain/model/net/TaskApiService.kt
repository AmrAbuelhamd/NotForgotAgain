package com.blogspot.soyamr.notforgotagain.model.net

import com.blogspot.soyamr.notforgotagain.model.net.pojo.Category
import com.blogspot.soyamr.notforgotagain.model.net.pojo.LoginUser
import com.blogspot.soyamr.notforgotagain.model.net.pojo.Task
import com.blogspot.soyamr.notforgotagain.model.net.pojo.UserToken
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST

interface TaskApiService {

    @POST("login")
    @Headers("No-Authentication: true")
    suspend fun login(@Body loginUser: LoginUser): UserToken

    @GET("tasks")
    suspend fun getTasks(): List<Task>

    @GET("categories")
    suspend fun getCategories(): List<Category>
}