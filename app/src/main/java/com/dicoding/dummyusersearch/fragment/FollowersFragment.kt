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
import com.dicoding.dummyusersearch.databinding.FragmentFollowersBinding
import com.dicoding.dummyusersearch.userdata.GitHubUserArray
import com.dicoding.dummyusersearch.viewmodel.FollowersFragmentViewModel

class FollowersFragment : Fragment() {
    private lateinit var _binding: FragmentFollowersBinding
    private val binding get() = _binding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFollowersBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val followersViewModel =
            ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
                FollowersFragmentViewModel::class.java
            )

        val layoutManager = LinearLayoutManager(context)
        val itemDecoration = DividerItemDecoration(context, layoutManager.orientation)
        val sharedPref = requireActivity().getSharedPreferences(prefsName, Context.MODE_PRIVATE)
        val id = sharedPref.getString(keyId, "null")

        binding.listGithubUser.layoutManager = layoutManager
        binding.listGithubUser.addItemDecoration(itemDecoration)

        followersViewModel.githubUserArray.observe(viewLifecycleOwner, { userArray ->
            setGitHubUserFollowersData(userArray)
        })

        followersViewModel.isLoading.observe(viewLifecycleOwner, {
            showLoading(it)
        })

        followersViewModel.isToast.observe(viewLifecycleOwner, { isToast ->
            showToast(isToast, followersViewModel.toastReason.value.toString())
        })

        followersViewModel.getGitHubUserFollowersData(id.toString())
    }

    private fun setGitHubUserFollowersData(listGithubUserID: ArrayList<GitHubUserArray>) {
        val listReview = ArrayList<GitHubUserArray>()
        for (userID in listGithubUserID) {
            val user = GitHubUserArray(userID.login, userID.htmlUrl, userID.avatarUrl)
            listReview.add(user)
        }
        val adapter = GithubUserAdapter(listReview)
        _binding.listGithubUser.adapter = adapter
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
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