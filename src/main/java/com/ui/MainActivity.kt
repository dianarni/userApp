package com.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.data.response.GithubResponse
import com.data.response.ItemsItem
import com.data.retrofit.ApiConfig
import com.example.githubuserapp.R
import com.example.githubuserapp.databinding.ActivityMainBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: MainViewModel
    private lateinit var adapter: UsersNameAdapter

    companion object {
        private const val TAG = "MainActivity"
        private const val GITHUB_ID = "142962375"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        adapter = UsersNameAdapter()
        adapter.notifyDataSetChanged()
        viewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainViewModel::class.java)

        binding.apply {
            rvUser.layoutManager = LinearLayoutManager (this@MainActivity)
            rvUser.setHasFixedSize(true)
            rvUser.adapter = adapter

            btnSearch.setOnClickListener {
                searchUser()
            }

            EtSearchName.setOnKeyListener { v, i, keyEvent ->
                if (keyEvent.action == KeyEvent.ACTION_DOWN && i == KeyEvent.KEYCODE_ENTER) {
                    return@setOnKeyListener true
                }
                return@setOnKeyListener false
            }
        }
viewModel.getSearchUsers().observe(this) {
    if (it != null) {
        adapter.submitList(it)
        showLoading(false)
    }
}

        val layoutManager = LinearLayoutManager(this)
        binding.rvUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvUser.addItemDecoration(itemDecoration)

        findGithub()
    }

    private fun findGithub() {
        showLoading(true)
        val client = ApiConfig.getApiService().getSearchUsers(GITHUB_ID)
        client.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        setUserData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setUserData(username: List<ItemsItem>) {
        val adapter = UsersNameAdapter()
        adapter.submitList(username)
        binding.rvUser.adapter = adapter
        binding.EtSearchName.setText("")
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.ProgressBar.visibility = View.VISIBLE
        } else {
            binding.ProgressBar.visibility = View.GONE
        }
    }

    private fun searchUser(){
        binding.apply {
            val query = EtSearchName.text.toString()
            if (query.isEmpty()) return
            showLoading(true)
            viewModel.setSearchUsers(query)
        }

    }
}
