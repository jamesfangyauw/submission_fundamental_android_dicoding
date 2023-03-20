package com.james.submissiononefundamentalandroiddicoding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.james.submissiononefundamentalandroiddicoding.adapter.RecyclerViewAdapter
import com.james.submissiononefundamentalandroiddicoding.databinding.ActivityFavoriteBinding
import com.james.submissiononefundamentalandroiddicoding.db.UserEntity
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem
import com.james.submissiononefundamentalandroiddicoding.viewmodel.FavoriteViewModel
import com.james.submissiononefundamentalandroiddicoding.viewmodel.ViewModelFactory

class FavoriteActivity : AppCompatActivity() {

    private var _activityFavoriteBinding: ActivityFavoriteBinding? = null
    private val binding get() = _activityFavoriteBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _activityFavoriteBinding = ActivityFavoriteBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        val favoriteViewModel by viewModels<FavoriteViewModel>() {
            ViewModelFactory.getInstance(application)
        }

        favoriteViewModel.getAllUser().observe(this) { userList ->
            setListUser(userList)
        }
    }

    private fun setListUser(users: List<UserEntity>) {
        binding?.rvItemFavorite?.layoutManager = LinearLayoutManager(this)
        val listUser = ArrayList<ItemsItem>()
        for (user in users) {
            val item = ItemsItem(user.username, user.avatarUrl)
            listUser.add(item)
        }
        val adapter = RecyclerViewAdapter(listUser)
        binding?.rvItemFavorite?.adapter = adapter

        adapter.setOnItemClickCallback(object : RecyclerViewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val user = UserEntity(data.login, data.avatarUrl) as UserEntity
                val intentToDetail = Intent(this@FavoriteActivity, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.EXTRA_USER, user)
                startActivity(intentToDetail)
            }
        })

        val actionBar = supportActionBar
        actionBar!!.title = getString(R.string.favorite_users)
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    override fun onDestroy() {
        super.onDestroy()
        _activityFavoriteBinding = null
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}