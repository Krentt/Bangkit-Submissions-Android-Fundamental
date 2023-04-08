package com.example.mygithubuserapp.viewModel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.repository.FavUserRepository

class FavoriteViewModel(application: Application): ViewModel() {
    private val mFavoriteUserRepository: FavUserRepository = FavUserRepository(application)

    fun getAllFavUsers(): LiveData<List<FavoriteUser>> = mFavoriteUserRepository.getAllFavUser()
}