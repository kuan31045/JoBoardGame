package com.kappstudio.joboardgame.data.source.repository

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FirebaseFirestore
import com.kappstudio.joboardgame.data.Party
import timber.log.Timber
import java.util.Calendar

interface PartyRepository {

    fun getParties(): MutableLiveData<List<Party>>
}

class PartyRepositoryImpl : PartyRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val partyCollection = firestore.collection(COLLECTION_PARTIES)

    override fun getParties(): MutableLiveData<List<Party>> {
        Timber.d("----------getParties----------")

        val liveData = MutableLiveData<List<Party>>()

        partyCollection.addSnapshotListener { snapshot, exception ->
                exception?.let {
                    Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                }

                val result = snapshot?.toObjects(Party::class.java) ?: listOf()
                liveData.value = sortParty(result)
            }

        return liveData
    }

    private fun sortParty(parties: List<Party>): List<Party> {
        val openParties =
            parties.filter { it.partyTime + 3600000 >= Calendar.getInstance().timeInMillis }
                .sortedBy { it.partyTime }

        val overParties =
            parties.filter { it.partyTime + 3600000 < Calendar.getInstance().timeInMillis }
                .sortedByDescending { it.partyTime }

        return openParties + overParties
    }

    private companion object {
        const val COLLECTION_PARTIES = "parties"
    }
}