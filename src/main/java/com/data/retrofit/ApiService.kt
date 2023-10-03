package com.data.retrofit

import com.data.response.GithubResponse
//import com.data.response.UserResponse
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    @GET("search/users")
    @Headers("Authorization: token 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")
    fun getSearchUsers(
        @Query("q") query: String,
    ): Call<GithubResponse>
}