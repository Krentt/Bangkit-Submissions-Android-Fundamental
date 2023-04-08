package com.example.mygithubuserapp.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithubuserapp.api.UserDetailResponse
import com.example.mygithubuserapp.api.ApiConfig
import com.example.mygithubuserapp.database.FavoriteUser
import com.example.mygithubuserapp.repository.FavUserRepository
import retrofit2.Call
import retrofit2.Response

class UserDetailViewModel(application: Application) : ViewModel(){

    companion object{
        private const val TAG = "UserDetailViewModel"
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _user = MutableLiveData<UserDetailResponse>()
    val user: LiveData<UserDetailResponse> = _user

    private val mFavUserRepository: FavUserRepository = FavUserRepository(application)


    fun setSearch(uname: String){
        getUser(uname)
    }

    fun insert(user: FavoriteUser){
        mFavUserRepository.insert(user)
    }

    fun delete(user: FavoriteUser){
        mFavUserRepository.delete(user)
    }

    fun getFavoriteUserByusername(username: String): LiveData<FavoriteUser> = mFavUserRepository.getFavoriteUserByUsername(username)

    private fun getUser(username: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(username)
        client.enqueue(object: retrofit2.Callback<UserDetailResponse>{
            override fun onResponse(
                call: Call<UserDetailResponse>,
                response: Response<UserDetailResponse>
            ) {
                if (response.isSuccessful){
                    _isLoading.value = false
                    _user.value = response.body()
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }

            }

            override fun onFailure(call: Call<UserDetailResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}