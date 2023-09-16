package com.kappstudio.joboardgame.domain

import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPartyWithHostUseCase(
    private val partyRepository: PartyRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(userId: String? = null): Flow<Result<List<Party>>> = flow {
        emit(Result.Loading)

        val parties = if (userId == null) {
            partyRepository.getParties()
        } else {
            //Filter
            partyRepository.getParties()
        }

        parties.collect { parties ->
            val hostIdList = parties.map { it.hostId }.distinct()
            val hosts = userRepository.getUsersByIdList(hostIdList)

            if (hosts is Result.Success) {
                val newParties = parties.map { party ->
                    party.copy(host = hosts.data.firstOrNull { it.id == party.hostId }
                        ?: User())
                }

                emit(Result.Success(newParties))

            } else {
                emit(Result.Fail(appInstance.getString(R.string.check_internet)))
            }
        }
    }
}