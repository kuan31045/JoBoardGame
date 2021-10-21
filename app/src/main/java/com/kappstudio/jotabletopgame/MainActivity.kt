package com.kappstudio.jotabletopgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kappstudio.jotabletopgame.databinding.ActivityMainBinding




class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        setSupportActionBar(binding.toolbar)
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment, R.id.gameFragment, R.id.toolFragment,R.id.profileFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            binding.ivLogo.visibility = View.GONE

            binding.tvTitle.text = when (destination.id) {

                R.id.homeFragment -> {
                    binding.ivLogo.visibility = View.VISIBLE
                    ""
                }
                R.id.gameFragment -> getString(R.string.game_page)
                R.id.toolFragment -> getString(R.string.tool_page)
                R.id.profileFragment -> getString(R.string.profile_page)

                else -> ""
            }

        }
    }
}
