package com.kappstudio.joboardgame.domain

import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetUserFriendsUseCase(
    private val userRepository: UserRepository,
) {
    operator fun invoke(userId: String): Flow<Result<List<User>>> = flow {
        emit(Result.Loading)

        val userFlow = userRepository.getUserByIdStream(userId)

        userFlow.collect { user ->
            if (user.friendList.isEmpty()) {
                emit(Result.Success(emptyList()))
                return@collect
            }

            val result = userRepository.getUsersByIdList(user.friendList)
            if (result is Result.Success) {
                emit(Result.Success(result.data))
            } else {
                emit(Result.Fail(R.string.cant_get_friend))
            }
        }
    }
}