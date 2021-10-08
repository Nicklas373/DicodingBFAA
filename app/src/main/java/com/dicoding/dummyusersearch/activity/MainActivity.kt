package com.dicoding.dummyusersearch.activity

import android.app.SearchManager
import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.adapter.GithubUserAdapter
import com.dicoding.dummyusersearch.databinding.ActivityMainBinding
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.dicoding.dummyusersearch.viewmodel.MainActivityViewModel

class MainActivity : AppCompatActivity() {
    private val listGitHubUser = ArrayList<GitHubUserArray>()
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mainViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(MainActivityViewModel::class.java)
        mainViewModel.githubUserArray.observe(this, { userArray ->
            setGitHubUserData(userArray)
        })

        mainViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        mainViewModel.isToast.observe(this,{ isToast ->
          showToast(isToast, mainViewModel.toastReason.toString())
        })

        val layoutManager = LinearLayoutManager(this)
        binding.listGithubUser.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.listGithubUser.addItemDecoration(itemDecoration)

        title = resources.getString(R.string.app_name)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = findViewById<SearchView>(R.id.gitSearch)
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.git_search)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                listGitHubUser.clear()
                mainViewModel.findGitHubUserID(query)
                val adapter = GithubUserAdapter(listGitHubUser)
                binding.listGithubUser.adapter = adapter
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
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

    private fun showToast(isToast: Boolean, toastReason: String) {
        if (isToast) {
            Toast.makeText(this, "It's Safe", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, toastReason, Toast.LENGTH_SHORT).show()
        }
    }
}