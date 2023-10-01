package com.kappstudio.joboardgame.ui.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.domain.GetUserFriendsUseCase
import com.kappstudio.joboardgame.ui.user.NavToUserInterface

class FriendViewModel(
    userId: String,
    getUserFriendsUseCase: GetUserFriendsUseCase,
) : ViewModel(), NavToUserInterface {

    val friends: LiveData<Result<List<User>>> = getUserFriendsUseCase(userId).asLiveData()
}