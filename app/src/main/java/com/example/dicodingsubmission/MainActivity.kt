package com.example.dicodingsubmission

import android.os.Bundle
import android.widget.AdapterView
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private lateinit var GitAdapter: UserAdapter
    private lateinit var GitListImage: Array<String>
    private lateinit var GitListName: Array<String>
    private lateinit var GitListComp: Array<String>
    private lateinit var GitListId: Array<String>
    private lateinit var GitListFollower: Array<String>
    private lateinit var GitListFollowing: Array<String>
    private lateinit var GitListLocation: Array<String>
    private lateinit var GitListRepository: Array<String>
    private var GitUser = arrayListOf<GitHubUser>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val listView: ListView = findViewById(R.id.main_git_list)
        GitAdapter = UserAdapter(this)
        listView.adapter = GitAdapter

        prepare()
        addItem()

        listView.onItemClickListener = AdapterView.OnItemClickListener { _, _, _, _ ->
        }
    }

    private fun prepare() {
        GitListImage = resources.getStringArray(R.array.git_image)
        GitListName = resources.getStringArray(R.array.git_name)
        GitListId = resources.getStringArray(R.array.git_username)
        GitListFollower = resources.getStringArray(R.array.git_followers)
        GitListFollowing = resources.getStringArray(R.array.git_following)
        GitListComp = resources.getStringArray(R.array.git_company)
        GitListLocation = resources.getStringArray(R.array.git_location)
        GitListRepository = resources.getStringArray(R.array.git_repository)
    }

    private fun addItem() {

        for (position in GitListImage.indices) {
            val gituser = GitHubUser(
                GitListImage[position],
                GitListName[position],
                GitListId[position],
                GitListFollower[position],
                GitListFollowing[position],
                GitListComp[position],
                GitListLocation[position],
                GitListRepository[position]
            )
            GitUser.add(gituser)
        }

        GitAdapter.GitUser = GitUser
    }
}