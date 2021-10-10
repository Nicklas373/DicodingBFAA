package com.dicoding.dummyusersearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowersFragmentViewModel : ViewModel() {
    private val _githubUserArray = MutableLiveData<ArrayList<GitHubUserArray>>()
    val githubUserArray: LiveData<ArrayList<GitHubUserArray>> = _githubUserArray

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isToast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = _isToast

    private val _toastReason = MutableLiveData<String>()
    val toastReason: LiveData<String> = _toastReason

    fun getGitHubUserFollowersData(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserFollowers(query)
        client.enqueue(object : Callback<ArrayList<GitHubUserArray>> {
            override fun onResponse(
                call: Call<ArrayList<GitHubUserArray>>,
                response: Response<ArrayList<GitHubUserArray>>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.isEmpty() == true) {
                        _isToast.value = false
                        _toastReason.value = "Tidak ada daftar followers!!"
                        _githubUserArray.value = response.body()
                    } else {
                        _isToast.value = true
                        _githubUserArray.value = response.body()
                    }
                } else {
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ArrayList<GitHubUserArray>>, t: Throwable) {
                _isLoading.value = false
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message.toString()}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "FollowersViewModel"
    }

}