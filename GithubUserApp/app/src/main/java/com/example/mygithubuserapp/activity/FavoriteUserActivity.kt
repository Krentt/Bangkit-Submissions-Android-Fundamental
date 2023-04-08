package com.example.mygithubuserapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuserapp.adapter.ListUserAdapter
import com.example.mygithubuserapp.api.ItemsItem
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.databinding.ActivityFavoriteUserBinding
import com.example.mygithubuserapp.helper.ViewModelFactory
import com.example.mygithubuserapp.viewModel.FavoriteViewModel

class FavoriteUserActivity : AppCompatActivity() {

    private lateinit var binding: ActivityFavoriteUserBinding

    private val favoriteViewModel by viewModels<FavoriteViewModel>(){
        ViewModelFactory.getInstance(application)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFavoriteUserBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.listUsers.layoutManager = layoutManager

        favoriteViewModel.getAllFavUsers().observe(this){ users: List<FavoriteUser> ->
            setListUser(users)
        }

        supportActionBar?.title = "Favorite Users"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }

    private fun setListUser(users: List<FavoriteUser>){
        val items = arrayListOf<ItemsItem>()
        users.map {
            val item = ItemsItem(login = it.username, avatarUrl = it.avatarUrl!!, followersUrl = it.followers_url, followingUrl = it.following_url)
            items.add(item)
        }
        val adapter = ListUserAdapter(items)
        binding.listUsers.adapter = adapter

        adapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback {
            override fun onItemCLicked(data: ItemsItem) {
                val intentToDetail = Intent(this@FavoriteUserActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra("DATA", data.login)
                startActivity(intentToDetail)
            }
        })

    }
}