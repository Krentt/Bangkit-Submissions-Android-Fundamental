package com.example.mygithubuserapp.viewModel

import android.util.Log
import androidx.lifecycle.*
import com.example.mygithubuserapp.SettingPreferences
import com.example.mygithubuserapp.helper.Event
import com.example.mygithubuserapp.api.ItemsItem
import com.example.mygithubuserapp.api.ListUserResponse
import com.example.mygithubuserapp.api.ApiConfig
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel(){

    companion object{
        private const val TAG = "MainViewModel"
    }

    private val _listUsers = MutableLiveData<List<ItemsItem>>()
    val listUsers: LiveData<List<ItemsItem>> = _listUsers

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _toastText = MutableLiveData<Event<String>>()
    val toastText: LiveData<Event<String>> = _toastText

    init{
        getListUsers()
    }

    // Public fun for set the nameSearch
    fun setSearch(search: String){
        getListUsers(search)
    }

    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    fun saveThemeSetting(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            pref.saveThemeSetting(isDarkModeActive)
        }
    }

    private fun getListUsers(nama: String = "arif"){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getUsers(nama)
        client.enqueue(object : Callback<ListUserResponse> {
            override fun onResponse(
                call: Call<ListUserResponse>,
                response: Response<ListUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful){
                    if(response.body()?.totalCount != 0){
                        _listUsers.value = response.body()?.items
                    } else {
                        _toastText.value = Event("Pencarian Anda tidak ditemukan!")
                    }
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<ListUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }

        })
    }
}