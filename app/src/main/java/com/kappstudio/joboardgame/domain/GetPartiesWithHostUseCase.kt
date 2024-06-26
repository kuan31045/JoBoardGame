package com.kappstudio.joboardgame.domain

import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPartiesWithHostUseCase(
    private val partyRepository: PartyRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(userId: String? = null): Flow<Result<List<Party>>> = flow {
        emit(Result.Loading)

        val parties = if (userId == null) {
            partyRepository.getPartiesStream()
        } else {
            //Filter
            partyRepository.getUserPartiesStream(userId)
        }

        parties.collect { parties ->
            if(parties.isEmpty()){
                emit(Result.Success(emptyList()))
                return@collect
            }

            val hostIdList = parties.map { it.hostId }.distinct()
            val hosts = userRepository.getUsersByIdList(hostIdList)
            if (hosts is Result.Success) {
                val newParties = parties.map { party ->
                    party.copy(host = hosts.data.firstOrNull { it.id == party.hostId } ?: User())
                }
                emit(Result.Success(newParties))
            } else {
                emit(Result.Fail(R.string.check_internet))
            }
        }
    }
}