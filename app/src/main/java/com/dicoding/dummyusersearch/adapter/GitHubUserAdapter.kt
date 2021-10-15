package com.dicoding.dummyusersearch.adapter

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.dummyusersearch.R
import com.dicoding.dummyusersearch.activity.GithubUserProfileActivity
import com.dicoding.dummyusersearch.database.FavouriteDB
import com.dicoding.dummyusersearch.database.FavouriteRoomDB
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
                val database =
                    FavouriteRoomDB.getDatabase(itemView.context.applicationContext).favouriteDao()
                val exist = database.checkUserFavourites(binding.gitUsernameView.text.toString())
                if (!exist) {
                    val title = "Favourite"
                    val message =
                        "${binding.gitUsernameView.text} tidak ada di daftar favourite! Apakah anda ingin menambahkan ke daftar favourite ?"
                    val alertDialogBuilder = AlertDialog.Builder(itemView.context)
                    with(alertDialogBuilder) {
                        setTitle(title)
                        setMessage(message)
                        setCancelable(false)
                        setPositiveButton(context.resources.getString(R.string.dialog_yes)) { _, _ ->
                            val githubUserDBFavourite =
                                FavouriteRoomDB.getDatabase(context.applicationContext)
                                    .favouriteDao()
                            val inputFavData = FavouriteDB(
                                login = binding.gitUsernameView.text.toString(),
                                avatarUrl = git_image,
                                htmlUrl = binding.gitUrlView.text.toString()
                            )
                            githubUserDBFavourite.insert(inputFavData)
                            Toast.makeText(
                                context,
                                "${binding.gitUsernameView.text} sudah di tambahkan ke daftar favourite !",
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
                        "${binding.gitUsernameView.text} sudah ada di daftar favourite! Apakah anda ingin menghapus dari daftar favourite ?"
                    val alertDialogBuilder = AlertDialog.Builder(itemView.context)
                    with(alertDialogBuilder) {
                        setTitle(title)
                        setMessage(message)
                        setCancelable(false)
                        setPositiveButton(context.resources.getString(R.string.dialog_yes)) { _, _ ->
                            val githubUserDBFavourite =
                                FavouriteRoomDB.getDatabase(context.applicationContext)
                                    .favouriteDao()
                            githubUserDBFavourite.delete(binding.gitUsernameView.text.toString())
                            Toast.makeText(
                                context,
                                "${binding.gitUsernameView.text} sudah di hapus dari favourite",
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
                sharedPrefID(git_username, git_image, git_id)
                holder.itemView.context.startActivity(intent)
            }
        }
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