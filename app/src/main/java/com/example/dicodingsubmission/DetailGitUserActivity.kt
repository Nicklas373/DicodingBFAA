package com.example.dicodingsubmission

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class DetailGitUserActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_GITHUB_USER = "extra_github_user"
    }

    // Declare variable for textview and etc
    lateinit var github_user_name: TextView
    lateinit var github_user_id: TextView
    lateinit var github_user_repository: TextView
    lateinit var github_user_follower: TextView
    lateinit var github_user_following: TextView
    lateinit var github_user_location: TextView
    lateinit var github_user_company: TextView
    lateinit var github_user_image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_github_user)

        // Bind controller
        val github_user_name: TextView = findViewById(R.id.git_name)
        val github_user_id: TextView = findViewById(R.id.git_id)
        val github_user_repository: TextView = findViewById(R.id.git_repo_txt)
        val github_user_follower: TextView = findViewById(R.id.git_follower_txt)
        val github_user_following: TextView = findViewById(R.id.git_following_txt)
        val github_user_location: TextView = findViewById(R.id.git_location_txt)
        val github_user_company: TextView = findViewById(R.id.git_company_txt)
        val github_user_image: ImageView = findViewById(R.id.git_profile_imageview)

        // Configuring
        val gituser = intent.getParcelableExtra<GitHubUser>(EXTRA_GITHUB_USER) as GitHubUser
        github_user_name.text = gituser.gitName
        github_user_id.text = gituser.gitId
        github_user_repository.text = gituser.gitRepo
        github_user_follower.text = gituser.gitFollower
        github_user_following.text = gituser.gitFollowing
        github_user_location.text = gituser.gitLocation
        github_user_company.text = gituser.gitComp

        when (gituser.gitId) {
            "JakeWharton" -> {
                github_user_image.setImageResource(R.drawable.user1)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "amitshekhariitbhu" -> {
                github_user_image.setImageResource(R.drawable.user2)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "romainguy" -> {
                github_user_image.setImageResource(R.drawable.user3)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "chrisbanes" -> {
                github_user_image.setImageResource(R.drawable.user4)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "tipsy" -> {
                github_user_image.setImageResource(R.drawable.user5)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "ravi8x" -> {
                github_user_image.setImageResource(R.drawable.user6)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "jasoet" -> {
                github_user_image.setImageResource(R.drawable.user7)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "budioktaviyan" -> {
                github_user_image.setImageResource(R.drawable.user8)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "hendisantika" -> {
                github_user_image.setImageResource(R.drawable.user9)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }

            "sidiqpermana" -> {
                github_user_image.setImageResource(R.drawable.user10)
                Toast.makeText(this, gituser.gitId, Toast.LENGTH_SHORT).show()
            }
        }
    }
}