package com.dicoding.dummyusersearch.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dummyusersearch.adapter.GithubUserFollowersAdapter
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.databinding.FragmentFollowingBinding
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {
    private val listGitHubUser = ArrayList<GitHubUserArray>()
    private var _binding: FragmentFollowingBinding? = null
    private val binding get() = _binding!!
    private val prefsName = "TEMP_ID"
    private val keyId = "key_id"

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        val sharedPref = requireActivity().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val id = sharedPref.getString(keyId, "null")

        binding.listGithubUser.layoutManager = layoutManager
        binding.listGithubUser.addItemDecoration(itemDecoration)


        getGitHubUserFollowingData(id.toString())
    }

    private fun getGitHubUserFollowingData(query: String) {
        val client = ApiConfig.getApiService().getUserFollowers(query)
        client.enqueue(object : Callback<List<GitHubUserArray>> {
            override fun onResponse(
                call: Call<List<GitHubUserArray>>,
                response: Response<List<GitHubUserArray>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        listGitHubUser.clear()
                        setGitHubUserFollowingData(responseBody)
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<GitHubUserArray>>, t: Throwable) {
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    private fun setGitHubUserFollowingData(listGithubUserID: List<GitHubUserArray>) {
        val listReview = ArrayList<GitHubUserArray>()
        for (userID in listGithubUserID) {
            val user = GitHubUserArray(userID.login, userID.htmlUrl, userID.avatarUrl)
            listReview.add(user)
        }
        val adapter = GithubUserFollowersAdapter(listReview)
        binding.listGithubUser.adapter = adapter
    }
}
