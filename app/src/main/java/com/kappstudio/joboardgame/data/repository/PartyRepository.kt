package com.kappstudio.joboardgame.data.repository

import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.PartyMsg
import com.kappstudio.joboardgame.ui.login.UserManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface PartyRepository {

    fun getParties(): Flow<List<Party>>

    fun getParty(id: String): Flow<Party>

    fun getPartyMsgs(partyId: String): Flow<List<PartyMsg>>

    suspend fun joinParty(partyId: String)

    suspend fun leaveParty(partyId: String)

    suspend fun sendPartyMsg(msg: PartyMsg): Boolean

    suspend fun deletePartyMsg(msgId: String)

    suspend fun addPartyPhoto(partyId: String, photo: String)
}

class PartyRepositoryImpl : PartyRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val partyCollection = firestore.collection(COLLECTION_PARTIES)
    private val msgCollection = firestore.collection(COLLECTION_PARTY_MSGS)

    override fun getParties(): Flow<List<Party>> {
        return partyCollection
            .snapshots()
            .map { sortParty(it.toObjects(Party::class.java)) }
    }

    override fun getParty(id: String): Flow<Party> {
        return partyCollection
            .document(id)
            .snapshots()
            .map { it.toObject(Party::class.java)!! }
    }

    override fun getPartyMsgs(partyId: String): Flow<List<PartyMsg>> {
        return msgCollection
            .whereEqualTo(FIELD_PARTY_ID, partyId)
            .snapshots()
            .map {
                it.toObjects(PartyMsg::class.java).sortedByDescending { msg -> msg.createdTime }
            }
    }

    override suspend fun joinParty(partyId: String) {
        partyCollection
            .document(partyId)
            .update(
                FIELD_PLAYER_ID_LIST,
                FieldValue.arrayUnion(UserManager.user.value?.id ?: "")
            )
    }

    override suspend fun leaveParty(partyId: String) {
        partyCollection
            .document(partyId)
            .update(
                FIELD_PLAYER_ID_LIST,
                FieldValue.arrayRemove(UserManager.user.value?.id ?: "")
            )
    }

    override suspend fun sendPartyMsg(msg: PartyMsg): Boolean =
        suspendCoroutine { continuation ->

            val newMsg = msg.copy(
                id = msg.id.ifEmpty { msgCollection.document().id }
            )

            msgCollection
                .document(newMsg.id)
                .set(newMsg)
                .addOnCompleteListener { continuation.resume(it.isSuccessful) }
        }

    override suspend fun deletePartyMsg(msgId: String) {
        msgCollection
            .document(msgId)
            .delete()
    }

    override suspend fun addPartyPhoto(partyId: String, photo: String) {
        partyCollection
            .document(partyId)
            .update(
                FIELD_PHOTOS,
                FieldValue.arrayUnion(photo)
            )
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
        const val COLLECTION_PARTY_MSGS = "partyMsgs"
        const val FIELD_PARTY_ID = "partyId"
        const val FIELD_PLAYER_ID_LIST = "playerIdList"
        const val FIELD_PHOTOS = "photos"
    }
}