package com.example.dicodingsubmission

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class UserAdapter internal constructor(private val context: Context) : BaseAdapter() {
    internal var GitUser = arrayListOf<GitHubUser>()

    override fun getCount(): Int {
        return GitUser.size
    }

    override fun getItem(position: Int): Any {
        return GitUser[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, viewGroup: ViewGroup): View {
        val GitUsername = context.getResources().getStringArray(R.array.git_username)[position]
        val GitName = context.getResources().getStringArray(R.array.git_name)[position]
        val GitLocation = context.getResources().getStringArray(R.array.git_location)[position]
        val GitCompany = context.getResources().getStringArray(R.array.git_company)[position]
        val GitFollower = context.getResources().getStringArray(R.array.git_followers)[position]
        val GitFollowing = context.getResources().getStringArray(R.array.git_following)[position]
        val GitRepository = context.getResources().getStringArray(R.array.git_repository)[position]
        val GitImage = context.getResources().getStringArray(R.array.git_image)[position]

        val GitID = GitHubUser(
            GitImage,
            GitName,
            GitUsername,
            GitFollower,
            GitFollowing,
            GitCompany,
            GitLocation,
            GitRepository
        )

        var itemView = view

        if (itemView == null) {
            itemView =
                LayoutInflater.from(context).inflate(R.layout.item_github_user, viewGroup, false)
        }

        val viewHolder = ViewHolder(itemView as View)
        val gituser = getItem(position) as GitHubUser

        viewHolder.bind(gituser)

        viewHolder.itemView.setOnClickListener {
            val intent = Intent(context, DetailGitUserActivity::class.java)
            intent.putExtra(DetailGitUserActivity.EXTRA_GITHUB_USER, GitID)
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

        fun bind(gituser: GitHubUser) {
            val getImage = context.resources.getIdentifier(gituser.gitImage, "drawable", context.packageName)

            gitName.text = gituser.gitName
            gitComp.text = gituser.gitComp
            gitImage.setImageResource(getImage)

            gitFavourite.setOnClickListener {
                Toast.makeText(itemView.context, "Favorite " + gituser.gitName.toString() + " !", Toast.LENGTH_LONG).show()
            }

            gitShare.setOnClickListener{
                Toast.makeText(itemView.context, "Shared " + gituser.gitName.toString() + " !", Toast.LENGTH_LONG).show()
            }
        }
    }
}
