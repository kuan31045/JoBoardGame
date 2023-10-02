package com.kappstudio.joboardgame.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.map
import com.kappstudio.joboardgame.data.Game
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.GameRepository
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.domain.GetPartiesWithHostUseCase
import com.kappstudio.joboardgame.ui.gamedetail.NavToGameDetailInterface
import com.kappstudio.joboardgame.ui.partydetail.NavToPartyDetailInterface
import java.util.Calendar

class ProfileViewModel(
    gameRepository: GameRepository,
    getPartiesWithHostUseCase: GetPartiesWithHostUseCase,
) : ViewModel(), NavToPartyDetailInterface, NavToGameDetailInterface {

    val viewedGames: LiveData<List<Game>> = gameRepository.getAllViewedGamesStream().asLiveData()

    val me: LiveData<User> = UserManager.user

    val parties: LiveData<List<Party>> =
        getPartiesWithHostUseCase(UserManager.getUserId()).asLiveData().map {
            when (it) {
                is Result.Success -> it.data
                else -> emptyList()
            }
        }

    val comingParties: LiveData<List<Party>> = parties.map { parties ->
        parties.filter { party ->
            party.partyTime + 3600000 >= Calendar.getInstance().timeInMillis
        }
    }

    val hostParties: LiveData<List<Party>> = parties.map { parties ->
        parties.filter { party ->
            party.hostId == UserManager.getUserId()
        }
    }
}