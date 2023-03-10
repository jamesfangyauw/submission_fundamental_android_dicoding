package com.james.submissiononefundamentalandroiddicoding

import android.app.SearchManager
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.james.submissiononefundamentalandroiddicoding.adapter.RecyclerViewAdapter
import com.james.submissiononefundamentalandroiddicoding.databinding.ActivityMainBinding
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem
import com.james.submissiononefundamentalandroiddicoding.viewmodel.MainViewModel

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        )[MainViewModel::class.java]

        mainViewModel.isLoading.observe(this){isLoading ->
            showLoading(isLoading)
        }

        mainViewModel.listUser.observe(this){listUser ->
            setListUser(listUser)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val mainViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(MainViewModel::class.java)

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUser(query)
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading){
            activityMainBinding.progressBar.visibility = View.VISIBLE
        } else{
            activityMainBinding.progressBar.visibility = View.GONE
        }
    }

    private fun setListUser(users: List<ItemsItem>) {
        activityMainBinding.rvItem.layoutManager=LinearLayoutManager(this)
        val listUser = ArrayList<ItemsItem>()
        for (user in users){
            val item = ItemsItem(user.login,user.avatarUrl)
            listUser.add(item)
        }
        val adapter = RecyclerViewAdapter(listUser)
        activityMainBinding.rvItem.adapter = adapter
    }
}