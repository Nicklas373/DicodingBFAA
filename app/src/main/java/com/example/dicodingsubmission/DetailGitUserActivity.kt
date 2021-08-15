package com.example.dicodingsubmission

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailGitUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_github_user)
        setSupportActionBar(findViewById(R.id.frame_ics_layout_toolbar))

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = resources.getString(R.string.app_name_secondary)

        val githubUserName: TextView = findViewById(R.id.git_name)
        val githubUserId: TextView = findViewById(R.id.git_id)
        val githubUserRepository: TextView = findViewById(R.id.git_repo_txt)
        val githubUserFollower: TextView = findViewById(R.id.git_follower_txt)
        val githubUserFollowing: TextView = findViewById(R.id.git_following_txt)
        val githubUserLocation: TextView = findViewById(R.id.git_location_txt)
        val githubUserCompany: TextView = findViewById(R.id.git_company_txt)
        val githubUserImage: ImageView = findViewById(R.id.git_profile_imageview)
        val gitUser = intent.getParcelableExtra<GitHubUser>(EXTRA_GITHUB_USER) as GitHubUser

        githubUserName.text = gitUser.gitName
        githubUserId.text = gitUser.gitId
        githubUserRepository.text = gitUser.gitRepo
        githubUserFollower.text = gitUser.gitFollower
        githubUserFollowing.text = gitUser.gitFollowing
        githubUserLocation.text = gitUser.gitLocation
        githubUserCompany.text = gitUser.gitComp

        when (gitUser.gitId) {
            resources.getStringArray(R.array.git_username)[0].toString() -> {
                githubUserImage.setImageResource(R.drawable.user1)
            }

            resources.getStringArray(R.array.git_username)[1].toString() -> {
                githubUserImage.setImageResource(R.drawable.user2)
            }

            resources.getStringArray(R.array.git_username)[2].toString() -> {
                githubUserImage.setImageResource(R.drawable.user3)
            }

            resources.getStringArray(R.array.git_username)[3].toString() -> {
                githubUserImage.setImageResource(R.drawable.user4)
            }

            resources.getStringArray(R.array.git_username)[4].toString() -> {
                githubUserImage.setImageResource(R.drawable.user5)
            }

            resources.getStringArray(R.array.git_username)[5].toString() -> {
                githubUserImage.setImageResource(R.drawable.user6)
            }

            resources.getStringArray(R.array.git_username)[6].toString() -> {
                githubUserImage.setImageResource(R.drawable.user7)
            }

            resources.getStringArray(R.array.git_username)[7].toString() -> {
                githubUserImage.setImageResource(R.drawable.user8)
            }

            resources.getStringArray(R.array.git_username)[8].toString() -> {
                githubUserImage.setImageResource(R.drawable.user9)
            }

            resources.getStringArray(R.array.git_username)[9].toString() -> {
                githubUserImage.setImageResource(R.drawable.user10)
            }
        }
    }
}