package com.example.dicodingsubmission

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var gitAdapter: UserAdapter
    private lateinit var gitListImage: Array<String>
    private lateinit var gitListName: Array<String>
    private lateinit var gitListComp: Array<String>
    private lateinit var gitListId: Array<String>
    private lateinit var gitListFollower: Array<String>
    private lateinit var gitListFollowing: Array<String>
    private lateinit var gitListLocation: Array<String>
    private lateinit var gitListRepository: Array<String>
    private var gitUserArray = arrayListOf<GitHubUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(findViewById(R.id.activity_main_toolbar))

        supportActionBar?.title = resources.getString(R.string.app_name)

        val listView: ListView = findViewById(R.id.main_git_list)
        gitAdapter = UserAdapter(this)
        listView.adapter = gitAdapter

        prepare()
        addItem()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
        }
    }

    private fun prepare() {
        gitListImage = resources.getStringArray(R.array.git_image)
        gitListName = resources.getStringArray(R.array.git_name)
        gitListId = resources.getStringArray(R.array.git_username)
        gitListFollower = resources.getStringArray(R.array.git_followers)
        gitListFollowing = resources.getStringArray(R.array.git_following)
        gitListComp = resources.getStringArray(R.array.git_company)
        gitListLocation = resources.getStringArray(R.array.git_location)
        gitListRepository = resources.getStringArray(R.array.git_repository)
    }

    private fun addItem() {

        for (position in gitListImage.indices) {
            val gitUser = GitHubUser(
                gitListImage[position],
                gitListName[position],
                gitListId[position],
                gitListFollower[position],
                gitListFollowing[position],
                gitListComp[position],
                gitListLocation[position],
                gitListRepository[position]
            )
            gitUserArray.add(gitUser)
        }

        gitAdapter.gitUser = gitUserArray
    }
}