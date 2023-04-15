package com.james.submissiononefundamentalandroiddicoding

import android.os.Bundle
import android.view.View
import android.widget.Toast
import android.widget.Toast.makeText
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.james.submissiononefundamentalandroiddicoding.adapter.SectionsPagerAdapter
import com.james.submissiononefundamentalandroiddicoding.databinding.ActivityDetailBinding
import com.james.submissiononefundamentalandroiddicoding.db.UserEntity
import com.james.submissiononefundamentalandroiddicoding.model.DetailUserResponse
import com.james.submissiononefundamentalandroiddicoding.viewmodel.DetailViewModel
import com.james.submissiononefundamentalandroiddicoding.viewmodel.ViewModelFactory

class DetailActivity : AppCompatActivity() {

    private lateinit var activityDetailBinding: ActivityDetailBinding
    val detailViewModel by viewModels<DetailViewModel> {
        ViewModelFactory.getInstance(application)
    }
    private lateinit var user: UserEntity
    private var isFavorite = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activityDetailBinding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(activityDetailBinding.root)

        user = intent.getParcelableExtra(EXTRA_USER)!!

        detailViewModel.getDetailUser(user.username).observe(this){result ->
            if (result != null){
                when (result){
                    is Result.Loading -> activityDetailBinding.progressBarDetail.visibility = View.VISIBLE
                    is Result.Success -> {
                        activityDetailBinding.progressBarDetail.visibility = View.GONE
                        setDetailUser(result.data)
                    }
                    is Result.Error -> {
                        activityDetailBinding.progressBarDetail.visibility = View.GONE
                        makeText(
                            this@DetailActivity,
                            "Terjadi kesalahan" + result.error,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            }
        }

//        detailViewModel.isLoading.observe(this) {
//            showLoading(it)
//        }
//
//        detailViewModel.detailUser.observe(this) { detailUser ->
//            setDetailUser(detailUser)
//        }

        detailViewModel.getFavoriteUserByUsername(user.username)
            .observe(this@DetailActivity) { listFavByUsername ->
                isFavorite = listFavByUsername.isNotEmpty()
                if (isFavorite == false) {
                    activityDetailBinding.fabFavorite.setImageResource(R.drawable.baseline_favorite_border_24)
                } else {
                    activityDetailBinding.fabFavorite.setImageResource(R.drawable.baseline_favorite_24)
                }
            }

        activityDetailBinding.fabFavorite.setOnClickListener {
            if (isFavorite) {
                detailViewModel.delete(user)
                makeText(
                    this@DetailActivity,
                    "${user.username} have been deleted from Users Favorite ",
                    Toast.LENGTH_LONG
                ).show()
            } else {
                detailViewModel.insert(user)
                makeText(
                    this@DetailActivity,
                    "${user.username} have been add from Users Favorite",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
        val sectionsPagerAdapter = SectionsPagerAdapter(this)
        val viewPager: ViewPager2 = findViewById(R.id.view_pager)
        viewPager.adapter = sectionsPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()
        sectionsPagerAdapter.username = user.username
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
        actionBar!!.title = "${detailUser.name}"
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