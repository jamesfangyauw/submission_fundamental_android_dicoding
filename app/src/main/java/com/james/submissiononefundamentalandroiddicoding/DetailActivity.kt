package com.james.submissiononefundamentalandroiddicoding

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.StringRes
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.james.submissiononefundamentalandroiddicoding.adapter.SectionsPagerAdapter
import com.james.submissiononefundamentalandroiddicoding.databinding.ActivityDetailBinding
import com.james.submissiononefundamentalandroiddicoding.model.DetailUserResponse
import com.james.submissiononefundamentalandroiddicoding.viewmodel.DetailViewModel

class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding: ActivityDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        val login = intent.getStringExtra(EXTRA_USER)

        val detailViewModel = ViewModelProvider(
            this,
            ViewModelProvider.NewInstanceFactory()
        ).get(DetailViewModel::class.java)

        detailViewModel.getDetailUser(login!!)

        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        detailViewModel.detailUser.observe(this) { detailUser ->
            setDetailUser(detailUser)
        }

        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        sectionsPagerAdapter.username = login
    }

    private fun setDetailUser(detailUser: DetailUserResponse) {
        activityDetailBinding.tvUsernameDetail.text = detailUser.login
        activityDetailBinding.tvNameDetail.text = detailUser.name
        activityDetailBinding.tvFollowers.text =
            detailUser.followers.toString() + getString(R.string.followers)
        activityDetailBinding.tvFollowing.text =
            detailUser.following.toString() + getString(R.string.followings)
        Glide.with(this@DetailActivity).load(detailUser.avatarUrl).circleCrop()
            .into(activityDetailBinding.ivPhotoDetail)

        val actionBar = supportActionBar
        actionBar!!.title= "${detailUser?.name}"
        actionBar.setDisplayHomeAsUpEnabled(true)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            activityDetailBinding.progressBarDetail.visibility = View.VISIBLE
        } else {
            activityDetailBinding.progressBarDetail.visibility = View.GONE
        }
    }
    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    companion object {
        const val EXTRA_USER = "extra_user"
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2,
        )
    }
}