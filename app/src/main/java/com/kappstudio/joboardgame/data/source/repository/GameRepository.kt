package com.kappstudio.joboardgame.data.source.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.kappstudio.joboardgame.data.Game
import timber.log.Timber

interface GameRepository {

    fun getGames(): MutableLiveData<List<Game>>
}

class GameRepositoryImpl : GameRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val gameCollection = firestore.collection(COLLECTION_GAMES)

    override fun getGames(): MutableLiveData<List<Game>> {
        Timber.d("----------getGames----------")

        val liveData = MutableLiveData<List<Game>>()

        gameCollection
            .orderBy(FIELD_CREATED_TIME, Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, exception ->
                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }
                liveData.value = snapshot?.toObjects(Game::class.java) ?: listOf()
            }

        return liveData
    }

    private companion object {
        const val COLLECTION_GAMES = "games"
        const val FIELD_CREATED_TIME = "createdTime"
    }
}