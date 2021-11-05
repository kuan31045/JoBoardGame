package com.kappstudio.joboardgame

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.libraries.places.api.Places
import com.kappstudio.joboardgame.data.UserManager
import com.kappstudio.joboardgame.databinding.ActivityMainBinding
import com.kappstudio.joboardgame.util.statusBarUtil

class MainActivity : AppCompatActivity() {
    val viewModel: MainViewModel by viewModels()
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.lifecycleOwner = this
        binding.vm = viewModel

        // Initialize the SDK
        Places.initialize(applicationContext,getString(R.string.place_api_key) )

        // Create a new PlacesClient instance
        val placesClient = Places.createClient(this)


        binding.spnUser.adapter = ArrayAdapter(
            appInstance,
            android.R.layout.simple_spinner_dropdown_item,
            appInstance.resources.getStringArray(R.array.user_list).toList()
        )
        binding.spnUser.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {

            }

            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when(position){
                    0->UserManager.user = UserManager.user1
                    1->UserManager.user = UserManager.user2

                    2->UserManager.user = UserManager.user3

                    3->UserManager.user = UserManager.user4

                }
            }

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
            supportActionBar?.title = ""
            viewModel.setBarStatus(
                when (destination.id) {
                    R.id.partyFragment -> PageType.PARTY
                    R.id.gameFragment -> PageType.GAME
                    R.id.toolsFragment -> PageType.TOOLS
                    R.id.profileFragment -> PageType.PROFILE
                    R.id.partyDetailFragment->PageType.PARTY_DETAIL
                    R.id.newPartyFragment ->PageType.NEW_PARTY
                    R.id.myPartyFragment ->PageType.MY_PARTY
                    R.id.gameDetailFragment ->PageType.GAME_DETAIL
                    R.id.userDialog ->PageType.USER
                    R.id.favoriteFragment ->PageType.FAVORITE
                    R.id.myRatingFragment ->PageType.MY_RATING
                    R.id.ratingDialog ->PageType.RATING

                    R.id.diceFragment ->PageType.DICE
                    R.id.timerFragment ->PageType.TIMER
                    R.id.bottleFragment ->PageType.BOTTLE



                    else -> PageType.OTHER
                }
            )
        }
    }
}
