package com.dicoding.dummyusersearch.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.adapter.FavouriteUserAdapter
import com.dicoding.dummyusersearch.database.FavouriteDB
import com.dicoding.dummyusersearch.databinding.ActivityFavouriteBinding
import com.dicoding.dummyusersearch.viewmodel.FavouriteDBViewModel

class FavouriteActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavouriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favourite)

        binding = ActivityFavouriteBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)

        binding.listGithubUser.layoutManager = layoutManager
        binding.listGithubUser.addItemDecoration(itemDecoration)

        title = resources.getString(R.string.favourite)

        val favouriteViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                FavouriteDBViewModel::class.java
            )

        favouriteViewModel.subscribeFavResult(this).observe(this, { favResult ->
            setGitHubUserFavouriteData(favResult)
        })

    }

    private fun setGitHubUserFavouriteData(listGithubUserID: List<FavouriteDB>) {
        val listReview = ArrayList<FavouriteDB>()
        for (userID in listGithubUserID) {
            val user = FavouriteDB(userID.id, userID.login, userID.htmlUrl, userID.avatarUrl)
            listReview.add(user)
        }
        val adapter = FavouriteUserAdapter(listReview)
        binding.listGithubUser.adapter = adapter
    }
}