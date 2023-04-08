package com.example.mygithubuserapp.activity

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mygithubuserapp.api.ItemsItem
import com.example.mygithubuserapp.adapter.ListUserAdapter
import com.example.mygithubuserapp.viewModel.MainViewModel
import com.example.mygithubuserapp.R
import com.example.mygithubuserapp.SettingPreferences
import com.example.mygithubuserapp.databinding.ActivityMainBinding
import com.example.mygithubuserapp.helper.ViewModelPrefFactory

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var darkMode: Boolean = false
    private var menu: Menu? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val layoutManager = LinearLayoutManager(this)
        binding.listUsers.layoutManager = layoutManager

        val pref = SettingPreferences.getInstance(dataStore)
        val mainViewModel = ViewModelProvider(this, ViewModelPrefFactory(pref)).get(MainViewModel::class.java)
        mainViewModel.listUsers.observe(this) { listUser ->
            setListUsers(listUser)
        }

        mainViewModel.isLoading.observe(this) {
            showLoading(it)
        }

        mainViewModel.toastText.observe(this) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(this@MainActivity, toastText, Toast.LENGTH_SHORT).show()
            }
        }


        mainViewModel.getThemeSettings().observe(this){ isDarkModeActive: Boolean ->
            if (isDarkModeActive){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                darkMode = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                darkMode = false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.search_menu, menu)
        this.menu = menu

        if (darkMode){
            menu.getItem(2).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_sun))
        } else {
            menu.getItem(2).setIcon(ContextCompat.getDrawable(this, R.drawable.ic_night))
        }

        val searchManager = getSystemService(SEARCH_SERVICE) as SearchManager
        val searchView = menu.findItem(R.id.search).actionView as SearchView

        val searchViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            MainViewModel::class.java)

        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.queryHint = resources.getString(R.string.search_hint)
        searchView.setOnQueryTextListener(object: SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    searchViewModel.setSearch(query)
                }
                searchView.clearFocus()
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return true

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId){
            R.id.favorite -> {
                val i = Intent(this, FavoriteUserActivity::class.java)
                startActivity(i)
                return true
            }
            R.id.night -> {
                val pref = SettingPreferences.getInstance(dataStore)
                val mainViewModel = ViewModelProvider(this, ViewModelPrefFactory(pref)).get(MainViewModel::class.java)
                mainViewModel.saveThemeSetting(!darkMode)
                return true
            }
            else -> return true
        }

    }

    private fun setListUsers(listUsersDetail : List<ItemsItem>) {
        val listUser = ArrayList<ItemsItem>()
        for (user in listUsersDetail) {
            listUser.add(user)
        }
        val adapter = ListUserAdapter(listUser)
        binding.listUsers.adapter = adapter

        adapter.setOnItemClickCallback(object: ListUserAdapter.OnItemClickCallback {
            override fun onItemCLicked(data: ItemsItem) {
                val intentToDetail = Intent(this@MainActivity, DetailUserActivity::class.java)
                intentToDetail.putExtra("DATA", data.login)
                startActivity(intentToDetail)
            }
        })
    }

    private fun showLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.progressBar.visibility = View.VISIBLE
        } else {
            binding.progressBar.visibility = View.GONE
        }
    }
}