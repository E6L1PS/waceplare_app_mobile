package com.itacademy.data.api

import com.itacademy.data.model.auth.AuthenticationRequest
import com.itacademy.data.model.auth.RegisterRequest
import com.itacademy.data.model.auth.TokenResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @POST("auth/register")
    suspend fun signUp(@Body request: RegisterRequest): TokenResponse

    @POST("auth/authenticate")
    suspend fun signIn(@Body request: AuthenticationRequest): TokenResponse

    @GET("auth/check")
    suspend fun isAuthenticated(): Boolean

}