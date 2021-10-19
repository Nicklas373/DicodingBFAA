package com.dicoding.dummyusersearch.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.database.FavouriteRoomDB
import com.dicoding.dummyusersearch.databinding.ActivityGithubUserProfileBinding
import com.dicoding.dummyusersearch.userdata.GitHubUserJSON
import com.dicoding.dummyusersearch.viewmodel.GitHubUserProfileActivityViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso

class GithubUserProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGithubUserProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_profile)

        binding = ActivityGithubUserProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val userProfileViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                GitHubUserProfileActivityViewModel::class.java
            )

        val sharedPref = this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val gitUserSp = sharedPref.getString(keyId, "null")
        val gitHtmlSp = sharedPref.getString(urlId, "null")
        val gitImageSp = sharedPref.getString(imageId, "null")
        val sectionsPagerAdapter = SectionPagerActivity(this)
        val viewPager: ViewPager2 = binding.viewPager
        val tabs: TabLayout = binding.tabs

        userProfileViewModel.githubUserProfileJSON.observe(this, { userJSON ->
            if (gitUserSp != null) {
                setGitHubUserData(userJSON)
            }
        })

        if (gitUserSp != null) {
            userProfileViewModel.getGitHubUserData(gitUserSp)
            title = gitUserSp
        } else {
            userProfileViewModel.getGitHubUserData("Null")
        }

        userProfileViewModel.isToast.observe(this, { isToast ->
            showToast(isToast, userProfileViewModel.toastReason.value.toString())
        })

        userProfileViewModel.isLoading.observe(this, {
            showLoading(it)
        })

        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 2
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        checkFavourite(gitUserSp.toString())

        binding.fabAdd.setOnClickListener {
            val database = FavouriteRoomDB.getDatabase(this).favouriteDao()
            val exist = database.checkUserFavourites(gitUserSp.toString())
            createAlertDialog(
                !exist,
                gitUserSp.toString(),
                gitImageSp.toString(),
                gitHtmlSp.toString()
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        setMode(item.itemId)
        return super.onOptionsItemSelected(item)
    }

    private fun setMode(selectedMode: Int) {
        when (selectedMode) {
            R.id.action_favourite -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
            R.id.action_settings -> {
                val intent = Intent(this, SettingsActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun setGitHubUserData(userJSON: GitHubUserJSON) {
        val gitNameText = binding.gitName
        val gitEmailText = binding.gitMail
        val gitLocationText = binding.gitLocation
        val gitImageDraw = binding.gitProfileImageview
        val gitCompanyText = binding.gitCompany
        val gitJoinText = binding.gitJoin
        val gitFollowersText = binding.gitFollowersCount
        val gitFollowingText = binding.gitFollowingCount
        val gitRepositoryText = binding.gitRepositoryCount

        if (userJSON.name.isNullOrBlank()) {
            gitNameText.text = "-"
        } else {
            gitNameText.text = userJSON.name
        }

        if (userJSON.email.isNullOrBlank()) {
            gitEmailText.text = "-"
        } else {
            gitEmailText.text = userJSON.email
        }

        if (userJSON.location.isNullOrBlank()) {
            gitLocationText.text = "-"
        } else {
            gitLocationText.text = userJSON.location
        }

        if (userJSON.company.isNullOrBlank()) {
            gitCompanyText.text = "-"
        } else {
            gitCompanyText.text = userJSON.company
        }

        if (userJSON.createdAt.isNullOrBlank()) {
            gitJoinText.text = "-"
        } else {
            val splitDate = userJSON.createdAt.substring(0, userJSON.createdAt.length - 10)
            gitJoinText.text = splitDate
        }

        gitFollowersText.text = userJSON.followers
        gitFollowingText.text = userJSON.following
        gitRepositoryText.text = userJSON.publicRepos
        Picasso.get().load(userJSON.avatarUrl).into(gitImageDraw)
    }

    private fun showToast(isToast: Boolean, toastReason: String) {
        if (!isToast) {
            Toast.makeText(this, toastReason, Toast.LENGTH_LONG).show()
        }
    }

    private fun checkFavourite(username: String) {
        val database =
            FavouriteRoomDB.getDatabase(this).favouriteDao()
        val exist = database.checkUserFavourites(username)

        if (exist) {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_24)
        } else {
            binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_border_24)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
            binding.gitProfileImageview.visibility = View.VISIBLE
            binding.gitName.visibility = View.VISIBLE
            binding.gitNameIcon.visibility = View.VISIBLE
            binding.gitMail.visibility = View.VISIBLE
            binding.gitEmailIcon.visibility = View.VISIBLE
            binding.gitLocation.visibility = View.VISIBLE
            binding.gitLocationIcon.visibility = View.VISIBLE
            binding.gitRepository.visibility = View.VISIBLE
            binding.gitRepositoryCount.visibility = View.VISIBLE
            binding.gitFollowers.visibility = View.VISIBLE
            binding.gitFollowersCount.visibility = View.VISIBLE
            binding.gitFollowing.visibility = View.VISIBLE
            binding.gitFollowingCount.visibility = View.VISIBLE
        }
    }

    private fun createAlertDialog(
        state: Boolean,
        userId: String,
        userAvatar: String,
        userHtml: String
    ) {
        val userProfileViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                GitHubUserProfileActivityViewModel::class.java
            )

        val alertDialogBuilder = AlertDialog.Builder(this)

        val message =
            if (state) userId + " " + (resources.getString(R.string.favourite_not_in_list_notification)) else userId + " " + (resources.getString(
                R.string.favourite_in_list_notification
            ))

        with(alertDialogBuilder) {
            setTitle(R.string.favourite)
            setMessage(message)
            setCancelable(false)
            setPositiveButton(context.resources.getString(R.string.dialog_yes)) { _, _ ->
                if (state) {
                    userProfileViewModel.insertUserFavourite(
                        userId,
                        userAvatar,
                        userHtml,
                        this@GithubUserProfileActivity
                    )
                    Toast.makeText(
                        context,
                        userId + " " + context.resources.getString(R.string.favourite_added_notification),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_24)
                } else {
                    userProfileViewModel.deleteUserFavourite(userId, this@GithubUserProfileActivity)
                    Toast.makeText(
                        context,
                        userId + " " + (context.resources.getString(R.string.favourite_removed_notification)),
                        Toast.LENGTH_LONG
                    )
                        .show()
                    binding.fabAdd.setImageResource(R.drawable.ic_baseline_favorite_border_24)
                }
            }
            setNegativeButton(context.resources.getString(R.string.dialog_no))
            { dialog, _ ->
                dialog.cancel()
            }
        }

        val alertDialog = alertDialogBuilder.create()
        alertDialog.show()
    }

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        private const val prefsName = "TEMP_ID"
        private const val keyId = "key_id"
        private const val imageId = "img_id"
        private const val urlId = "url_id"
    }
}