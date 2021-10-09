package com.dicoding.dummyusersearch.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.viewpager2.widget.ViewPager2
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.userdata.GitHubUserJSON
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GithubUserProfileActivity : AppCompatActivity() {

    private val prefsName = "TEMP_ID"
    private val keyId = "key_id"
    private val themeId = "theme_id"

    companion object {
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )

        private val TAG = GithubUserProfileActivity::class.java.simpleName
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_github_user_profile)

        val sharedPref = this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val gitUser = sharedPref.getString(keyId, "null")
        val sectionsPagerAdapter = SectionPagerActivity(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        val tabs: TabLayout = findViewById(R.id.tabs)

        if (gitUser != null) {
            getGitHubUserData(gitUser)
            title = gitUser
        } else {
            getGitHubUserData("Null")
        }

        initTheme()

        viewPager.adapter = sectionsPagerAdapter
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        supportActionBar?.elevation = 0f
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
                sharedPrefTheme("true")
            }
            R.id.action_light_mode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                sharedPrefTheme("false")
            }
        }
    }

    private fun sharedPrefTheme(theme: String) {
        val sharedPref = this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(themeId, theme)
        editor.apply()
    }

    private fun initTheme(){
        val sharedPref = this.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val theme = sharedPref.getString(themeId, "false")
        if (theme.toString() == "true") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            sharedPrefTheme("true")
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            sharedPrefTheme("false")
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

                        if (responseBody.name.isNullOrBlank()) {
                            gitNameText.text = "-"
                        } else {
                            gitNameText.text = responseBody.name
                        }

                        if (responseBody.email.isNullOrBlank()) {
                            gitEmailText.text = "-"
                        } else {
                            gitEmailText.text = responseBody.email
                        }

                        if (responseBody.location.isNullOrBlank()) {
                            gitLocationText.text = "-"
                        } else {
                            gitLocationText.text = responseBody.location
                        }

                        if (responseBody.company.isNullOrBlank()) {
                            gitCompanyText.text = "-"
                        } else {
                            gitCompanyText.text = responseBody.company
                        }

                        gitJoinText.text = splitDate
                        gitFollowersText.text = responseBody.followers
                        gitFollowingText.text = responseBody.following
                        gitRepositoryText.text = responseBody.public_repos
                        Picasso.get().load(responseBody.avatarUrl).into(gitImageDraw)
                    }
                } else {
                    Toast.makeText(this@GithubUserProfileActivity, "onFailure: ${response.message()}", Toast.LENGTH_SHORT).show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GitHubUserJSON>, t: Throwable) {
                Toast.makeText(this@GithubUserProfileActivity, "onFailure: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}