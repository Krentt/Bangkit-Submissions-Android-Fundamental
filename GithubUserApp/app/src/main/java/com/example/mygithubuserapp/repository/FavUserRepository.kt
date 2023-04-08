package com.example.mygithubuserapp.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.database.FavoriteUserDao
import com.example.mygithubuserapp.database.FavoriteUserDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavUserRepository(application: Application) {
    private val mFavUserDao: FavoriteUserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init{
        val db = FavoriteUserDatabase.getDatabase(application)
        mFavUserDao = db.favUserDao()
    }

    fun getAllFavUser(): LiveData<List<FavoriteUser>> = mFavUserDao.getAllUser()

    fun insert(user: FavoriteUser){
        executorService.execute { mFavUserDao.insert(user) }
    }

    fun delete(user: FavoriteUser){
        executorService.execute { mFavUserDao.delete(user) }
    }

    fun getFavoriteUserByUsername(username: String): LiveData<FavoriteUser> = mFavUserDao.getFavoriteUserByUsername(username)
}