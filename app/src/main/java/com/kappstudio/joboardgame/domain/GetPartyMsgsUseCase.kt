package com.kappstudio.joboardgame.domain

import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.PartyMsg
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.PartyRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetPartyMsgsUseCase(
    private val partyRepository: PartyRepository,
    private val userRepository: UserRepository,
) {
    operator fun invoke(partyId: String): Flow<Result<List<PartyMsg>>> = flow {
        emit(Result.Loading)

        val msgsFlow = partyRepository.getPartyMsgs(partyId)

        msgsFlow.collect { msgs ->
            val userIdList = msgs.map { it.userId }.distinct()
            val users = userRepository.getUsersByIdList(userIdList)
            if (users is Result.Success) {
                val newMsgs = msgs.map { msg ->
                    msg.copy(user = users.data.firstOrNull { it.id == msg.userId } ?: User())
                }
                emit(Result.Success(newMsgs))
            } else {
                emit(Result.Fail(R.string.cant_get_msg))
            }
        }
    }
}