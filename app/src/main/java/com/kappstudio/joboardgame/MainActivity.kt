package com.kappstudio.joboardgame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.libraries.places.api.Places
import com.kappstudio.joboardgame.login.UserManager
import com.kappstudio.joboardgame.databinding.ActivityMainBinding
import com.kappstudio.joboardgame.game.GameViewModel
import com.kappstudio.joboardgame.login.LoginActivity
import com.kappstudio.joboardgame.party.PartyFragmentDirections
import com.kappstudio.joboardgame.util.statusBarUtil

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()
    lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (UserManager.userToken == null) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            viewModel.getUserData(UserManager.userToken!!)
        }

        binding.lifecycleOwner = this
        binding.vm = viewModel
        // Initialize the SDK
        Places.initialize(applicationContext, getString(R.string.place_api_key))

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)


        binding.btnToMap.setOnClickListener {
            this.findNavController(R.id.nav_host_fragment_activity_main)
                .navigate(PartyFragmentDirections.navToMapFragment(null))
        }

        binding.btnToNotification.setOnClickListener {
            this.findNavController(R.id.nav_host_fragment_activity_main)
                .navigate(PartyFragmentDirections.navToNotificationFragment())
        }


        binding.btnToSearch.setOnClickListener {
            this.findNavController(R.id.nav_host_fragment_activity_main).navigate(
                NavigationDirections.navToSearchFragment(
                    when (viewModel.page.value) {
                        PageType.PARTY -> 0
                        PageType.GAME -> 1
                        PageType.PROFILE -> 2
                        else -> 0
                    }
                )
            )
        }

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
                R.id.partyFragment, R.id.gameFragment, R.id.toolsFragment, R.id.profileFragment
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.navView.setupWithNavController(navController)
    }

    private fun setBarAttr() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            viewModel.setBarStatus(
                when (destination.id) {
                    R.id.partyFragment -> PageType.PARTY
                    R.id.gameFragment -> PageType.GAME
                    R.id.toolsFragment -> PageType.TOOLS
                    R.id.profileFragment -> PageType.PROFILE
                    R.id.partyDetailFragment -> PageType.PARTY_DETAIL
                    R.id.newPartyFragment -> PageType.NEW_PARTY
                    R.id.myPartyFragment -> PageType.MY_PARTY
                    R.id.gameDetailFragment -> PageType.GAME_DETAIL
                    R.id.userFragment -> PageType.USER
                    R.id.favoriteFragment -> PageType.FAVORITE
                    R.id.myRatingFragment -> PageType.MY_RATING
                    R.id.ratingDialog -> PageType.RATING
                    R.id.diceFragment -> PageType.DICE
                    R.id.timerFragment -> PageType.TIMER
                    R.id.bottleFragment -> PageType.BOTTLE
                    R.id.albumFragment -> PageType.ALBUM
                    R.id.photoFragment -> PageType.ALBUM
                    R.id.mapFragment -> PageType.MAP
                    R.id.searchFragment -> PageType.SEARCH
                    R.id.selectGameFragment -> PageType.SELECT_GAME
                    R.id.newGameFragment -> PageType.NEW_GAME
                    R.id.friendFragment -> PageType.FRIEND_LIST
                    R.id.myHostFragment -> PageType.MY_HOST
                    R.id.notificationFragment -> PageType.NOTIFICATION
                    R.id.drawLotsFragment -> PageType.DRAW_LOTS
                    R.id.polygraphFragment -> PageType.POLYGRAPH
                    R.id.scoreboardFragment -> PageType.SCOREBOARD
                    R.id.reportDialog -> PageType.REPORT

                    else -> PageType.OTHER
                }
            )
        }
    }
}
