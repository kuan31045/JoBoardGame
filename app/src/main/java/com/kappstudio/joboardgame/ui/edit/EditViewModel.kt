package com.kappstudio.joboardgame.ui.edit

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.data.repository.StorageRepository
import com.kappstudio.joboardgame.data.repository.UserRepository
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.util.ConnectivityUtil
import com.kappstudio.joboardgame.util.LoadApiStatus
import com.kappstudio.joboardgame.util.ToastUtil
import com.kappstudio.joboardgame.util.checkValid
import kotlinx.coroutines.launch

class EditViewModel(
    private val userRepository: UserRepository,
    private val storageRepository: StorageRepository,
) : ViewModel() {

    val me: LiveData<User> = UserManager.user

    val imageUri = MutableLiveData<Uri?>()

    private val profileImage = MutableLiveData<String>()

    // EditText
    var name = MutableLiveData(me.value?.name)
    var info = MutableLiveData(me.value?.status)

    private val _status = MutableLiveData<LoadApiStatus?>()
    val status: LiveData<LoadApiStatus?> = _status

    fun prepareUpdate() {
        if (!name.value.checkValid()) {
            ToastUtil.showByRes(R.string.plz_enter_user_name)
            return
        }

        updateProfile()
    }

    private fun updateProfile() {
        if (ConnectivityUtil.isNotConnected()) {
            ToastUtil.showByRes(R.string.check_internet)
            return
        }

        viewModelScope.launch {
            if (imageUri.value != null) {
                uploadPhoto()
            }

            val result = userRepository.updateProfile(
                me.value!!.copy(
                    name = name.value!!,
                    status = info.value ?: "",
                    image = profileImage.value ?: me.value?.image!!
                )
            )

            _status.value = if (result) {
                ToastUtil.showByRes(R.string.update_ok)
                LoadApiStatus.DONE
            } else {
                LoadApiStatus.ERROR
            }
        }
    }

    private suspend fun uploadPhoto() {
        storageRepository.uploadPhoto(imageUri.value!!).collect { photoRes ->
            _status.value = when (photoRes) {
                is Result.Success -> {
                    profileImage.value = photoRes.data ?: ""
                    status.value
                }

                is Result.Fail -> {
                    ToastUtil.showByRes(photoRes.stringRes)
                    LoadApiStatus.ERROR
                }

                else -> LoadApiStatus.LOADING
            }
        }
    }
}