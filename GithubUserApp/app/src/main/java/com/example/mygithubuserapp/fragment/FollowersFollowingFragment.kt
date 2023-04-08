package com.example.mygithubuserapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuserapp.adapter.ListUserAdapter
import com.example.mygithubuserapp.api.ItemsItem
import com.example.mygithubuserapp.databinding.FragmentFollowersFollowingBinding
import com.example.mygithubuserapp.viewModel.FollowsViewModel

class FollowersFollowingFragment : Fragment() {

    private var position: Int? = null
    private var username: String? = null
    private lateinit var binding : FragmentFollowersFollowingBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.let {
            position = it.getInt(ARG_POSITION)
            username = it.getString(ARG_USERNAME)
        }

        val followsViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            FollowsViewModel::class.java)
        followsViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }

        followsViewModel.getFollowers(username.toString())
        followsViewModel.getFollowing(username.toString())

        if (position == 1){
            followsViewModel.listFollowers.observe(viewLifecycleOwner){
                    listFollowers -> setListFollows(listFollowers)
            }

        } else {
            followsViewModel.listFollowing.observe(viewLifecycleOwner){
                    listFollowing -> setListFollows(listFollowing)
            }
        }
    }

    private fun setListFollows(listUserDetail : List<ItemsItem>){
        val adapter = ListUserAdapter(listUserDetail)
        binding.listFollows.layoutManager = LinearLayoutManager(requireActivity())
        binding.listFollows.adapter = adapter
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowersFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }


    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_POSITION = "POSITION"
        const val ARG_USERNAME = "USERNAME"
    }
}