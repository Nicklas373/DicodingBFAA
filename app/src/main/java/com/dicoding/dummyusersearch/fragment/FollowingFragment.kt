package com.dicoding.dummyusersearch.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dummyusersearch.adapter.GithubUserAdapter
import com.dicoding.dummyusersearch.api.ApiConfig
import com.dicoding.dummyusersearch.databinding.FragmentFollowingBinding
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowingFragment : Fragment() {
    private val listGitHubUser = ArrayList<GitHubUserArray>()
    private lateinit var _binding: FragmentFollowingBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
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
        binding.progressBar.visibility = View.VISIBLE
        val client = ApiConfig.getApiService().getUserFollowing(query)
        client.enqueue(object : Callback<List<GitHubUserArray>> {
            override fun onResponse(
                call: Call<List<GitHubUserArray>>,
                response: Response<List<GitHubUserArray>>
            ) {
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) {
                        if (responseBody.isEmpty()) {
                            listGitHubUser.clear()
                            binding.progressBar.visibility = View.GONE
                            Toast.makeText(activity, "Tidak ada daftar following!", Toast.LENGTH_LONG)
                                .show()
                        } else {
                            listGitHubUser.clear()
                            binding.progressBar.visibility = View.GONE
                            setGitHubUserFollowingData(responseBody)
                        }
                    }
                } else {
                    Toast.makeText(activity, "onFailure: ${response.message()}", Toast.LENGTH_SHORT)
                        .show()
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<List<GitHubUserArray>>, t: Throwable) {
                Toast.makeText(activity, "onFailure: ${t.message}", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "onFailure: ${t.message}")
                binding.progressBar.visibility = View.GONE
            }
        })
    }

    private fun setGitHubUserFollowingData(listGithubUserID: List<GitHubUserArray>) {
        val listReview = ArrayList<GitHubUserArray>()
        for (userID in listGithubUserID) {
            val user = GitHubUserArray(userID.login, userID.htmlUrl, userID.avatarUrl)
            listReview.add(user)
        }
        val adapter = GithubUserAdapter(listReview)
        binding.listGithubUser.adapter = adapter
    }

    companion object {
        private val TAG = FollowingFragment::class.java.simpleName
        private const val prefsName = "TEMP_ID"
        private const val keyId = "key_id"
    }
}
