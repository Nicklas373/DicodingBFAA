package com.dicoding.dummyusersearch.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dummyusersearch.activity.GithubUserProfileActivity
import com.dicoding.dummyusersearch.activity.GithubUserProfileActivity.Companion.EXTRA_GITHUB_USER
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class GithubUserAdapter(private val listUser: ArrayList<GitHubUserArray>) : RecyclerView.Adapter<GithubUserAdapter.ListViewHolder>() {

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var gitName: TextView = itemView.findViewById(R.id.git_username_view)
        var gitId: TextView = itemView.findViewById(R.id.git_url_view)
        var gitImage: CircleImageView = itemView.findViewById(R.id.git_image_view)
        var gitFavourite: Button = itemView.findViewById(R.id.git_favourite)
        var gitShare: Button = itemView.findViewById(R.id.git_share)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_github_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val (git_username, git_id, git_image) = listUser[position]
        holder.gitName.text = git_username
        holder.gitId.text = git_id
        Picasso.get().load(git_image).into(holder.gitImage)
        holder.gitFavourite.setOnClickListener {
            Toast.makeText(holder.itemView.context, "Favourite $git_username !", Toast.LENGTH_LONG).show()
        }
        holder.gitShare.setOnClickListener{
            Toast.makeText(holder.itemView.context, "Shared $git_username !", Toast.LENGTH_LONG).show()
        }
        holder.itemView.setOnClickListener{
            val intent = Intent(holder.itemView.context, GithubUserProfileActivity::class.java)
            intent.putExtra(EXTRA_GITHUB_USER, git_username)
            holder.itemView.context.startActivity(intent)
        }
    }
}