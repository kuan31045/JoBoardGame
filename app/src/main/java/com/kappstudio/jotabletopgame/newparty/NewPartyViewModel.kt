package com.kappstudio.jotabletopgame.newparty

import androidx.lifecycle.*
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance
import com.kappstudio.jotabletopgame.data.sourc.remote.FirebaseService
import com.kappstudio.jotabletopgame.data.NewParty
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class NewPartyViewModel : ViewModel() {
    var time = MutableLiveData<Long>(0)

    // EditText
    var title = MutableLiveData("")
    var location = MutableLiveData("")
    var requirePlayerQty = MutableLiveData("")
    var note = MutableLiveData("")
    var games = MutableLiveData("")

    // Spinner
    val countryPosition = MutableLiveData<Int>()
    val country: LiveData<String> = Transformations.map(countryPosition) {
        appInstance.resources.getStringArray(R.array.country_list)[countryPosition.value ?: 0]
    }

    fun createParty() {
        Timber.d(
            "data= " +
                    "title= ${title.value}" +
                    "country= ${country.value}" +
                    "location= ${location.value}" +
                    "requirePlayerQt= ${requirePlayerQty.value}" +
                    "note= ${note.value}" +
                    "games= ${games.value}" +
                    "time= ${time.value}"

        )
       val gameNameList = games.value?.split(",")
        viewModelScope.launch {
          val res =   FirebaseService.createParty(
                NewParty(
                    title = title.value?:"",
                    partyTime = time.value?:0,
                    location = location.value?:"",
                    note = note.value?:"",
                    requirePlayerQty = requirePlayerQty.value?.toIntOrNull()?:1,
                    gameNameList = gameNameList as MutableList<String>,
                 )
            )


                if(res){
                    ToastUtil.show("創建成功!")
                }

        }



    }
}