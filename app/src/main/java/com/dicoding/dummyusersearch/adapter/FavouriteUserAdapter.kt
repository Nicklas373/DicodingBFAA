package com.dicoding.dummyusersearch.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dummyusersearch.activity.GithubUserProfileActivity
import com.dicoding.dummyusersearch.database.FavouriteDB
import com.dicoding.dummyusersearch.databinding.ItemGithubUserBinding
import com.squareup.picasso.Picasso

class FavouriteUserAdapter(private val listUser: List<FavouriteDB>) :
    RecyclerView.Adapter<FavouriteUserAdapter.ViewHolder>() {

    private lateinit var sharedPref: SharedPreferences

    override fun getItemCount(): Int = listUser.size

    inner class ViewHolder(private val binding: ItemGithubUserBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(fav: FavouriteDB) {
            sharedPref = itemView.context.getSharedPreferences(prefsName, Context.MODE_PRIVATE)
            binding.gitUsernameView.text = fav.login
            binding.gitUrlView.text = fav.avatarUrl
            Picasso.get().load(fav.htmlUrl).into(binding.gitImageView)
            binding.gitFavourite.setOnClickListener {
                Toast.makeText(
                    itemView.context,
                    "Favourite ${fav.login} !",
                    Toast.LENGTH_LONG
                )
                    .show()
            }
            binding.gitShare.setOnClickListener {
                val sendIntent: Intent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, fav.login)
                    type = "text/html"
                }
                itemView.context.startActivity(sendIntent)
            }
            itemView.setOnClickListener {
                val intent = Intent(itemView.context, GithubUserProfileActivity::class.java)
                fav.login?.let { it1 ->
                    fav.htmlUrl?.let { it2 ->
                        fav.avatarUrl?.let { it3 ->
                            sharedPrefID(
                                it1,
                                it2, it3
                            )
                        }
                    }
                }
                itemView.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val view =
            ItemGithubUserBinding.inflate(LayoutInflater.from(viewGroup.context), viewGroup, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(listUser[position])
    }

    private fun sharedPrefID(id: String, image: String, html: String) {
        val editor: SharedPreferences.Editor = sharedPref.edit()
        editor.putString(keyId, id)
        editor.putString(imageId, image)
        editor.putString(urlId, html)
        editor.apply()
    }

    companion object {
        private const val prefsName = "TEMP_ID"
        private const val keyId = "key_id"
        private const val imageId = "img_id"
        private const val urlId = "url_id"
    }
}