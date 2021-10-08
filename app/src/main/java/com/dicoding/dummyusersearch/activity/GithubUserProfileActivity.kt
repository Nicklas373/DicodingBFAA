package com.dicoding.dummyusersearch.activity

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.userdata.GitHubUserJSON
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubUserProfileActivity : AppCompatActivity() {

    companion object {
        private val TAG = GithubUserProfileActivity::class.java.simpleName
        const val EXTRA_GITHUB_USER = "extra_github_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_profile)

        val gitUser = intent.getStringExtra(EXTRA_GITHUB_USER)
        if (gitUser != null) {
            getGitHubUserData(gitUser)
            title = gitUser
        } else {
            getGitHubUserData("Null")
        }
    }

    private fun getGitHubUserData(query: String) {
        val client = ApiConfig.getApiService().getUserDetail(query)
        client.enqueue(object : Callback<GitHubUserJSON> {
            override fun onResponse(
                call: Call<GitHubUserJSON>,
                response: Response<GitHubUserJSON>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        val gitNameText = findViewById<TextView>(R.id.git_name)
                        val gitEmailText = findViewById<TextView>(R.id.git_mail)
                        val gitLocationText = findViewById<TextView>(R.id.git_location)
                        val gitImageDraw = findViewById<CircleImageView>(R.id.git_profile_imageview)
                        val gitCompanyText = findViewById<TextView>(R.id.git_company)
                        val gitJoinText = findViewById<TextView>(R.id.git_join)
                        val gitFollowersText = findViewById<TextView>(R.id.git_followers_count)
                        val gitFollowingText = findViewById<TextView>(R.id.git_following_count)
                        val gitRepositoryText = findViewById<TextView>(R.id.git_repository_count)
                        val dateJoin: String? = responseBody.created_at
                        val splitDate = dateJoin?.substring(0, dateJoin.length - 10)

                        gitNameText.text = responseBody.name
                        gitEmailText.text = responseBody.email
                        gitLocationText.text = responseBody.location
                        gitCompanyText.text = responseBody.company
                        gitJoinText.text = splitDate
                        gitFollowersText.text = responseBody.followers
                        gitFollowingText.text = responseBody.following
                        gitRepositoryText.text = responseBody.public_repos
                        Picasso.get().load(responseBody.avatarUrl).into(gitImageDraw)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GitHubUserJSON>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}