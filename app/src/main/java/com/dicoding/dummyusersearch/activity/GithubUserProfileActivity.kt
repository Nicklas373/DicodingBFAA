package com.dicoding.dummyusersearch.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.database.FavouriteDB
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

        userProfileViewModel.isToast.observe(this, { isToast ->
            showToast(isToast, userProfileViewModel.toastReason.value.toString())
        })

        if (gitUserSp != null) {
            userProfileViewModel.getGitHubUserData(gitUserSp)
            title = gitUserSp
        } else {
            userProfileViewModel.getGitHubUserData("Null")
        }

        initTheme()

        viewPager.adapter = sectionsPagerAdapter
        viewPager.offscreenPageLimit = 2
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f

        binding.fabAdd.setOnClickListener {
            val database =
                FavouriteRoomDB.getDatabase(this).favouriteDao()
            val exist = database.checkUserFavourites(gitUserSp.toString())

            if (!exist) {
                val title = "Favourite"
                val message =
                    "${gitUserSp.toString()} tidak ada di daftar favourite! Apakah anda ingin menambahkan ke daftar favourite ?"
                val alertDialogBuilder = AlertDialog.Builder(this)
                with(alertDialogBuilder) {
                    setTitle(title)
                    setMessage(message)
                    setCancelable(false)
                    setPositiveButton(context.resources.getString(R.string.dialog_yes)) { _, _ ->
                        val githubUserDBFavourite =
                            FavouriteRoomDB.getDatabase(this@GithubUserProfileActivity)
                                .favouriteDao()
                        val inputFavData = FavouriteDB(
                            login = gitUserSp.toString(),
                            avatarUrl = gitImageSp.toString(),
                            htmlUrl = gitHtmlSp.toString()
                        )
                        githubUserDBFavourite.insert(inputFavData)
                        Toast.makeText(
                            context,
                            "${gitUserSp.toString()} sudah di tambahkan ke daftar favourite !",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    setNegativeButton(context.resources.getString(R.string.dialog_no))
                    { dialog, _ ->
                        dialog.cancel()
                    }
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            } else {
                val title = "Favourite"
                val message =
                    "${gitUserSp.toString()} sudah ada di daftar favourite! Apakah anda ingin menghapus dari daftar favourite ?"
                val alertDialogBuilder = AlertDialog.Builder(this)
                with(alertDialogBuilder) {
                    setTitle(title)
                    setMessage(message)
                    setCancelable(false)
                    setPositiveButton(context.resources.getString(R.string.dialog_yes)) { _, _ ->
                        val githubUserDBFavourite =
                            FavouriteRoomDB.getDatabase(context.applicationContext)
                                .favouriteDao()
                        githubUserDBFavourite.delete(gitUserSp.toString())
                        Toast.makeText(
                            context,
                            "${gitUserSp.toString()} sudah di hapus dari favourite",
                            Toast.LENGTH_LONG
                        )
                            .show()
                    }
                    setNegativeButton(context.resources.getString(R.string.dialog_no))
                    { dialog, _ ->
                        dialog.cancel()
                    }
                }
                val alertDialog = alertDialogBuilder.create()
                alertDialog.show()
            }
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
            R.id.action_dark_mode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                sharedPrefTheme(true)
            }
            R.id.action_light_mode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefTheme(false)
            }
            R.id.action_favourite -> {
                val intent = Intent(this, FavouriteActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun sharedPrefTheme(theme: Boolean) {
        val sharedPref = this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putBoolean(themeId, theme)
        editor.apply()
    }

    private fun initTheme() {
        val sharedPref = this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val theme: Boolean = sharedPref.getBoolean(themeId, false)
        if (theme) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPrefTheme(true)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPrefTheme(false)
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

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        private const val prefsName = "TEMP_ID"
        private const val keyId = "key_id"
        private const val themeId = "theme_id"
        private const val imageId = "img_id"
        private const val urlId = "url_id"
    }
}