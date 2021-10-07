package com.dicoding.dummyusersearch.activity

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.adapter.GithubUserAdapter
import com.dicoding.dummyusersearch.databinding.ActivityMainBinding
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.dicoding.dummyusersearch.userdata.GitHubUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val listGitHubUser = ArrayList<GitHubUserArray>()
    private lateinit var binding: ActivityMainBinding

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.listGithubUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listGithubUser.addItemDecoration(itemDecoration)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.gitSearch)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.git_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                findGitHubUserID(query)
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
    }

    private fun findGitHubUserID(query: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().getUserQuery(query)
        client.enqueue(object : Callback<GitHubUserResponse> {
            override fun onResponse(
                call: Call<GitHubUserResponse>,
                response: Response<GitHubUserResponse>
            ) {
                showLoading(false)
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listGitHubUser.clear()
                        setGitHubUserData(responseBody.items)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GitHubUserResponse>, t: Throwable) {
                showLoading(false)
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setGitHubUserData(listGithubUserID: List<GitHubUserArray>) {
        val listReview = ArrayList<GitHubUserArray>()
        for (userID in listGithubUserID) {
            val user = GitHubUserArray(userID.login, userID.htmlUrl, userID.avatarUrl)
            listReview.add(user)
        }
        val adapter = GithubUserAdapter(listReview)
        binding.listGithubUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}