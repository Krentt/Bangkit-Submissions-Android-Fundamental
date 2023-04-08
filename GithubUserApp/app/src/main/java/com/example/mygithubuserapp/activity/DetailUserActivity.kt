package com.example.mygithubuserapp.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.mygithubuserapp.R
import com.example.mygithubuserapp.adapter.SectionsPagerAdapter
import com.example.mygithubuserapp.api.UserDetailResponse
import com.example.mygithubuserapp.viewModel.UserDetailViewModel
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.databinding.ActivityDetailUserBinding
import com.example.mygithubuserapp.helper.ViewModelFactory
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class DetailUserActivity : AppCompatActivity() {

    private lateinit var  binding: ActivityDetailUserBinding

    private val detailViewModel by viewModels<UserDetailViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    companion object{
        @StringRes
        private val TAB_TITLES = intArrayOf(
            R.string.tab_text_1,
            R.string.tab_text_2
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.title = "Detail User"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val username = intent.getStringExtra("DATA")

        if (username != null) {
            detailViewModel.setSearch(username)
        }

        val userFavorite = FavoriteUser()

        detailViewModel.user.observe( this){
            user -> setUser (user)
            userFavorite.username = user.login
            userFavorite.avatarUrl = user.avatarUrl
            userFavorite.followers_url = user.followersUrl
            userFavorite.following_url = user.followingUrl
        }
        detailViewModel.isLoading.observe(this) {
            showLoading(it)
        }
        if (username != null) {
            detailViewModel.getFavoriteUserByusername(username).observe(this){user ->
                if (user != null){
                    binding.btnFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFav.context,
                            R.drawable.ic_favorite
                        )
                    )
                    binding.btnFav.setOnClickListener {
                        detailViewModel.delete(user)
                    }
                } else {
                    binding.btnFav.setImageDrawable(
                        ContextCompat.getDrawable(
                            binding.btnFav.context,
                            R.drawable.ic_favorite_border
                        )
                    )
                    binding.btnFav.setOnClickListener {
                        detailViewModel.insert(userFavorite)
                    }
                }
            }
        }

        val sectionPagerAdapter = SectionsPagerAdapter(this)

        if (username != null) {
            sectionPagerAdapter.username = username
        }

        val viewPager: ViewPager2 = findViewById((R.id.view_pager))
        viewPager.adapter = sectionPagerAdapter
        val tabs: TabLayout = findViewById(R.id.tabs)
        TabLayoutMediator(tabs, viewPager) { tab, position ->
            tab.text = resources.getString(TAB_TITLES[position])
        }.attach()

    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    @SuppressLint("SetTextI18n")
    private fun setUser(user: UserDetailResponse){
        binding.tvDetailNama.text = user.name
        binding.tvDetailUsername.text = user.login
        binding.tvDetailFollowersCount.text = user.followers.toString() + " Followers"
        binding.tvDetailFollowingCount.text = user.following.toString() + " Following"
        Glide.with(this@DetailUserActivity)
            .load(user.avatarUrl)
            .into(binding.ivDetailProfil)
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }



}