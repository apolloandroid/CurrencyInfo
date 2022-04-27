package com.apollo.currencyinfo.presentation

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import com.apollo.currencyinfo.R
import com.apollo.currencyinfo.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setUpNavigation()
    }

    private fun setUpNavigation() {

        binding.navView.setOnItemSelectedListener { menuItem ->
            handleNavigation(menuItem)
            false
        }
    }

    override fun onBackPressed() {
        Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_HOME)
            flags = Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(this)
        }
    }

    private fun handleNavigation(item: MenuItem) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container) as NavHostFragment
        val navigationController = navHostFragment.navController
        if (!item.isChecked)
            when (item.itemId) {
                R.id.fragment_popular_currencies -> navigationController.navigate(R.id.fragment_popular_currencies)
                R.id.fragment_favorite_currencies -> navigationController.navigate(R.id.fragment_favorite_currencies)
            }
        item.isChecked = true
    }
}