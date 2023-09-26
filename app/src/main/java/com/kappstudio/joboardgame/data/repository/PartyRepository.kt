package com.kappstudio.joboardgame.data.repository

import android.net.Uri
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.snapshots
import com.google.firebase.storage.FirebaseStorage
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.Result
import com.kappstudio.joboardgame.data.Party
import com.kappstudio.joboardgame.data.PartyMsg
import com.kappstudio.joboardgame.data.Report
import com.kappstudio.joboardgame.data.remote.FirebaseService
import com.kappstudio.joboardgame.ui.login.UserManager
import com.kappstudio.joboardgame.util.ConnectivityUtil
import com.kappstudio.joboardgame.util.ToastUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import timber.log.Timber
import java.io.IOException
import java.util.Calendar
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

interface PartyRepository {

    fun getParties(): Flow<List<Party>>

    fun getParty(id: String): Flow<Party>

    fun getPartyMsgs(id: String): Flow<List<PartyMsg>>

    suspend fun joinParty(partyId: String)

    suspend fun leaveParty(partyId: String)

    suspend fun sendPartyMsg(msg: PartyMsg): Boolean

    suspend fun deletePartyMsg(id: String)

    suspend fun sendReport(report: Report): Boolean

    suspend fun addPartyPhoto(partyId: String, imgUri: Uri): Flow<Result<String>>
}

class PartyRepositoryImpl : PartyRepository {

    private val firestore = FirebaseFirestore.getInstance()
    private val partyCollection = firestore.collection(COLLECTION_PARTIES)
    private val msgCollection = firestore.collection(COLLECTION_PARTY_MSGS)
    private val reportCollection = firestore.collection(COLLECTION_REPORTS)

    private val storageReference = FirebaseStorage.getInstance().reference

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

    override fun getPartyMsgs(id: String): Flow<List<PartyMsg>> {
        return msgCollection
            .whereEqualTo(FIELD_PARTY_ID, id)
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

    override suspend fun deletePartyMsg(id: String) {
        msgCollection
            .document(id)
            .delete()
    }

    override suspend fun sendReport(report: Report): Boolean =
        suspendCoroutine { continuation ->

            val newReport = report.copy(
                id = report.id.ifEmpty { reportCollection.document().id }
            )

            reportCollection
                .document(newReport.id)
                .set(newReport)
                .addOnCompleteListener { continuation.resume(it.isSuccessful) }
        }

    override suspend fun addPartyPhoto(partyId: String, imgUri: Uri): Flow<Result<String>> = flow {
        emit(Result.Loading)

        if (ConnectivityUtil.isNotConnected()) {
            emit(Result.Fail(R.string.check_internet))
        }

        val uploadTime = Calendar.getInstance().timeInMillis
        val fileName = (UserManager.getUserId()) + uploadTime

        val uri = storageReference
            .child("${PATH_PHOTOS}/${UserManager.getUserId()}/$fileName")
            .putFile(imgUri).await()
            .task.result.storage.downloadUrl.await() ?: ""

        emit(Result.Success(uri.toString()))

    }.catch {
        Result.Fail(R.string.upload_fail)
    }.flowOn(Dispatchers.IO)

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
        const val COLLECTION_REPORTS = "reports"
        const val FIELD_PARTY_ID = "partyId"
        const val FIELD_PLAYER_ID_LIST = "playerIdList"
        const val PATH_PHOTOS = "photos"
        private const val FIELD_PHOTOS = "photos"
    }
}