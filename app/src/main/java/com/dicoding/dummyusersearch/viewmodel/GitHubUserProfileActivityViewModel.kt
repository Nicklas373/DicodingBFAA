package com.dicoding.dummyusersearch.viewmodel

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.database.FavouriteDB
import com.dicoding.dummyusersearch.database.FavouriteRoomDB
import com.dicoding.dummyusersearch.userdata.GitHubUserJSON
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GitHubUserProfileActivityViewModel : ViewModel() {

    private val _githubUserProfileJSON = MutableLiveData<GitHubUserJSON>()
    val githubUserProfileJSON: LiveData<GitHubUserJSON> = _githubUserProfileJSON

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isToast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = _isToast

    private val _toastReason = MutableLiveData<String>()
    val toastReason: LiveData<String> = _toastReason

    fun getGitHubUserData(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserDetail(query)
        client.enqueue(object : Callback<GitHubUserJSON> {
            override fun onResponse(
                call: Call<GitHubUserJSON>,
                response: Response<GitHubUserJSON>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _isLoading.value = false
                        _githubUserProfileJSON.value = response.body()
                    }
                } else {
                    _isLoading.value = true
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubUserJSON>, t: Throwable) {
                _isLoading.value = true
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun insertUserFavourite(
        userId: String,
        userAvatar: String,
        userHtml: String,
        context: Context
    ) {
        _isLoading.value = true
        val githubUserDBFavourite = FavouriteRoomDB.getDatabase(context).favouriteDao()
        val inputFavData = FavouriteDB(login = userId, avatarUrl = userAvatar, htmlUrl = userHtml)
        if (inputFavData.id.toString().isEmpty()) {
            _isLoading.value = true
        } else {
            githubUserDBFavourite.insert(inputFavData)
        }
        _isLoading.value = false
    }

    fun deleteUserFavourite(userId: String, context: Context) {
        _isLoading.value = true
        val githubUserDBFavourite = FavouriteRoomDB.getDatabase(context).favouriteDao()
        githubUserDBFavourite.delete(userId)
        _isLoading.value = false
    }

    companion object {
        private val TAG = GitHubUserProfileActivityViewModel::class.java.simpleName
    }
}