package com.dicoding.dummyusersearch.fragment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.dummyusersearch.adapter.GithubUserAdapter
import com.dicoding.dummyusersearch.databinding.FragmentFollowingBinding
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.dicoding.dummyusersearch.viewmodel.FollowFragmentViewModel

class FollowingFragment : Fragment() {
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

        val followingViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                FollowFragmentViewModel::class.java
            )

        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        val sharedPref = requireActivity().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val id = sharedPref.getString(keyId, "null")

        binding.listGithubUser.layoutManager = layoutManager
        binding.listGithubUser.addItemDecoration(itemDecoration)

        followingViewModel.githubUserFollowArray.observe(viewLifecycleOwner, { userArray ->
            setGitHubUserFollowingData(userArray)
        })

        followingViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        followingViewModel.isToast.observe(viewLifecycleOwner, { isToast ->
            showToast(isToast, followingViewModel.toastReason.value.toString())
        })

        followingViewModel.getGitHubUserFollowingData(id.toString())
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    private fun setGitHubUserFollowingData(listGithubUserID: ArrayList<GitHubUserArray>) {
        val listReview = ArrayList<GitHubUserArray>()
        for (userID in listGithubUserID) {
            val user = GitHubUserArray(userID.login, userID.htmlUrl, userID.avatarUrl)
            listReview.add(user)
        }
        val adapter = GithubUserAdapter(listReview)
        binding.listGithubUser.adapter = adapter
    }

    private fun showToast(isToast: Boolean, toastReason: String) {
        if (!isToast) {
            Toast.makeText(context, toastReason, Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private const val prefsName = "TEMP_ID"
        private const val keyId = "key_id"
    }
}
