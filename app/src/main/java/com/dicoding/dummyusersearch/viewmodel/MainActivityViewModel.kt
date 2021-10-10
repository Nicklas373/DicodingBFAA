package com.dicoding.dummyusersearch.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.dicoding.dummyusersearch.userdata.GitHubUserResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivityViewModel : ViewModel() {

    private val _githubUserArray = MutableLiveData<ArrayList<GitHubUserArray>>()
    val githubUserArray: LiveData<ArrayList<GitHubUserArray>> = _githubUserArray

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _isToast = MutableLiveData<Boolean>()
    val isToast: LiveData<Boolean> = _isToast

    private val _toastReason = MutableLiveData<String>()
    val toastReason: LiveData<String> = _toastReason

    fun findGitHubUserID(query: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUserQuery(query)
        client.enqueue(object : Callback<GitHubUserResponse> {
            override fun onResponse(
                call: Call<GitHubUserResponse>,
                response: Response<GitHubUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    if (response.body()?.items?.isEmpty() == true) {
                        _isToast.value = false
                        _toastReason.value = "Data Pencarian Kosong!"
                    } else {
                        _isToast.value = true
                        _githubUserArray.value = response.body()?.items
                    }
                } else {
                    _isToast.value = false
                    _toastReason.value = "onFailure: ${response.message()}"
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<GitHubUserResponse>, t: Throwable) {
                _isLoading.value = false
                _isToast.value = false
                _toastReason.value = "onFailure: ${t.message.toString()}"
                Log.e(TAG, "onFailure: ${t.message.toString()}")
            }
        })
    }

    companion object {
        private const val TAG = "MainViewModel"
    }
}