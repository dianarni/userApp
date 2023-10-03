package com.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.data.response.GithubResponse
import com.data.response.ItemsItem
import com.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.collections.ArrayList

class MainViewModel : ViewModel() {

    val listUsers = MutableLiveData<ArrayList<ItemsItem>>()

    fun setSearchUsers(query : String) {
        ApiConfig.getApiService()
            .getSearchUsers(query)
            .enqueue(object : Callback<GithubResponse> {
                override fun onResponse(
                    call: Call<GithubResponse>,
                    response: Response<GithubResponse>
                ) {
                    if (response.isSuccessful) {
                        listUsers.postValue(response.body()?.items as ArrayList<ItemsItem>?)
                    }
                }

                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                    Log.d("Failure", t.message.toString())
                }
            })
    }
    fun getSearchUsers() : LiveData<ArrayList<ItemsItem>>{
        return listUsers
    }
}
