package com.james.submissiononefundamentalandroiddicoding

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.james.submissiononefundamentalandroiddicoding.adapter.RecyclerViewAdapter
import com.james.submissiononefundamentalandroiddicoding.databinding.FollowFragmentBinding
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem
import com.james.submissiononefundamentalandroiddicoding.viewmodel.FollowViewModel


class FollowFragment : Fragment() {
    private lateinit var followFragmentBinding: FollowFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {

        followFragmentBinding = FollowFragmentBinding.inflate(layoutInflater, container,false)
        val followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)
        followViewModel.followings.observe(viewLifecycleOwner){followings ->
            setListFollow(followings)
        }

        followViewModel.followers.observe(viewLifecycleOwner){followers ->
            setListFollow(followers)
        }
        followViewModel.isLoading.observe(viewLifecycleOwner) {
            showLoading(it)
        }
        return followFragmentBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val index = arguments?.getInt(ARG_SECTION_NUMBER, 0)
        val username = arguments?.getString(USERNAME)

        val followViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(FollowViewModel::class.java)

        if (index == 1){
            followViewModel.getFollowers(username!!)
        } else {
            followViewModel.getFollowings(username!!)
        }
    }

    private fun setListFollow(followings: List<ItemsItem>) {
        followFragmentBinding.rvItemFragment.layoutManager= LinearLayoutManager(requireActivity())
        val listUser = ArrayList<ItemsItem>()
        for (user in followings){
            val item = ItemsItem(user.login,user.avatarUrl)
            listUser.add(item)
        }
        val adapter = RecyclerViewAdapter(listUser)
        followFragmentBinding.rvItemFragment.adapter = adapter

        adapter.setOnItemClickCallback(object : RecyclerViewAdapter.OnItemClickCallback{
            override fun onItemClicked(data: ItemsItem) {
                val intentToDetail = Intent(activity, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.EXTRA_USER, data.login)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            followFragmentBinding.progressBarFragment.visibility = View.VISIBLE
        } else{
            followFragmentBinding.progressBarFragment.visibility = View.GONE
        }
    }

    companion object {
        const val ARG_SECTION_NUMBER = "section_number"
        const val USERNAME = "username"
    }
}
