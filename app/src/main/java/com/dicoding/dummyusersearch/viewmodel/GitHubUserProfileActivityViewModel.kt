package com.dicoding.dummyusersearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.userdata.GitHubUserJSON
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class GitHubUserProfileActivityViewModel : ViewModel() {

    private val _githubUserProfileJSON = MutableLiveData<GitHubUserJSON>()
    val githubUserProfileJSON: LiveData<GitHubUserJSON> = _githubUserProfileJSON

    private val _isToast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = _isToast

    private val _toastReason = MutableLiveData<String>()
    val toastReason: LiveData<String> = _toastReason

    fun getGitHubUserData(query: String) {
        val client = ApiConfig.getApiService().getUserDetail(query)
        client.enqueue(object : Callback<GitHubUserJSON> {
            override fun onResponse(
                call: Call<GitHubUserJSON>,
                response: Response<GitHubUserJSON>
            ) {
                if (response.isSuccessful) {
                    if (response.body() != null) {
                        _githubUserProfileJSON.value = response.body()
                    }
                } else {
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubUserJSON>, t: Throwable) {
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message}"
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object {
        private val TAG = GitHubUserProfileActivityViewModel::class.java.simpleName
    }
}