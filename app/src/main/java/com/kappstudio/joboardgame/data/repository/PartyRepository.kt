package com.kappstudio.joboardgame.data.repository

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.kappstudio.joboardgame.data.Party
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import timber.log.Timber
import java.util.Calendar

interface PartyRepository {

    fun getParties(): Flow<List<Party>>
}

class PartyRepositoryImpl() : PartyRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val partyCollection = firestore.collection(COLLECTION_PARTIES)

    override fun getParties(): Flow<List<Party>> {
        Timber.d("----------getParties----------")

        return partyCollection
            .snapshots()
            .map { sortParty(it.toObjects(Party::class.java)) }
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