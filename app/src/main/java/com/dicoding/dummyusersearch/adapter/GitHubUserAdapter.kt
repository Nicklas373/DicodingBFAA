package com.dicoding.dummyusersearch.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.activity.GithubUserProfileActivity
import com.dicoding.dummyusersearch.databinding.ItemGithubUserBinding
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.squareup.picasso.Picasso

class GithubUserAdapter(private val listUser: ArrayList<GitHubUserArray>) :
    RecyclerView.Adapter<GithubUserAdapter.ListViewHolder>() {

    private lateinit var sharedPref: SharedPreferences

    class ListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val binding = ItemGithubUserBinding.bind(itemView)
    }

    override fun getItemCount(): Int = listUser.size

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ListViewHolder {
        val view: View = LayoutInflater.from(viewGroup.context)
            .inflate(R.layout.item_github_user, viewGroup, false)
        return ListViewHolder(view)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        with(holder) {
            val (git_username, git_id, git_image) = listUser[position]
            sharedPref =
                holder.itemView.context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            binding.gitUsernameView.text = git_username
            binding.gitUrlView.text = git_id
            Picasso.get().load(git_image).into(binding.gitImageView)
            binding.gitFavourite.setOnClickListener {
                Toast.makeText(
                    holder.itemView.context,
                    "Favourite $git_username !",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            binding.gitShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, git_id)
                    type = "text/html"
                }
                holder.itemView.context.startActivity(sendIntent)
            }
            holder.itemView.setOnClickListener {
                val intent = Intent(holder.itemView.context, GithubUserProfileActivity::class.java)
                sharedPrefID(git_username)
                holder.itemView.context.startActivity(intent)
            }
        }
    }

    private fun sharedPrefID(id: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(keyId, id)
        editor.apply()
    }

    companion object {
        private const val prefsName = "TEMP_ID"
        private const val keyId = "key_id"
    }
}