package com.kappstudio.jotabletopgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.kappstudio.jotabletopgame.databinding.ActivityMainBinding
import com.kappstudio.jotabletopgame.util.statusBarUtil

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        viewModel.isImmersion.observe(this, {
            statusBarUtil(this, it)
        })

        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        binding.toolbar.setNavigationOnClickListener {
           navController.popBackStack()

        }

        setNavController()
        setBarAttr()
    }

    private fun setNavController() {
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        var appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.partyFragment, R.id.gameFragment, R.id.toolFragment, R.id.profileFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun setBarAttr() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            supportActionBar?.title = ""

            viewModel.setBarStatus(
                when (destination.id) {
                    R.id.partyFragment -> PageType.PARTY
                    R.id.gameFragment -> PageType.GAME
                    R.id.toolFragment -> PageType.TOOL
                    R.id.profileFragment -> PageType.PROFILE
                    R.id.partyDetailFragment->PageType.PARTY_DETAIL
                    R.id.newPartyFragment ->PageType.NEW_PARTY
                    R.id.myPartyFragment ->PageType.MY_PARTY
                    R.id.gameDetailFragment ->PageType.GAME_DETAIL

                    else -> PageType.OTHER
                }
            )
        }
    }
}
