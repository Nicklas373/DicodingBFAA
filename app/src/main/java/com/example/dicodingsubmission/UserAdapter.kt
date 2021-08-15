package com.example.dicodingsubmission

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {
    var gitUser = arrayListOf<GitHubUser>()

    override fun getCount(): Int {
        return gitUser.size
    }

    override fun getItem(position: Int): Any {
        return gitUser[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val gitUsername = context.resources.getStringArray(R.array.git_username)[position]
        val gitName = context.resources.getStringArray(R.array.git_name)[position]
        val gitLocation = context.resources.getStringArray(R.array.git_location)[position]
        val gitCompany = context.resources.getStringArray(R.array.git_company)[position]
        val gitFollower = context.resources.getStringArray(R.array.git_followers)[position]
        val gitFollowing = context.resources.getStringArray(R.array.git_following)[position]
        val gitRepository = context.resources.getStringArray(R.array.git_repository)[position]
        val gitImage = context.resources.getStringArray(R.array.git_image)[position]

        val gitID = GitHubUser(
            gitImage,
            gitName,
            gitUsername,
            gitFollower,
            gitFollowing,
            gitCompany,
            gitLocation,
            gitRepository
        )

        var itemView = view

        if (itemView == null) {
            itemView =
                LayoutInflater.from(context).inflate(R.layout.item_github_user, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)
        val gitUser = getItem(position) as GitHubUser

        viewHolder.bind(gitUser)

        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, DetailGitUserActivity::class.java)
            intent.putExtra(DetailGitUserActivity.EXTRA_GITHUB_USER, gitID)
            context.startActivity(intent)
        }

        return itemView
    }

    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view){
        private val gitName: TextView = view.findViewById(R.id.git_username_view)
        private val gitComp: TextView = view.findViewById(R.id.git_comp_view)
        private val gitImage: ImageView = view.findViewById(R.id.git_image_view)
        private val gitFavourite: Button = view.findViewById(R.id.git_favourite)
        private val gitShare: Button = view.findViewById(R.id.git_share)

        fun bind(gitUser: GitHubUser) {
            val getImage = context.resources.getIdentifier(gitUser.gitImage, "drawable", context.packageName)

            gitName.text = gitUser.gitName
            gitComp.text = gitUser.gitComp
            gitImage.setImageResource(getImage)

            gitFavourite.setOnClickListener {
                Toast.makeText(itemView.context, "Favorite " + gitUser.gitName.toString() + " !", Toast.LENGTH_LONG).show()
            }

            gitShare.setOnClickListener{
                Toast.makeText(itemView.context, "Shared " + gitUser.gitName.toString() + " !", Toast.LENGTH_LONG).show()
            }
        }
    }
}