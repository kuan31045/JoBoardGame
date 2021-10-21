package com.kappstudio.jotabletopgame.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.jotabletopgame.data.FirebaseService
import com.kappstudio.jotabletopgame.data.Party
import timber.log.Timber

class HomeViewModel : ViewModel() {

    val parties: LiveData<List<Party>?> = FirebaseService.getAllParties()


    init {
          //   FirebaseService.addMockParty()
    }




}