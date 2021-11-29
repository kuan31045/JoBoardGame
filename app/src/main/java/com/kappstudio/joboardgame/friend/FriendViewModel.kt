package com.kappstudio.joboardgame.friend

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.source.JoRepository
import com.kappstudio.joboardgame.user.NavToUserInterface

class FriendViewModel(userId: String, private val repository: JoRepository) : ViewModel(),
    NavToUserInterface {

    val user: LiveData<User> = repository.getUser(userId)

    private var _friends = MutableLiveData<List<User>>()
    val friends: LiveData<List<User>>
        get() = _friends

    fun getFriends() {
        _friends = repository.getUsersById(user.value?.friendList?: listOf())
    }
}