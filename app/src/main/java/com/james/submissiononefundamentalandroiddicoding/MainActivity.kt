package com.james.submissiononefundamentalandroiddicoding

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.james.submissiononefundamentalandroiddicoding.adapter.RecyclerViewAdapter
import com.james.submissiononefundamentalandroiddicoding.databinding.ActivityMainBinding
import com.james.submissiononefundamentalandroiddicoding.db.UserEntity
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem
import com.james.submissiononefundamentalandroiddicoding.viewmodel.MainViewModel
import com.james.submissiononefundamentalandroiddicoding.viewmodel.SettingViewModel
import com.james.submissiononefundamentalandroiddicoding.viewmodel.SettingViewModelFactory
import com.james.submissiononefundamentalandroiddicoding.viewmodel.ViewModelFactory

class MainActivity : AppCompatActivity() {

    private lateinit var activityMainBinding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityMainBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(activityMainBinding.root)

//        val mainViewModel = ViewModelProvider(
//            this,
//            ViewModelProvider.NewInstanceFactory()
//        )[MainViewModel::class.java]

//        mainViewModel.isLoading.observe(this) { isLoading ->
//            showLoading(isLoading)
//        }
//
//        mainViewModel.listUser.observe(this) { listUser ->
//            setListUser(listUser)
//        }


        val pref = SettingPreferences.getInstance(dataStore)
        val settingViewModel =
            ViewModelProvider(this, SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(
            this
        ) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.menu, menu)
        val mainViewModel by viewModels<MainViewModel> { ViewModelFactory.getInstance(application) }

        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String): Boolean {
                mainViewModel.searchUser(query).observe(this@MainActivity) { result ->
                    if (result != null) {
                        when (result) {
                            is Result.Loading -> activityMainBinding.progressBarActivtiy.visibility =
                                View.VISIBLE
                            is Result.Success -> {
                                activityMainBinding.progressBarActivtiy.visibility = View.GONE
                                setListUser(result.data)
                            }
                            is Result.Error -> {
                                activityMainBinding.progressBarActivtiy.visibility = View.GONE
                                Toast.makeText(
                                    this@MainActivity,
                                    "Terjadi kesalahan" + result.error,
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String): Boolean {
                return false
            }
        })
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.favorite -> {
                val intentToFavorite = Intent(this@MainActivity, FavoriteActivity::class.java)
                startActivity(intentToFavorite)
            }
            R.id.setting -> {
                val intentToSetting = Intent(this@MainActivity, SettingActivity::class.java)
                startActivity(intentToSetting)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            activityMainBinding.progressBarActivtiy.visibility = View.VISIBLE
        } else {
            activityMainBinding.progressBarActivtiy.visibility = View.GONE
        }
    }

    private fun setListUser(users: List<ItemsItem>) {
//        if (users.isEmpty()){
//            Toast.makeText(this, "Data not found, use other keyword", Toast.LENGTH_SHORT).show()
//        }
        activityMainBinding.rvItem.layoutManager = LinearLayoutManager(this)
        val listUser = ArrayList<ItemsItem>()
        for (user in users) {
            val item = ItemsItem(user.login, user.avatarUrl)
            listUser.add(item)
        }
        val adapter = RecyclerViewAdapter(listUser)
        activityMainBinding.rvItem.adapter = adapter

        adapter.setOnItemClickCallback(object : RecyclerViewAdapter.OnItemClickCallback {
            override fun onItemClicked(data: ItemsItem) {
                val user = UserEntity(data.login, data.avatarUrl)
                val intentToDetail = Intent(this@MainActivity, DetailActivity::class.java)
                intentToDetail.putExtra(DetailActivity.EXTRA_USER, user)
                startActivity(intentToDetail)
            }
        })
    }
}