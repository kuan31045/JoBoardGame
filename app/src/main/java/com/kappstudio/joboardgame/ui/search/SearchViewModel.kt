package com.kappstudio.joboardgame.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.domain.GetPartiesWithHostUseCase
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface
import com.kappstudio.joboardgame.ui.user.NavToUserInterface
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale

const val formatter = "yyyy年MM月dd日 hh:mm"

class SearchViewModel(
    getPartiesWithHostUseCase: GetPartiesWithHostUseCase,
    gameRepository: GameRepository,
    userRepository: UserRepository,
) : ViewModel(), NavToPartyDetailInterface, NavToGameDetailInterface, NavToUserInterface {

    private var _isInit = MutableLiveData(true)
    val isInit: LiveData<Boolean> = _isInit

    // EditText
    var search = MutableLiveData("")

    val parties: LiveData<List<Party>> = getPartiesWithHostUseCase().asLiveData().map {
        when (it) {
            is Result.Success -> it.data
            else -> emptyList()
        }
    }

    private val games: Flow<List<Game>> = gameRepository.getGamesStream()

    private val users: Flow<List<User>> = userRepository.getUsersStream()

    private var _newParties = MutableLiveData<List<Party>>()
    val newParties: LiveData<List<Party>> = _newParties

    private var _newGames = MutableLiveData<List<Game>>()
    val newGames: LiveData<List<Game>> = _newGames

    private var _newUsers = MutableLiveData<List<User>>()
    val newUsers: LiveData<List<User>> = _newUsers

    fun onInit() {
        _isInit.value = false
    }

    fun search() {
        viewModelScope.launch {
            val query = search.value?.replace("\\s".toRegex(), "")?.lowercase(Locale.ROOT) ?: ""
            if (query != "") {
                searchParties(query)
                searchGames(query)
                searchUsers(query)
            }
        }
    }

    private fun searchParties(query: String) {
        viewModelScope.launch {
            val filteredList = mutableListOf<Party>()

            parties.value?.forEach { party ->
                val title = party.title.lowercase(Locale.ROOT)
                val host = party.host.name.lowercase(Locale.ROOT)
                val location = party.location.address.lowercase(Locale.ROOT)
                val time = SimpleDateFormat(formatter).format(party.partyTime)
                var game = ""

                party.gameNameList.forEach {
                    game += it
                }

                if (title.contains(query) || host.contains(query) || location.contains(query) || time.contains(
                        query
                    ) || game.contains(query)
                ) {
                    filteredList.add(party)
                }
            }

            _newParties.value = filteredList
        }

    }

    private fun searchUsers(query: String) {
        viewModelScope.launch {
            val filteredList = mutableListOf<User>()
            users.first().forEach { user ->
                val name = user.name.lowercase(Locale.ROOT)
                if (name.contains(query)) {
                    filteredList.add(user)
                }
            }

            _newUsers.value = filteredList
        }
    }

    private fun searchGames(query: String) {
        viewModelScope.launch {
            val filteredList = mutableListOf<Game>()
            games.first().forEach { game ->
                val name = game.name.lowercase(Locale.ROOT)

                if (name.contains(query)) {
                    filteredList.add(game)
                }
            }

            _newGames.value = filteredList
        }
    }
}