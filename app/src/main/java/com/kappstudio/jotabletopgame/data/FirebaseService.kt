package com.kappstudio.jotabletopgame.data

import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.kappstudio.jotabletopgame.R
import com.kappstudio.jotabletopgame.appInstance
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import tech.gujin.toast.ToastUtil
import timber.log.Timber
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FirebaseService {


    suspend fun getUserById(userId: String): User? =
        suspendCoroutine { continuation ->
            Timber.d("-----Get User By Id------------------------------")

            FirebaseFirestore.getInstance()
                .collection("users").document(userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val user = task.result?.toObject(User::class.java)
                        continuation.resume(user)
                    } else {
                        task.exception?.let {
                            //   continuation.resume(Result.Error(it))
                            Timber.w("Error getting documents.,$it")
                            return@addOnCompleteListener
                        }
                    }
                }
        }


    fun getLivePartyById(partyId: String): MutableLiveData<Party> {
        Timber.d("-----Get Party By Id------------------------------")

        val party = MutableLiveData<Party>()

        FirebaseFirestore.getInstance().collection("parties").document(partyId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Timber.d("Current data: ${snapshot.data}")
                    party.value = snapshot.toObject<Party>()
                } else {
                    Timber.d("Current data: null")
                }
            }

        return party
    }

    fun getLiveParties(): MutableLiveData<List<Party>> {
        Timber.d("-----Get All Parties------------------------------")

        val liveData = MutableLiveData<List<Party>>()

        FirebaseFirestore.getInstance()
            .collection("parties").orderBy("partyTime", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                liveData.value = snapshot?.toObjects(Party::class.java) ?: mutableListOf()
                Timber.d("Current data: ${liveData.value}")
            }
        return liveData
    }

    suspend fun getGames(): List<Game> = suspendCoroutine { continuation ->
        Timber.d("-----Get All Games------------------------------")

        FirebaseFirestore.getInstance()
            .collection("games").orderBy("createdTime", Query.Direction.ASCENDING)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val list = task.result?.toObjects(Game::class.java) ?: mutableListOf()
                    Timber.d("Current data: $list")

                    continuation.resume(list)
                } else {
                    task.exception?.let {

                        Timber.w("Error getting documents. ${it.message}")
                        return@addOnCompleteListener
                    }
                }
            }
    }

    fun leaveParty(partyId: String) {
        Timber.d("-----Leave Party------------------------------")
        FirebaseFirestore.getInstance()
            .collection("parties").document(partyId)
            .update("playerIdList", FieldValue.arrayRemove(UserManager.user["id"]))
        ToastUtil.show(appInstance.getString(R.string.bye))
    }

    fun joinParty(partyId: String) {
        Timber.d("-----Join Party------------------------------")
        FirebaseFirestore.getInstance()
            .collection("parties").document(partyId)
            .update("playerIdList", FieldValue.arrayUnion(UserManager.user["id"]))
        ToastUtil.show(appInstance.getString(R.string.welcome))
    }

    fun addMockUser() {
        val games = FirebaseFirestore.getInstance().collection("users")
        val gameCount = FirebaseFirestore.getInstance()
            .collection("info").document("userCount")

        //user5
        repeat(5) { n ->

            val data = hashMapOf(
                "id" to "user${n + 1}",
                "name" to "AKuan${n + 1}",
                "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/defult_profile.png?alt=media&token=35186e06-2e6f-4ec6-9768-c64491c4cfc9",
                "status" to "好餓",

                "favoriteGame" to listOf(

                    hashMapOf(
                        "id" to "game${n + 1}",
                        "name" to "卡坦島${n + 1}",
                        "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=640a394b-a6f1-4867-860a-b5599ba2a72e"
                    ),
                    hashMapOf(
                        "id" to "game${n + 2}",
                        "name" to "卡坦島${n + 2}",
                        "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=640a394b-a6f1-4867-860a-b5599ba2a72e"
                    )

                ),
                "recentlyViewed" to listOf(

                    hashMapOf(
                        "id" to "game${n + 3}",
                        "name" to "卡坦島${n + 3}",
                        "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=640a394b-a6f1-4867-860a-b5599ba2a72e"
                    ),
                    hashMapOf(
                        "id" to "game${n + 4}",
                        "name" to "卡坦島${n + 4}",
                        "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=640a394b-a6f1-4867-860a-b5599ba2a72e"
                    )

                )
            )

            gameCount.update("userCount", FieldValue.increment(1))
            games.document("user${n + 1}")
                .set(data)
        }
    }

    fun addMockParty() {
        val parties = FirebaseFirestore.getInstance().collection("parties")

        //game20
        repeat(5) {
            val doc = parties.document()

            val data = hashMapOf(
                "id" to doc.id,
                "hostId" to UserManager.user["id"],
                "host" to UserManager.user,
                "title" to "卡坦團${it + 1}",
                "partyTime" to Calendar.getInstance().timeInMillis + 10000,
                "location" to "台北市-基隆路一段178號",
                "note" to "提供箱${it + 1}箱啤酒",
                "requirePlayerQty" to it + 3,
                "gameNameList" to listOf("卡坦島${it + 1}", "卡坦島${it + 2}", "卡坦島${it + 3}"),
                "playerIdList" to listOf("user1", "user2", "user3"),
                "playerList" to listOf(

                    hashMapOf(
                        "id" to "user${it + 1}",
                        "name" to "AKuan${it + 1}",
                        "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=640a394b-a6f1-4867-860a-b5599ba2a72e"
                    ),
                            hashMapOf(
                            "id" to "user${it + 2}",
                    "name" to "AKuan${it + 2}",
                    "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=640a394b-a6f1-4867-860a-b5599ba2a72e"
                ),
                        hashMapOf(
                        "id" to "user${it + 3}",
                "name" to "AKuan${it + 3}",
                "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=640a394b-a6f1-4867-860a-b5599ba2a72e"
            ),
                    hashMapOf(
                        "id" to "user${it + 4}",
                        "name" to "AKuan${it + 4}",
                        "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=640a394b-a6f1-4867-860a-b5599ba2a72e"
                    )
                )
            )

            doc.set(data)

        }
    }

    fun addMockGame() {
        val games = FirebaseFirestore.getInstance().collection("games")
        val gameCount = FirebaseFirestore.getInstance()
            .collection("info").document("gameCount")

        //game20
        repeat(20) {
            val data = hashMapOf(
                "id" to "game${it + 1}",
                "name" to "卡坦島${it + 1}",
                "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=d5aea421-5119-471f-9fa3-0ce7da5d5140",
                "type" to listOf("策略", "貿易"),
                "time" to 70,
                "minPlayerQty" to 3,
                "maxPlayerQty" to 4,
                "desc" to "卡坦島(Catan)桌遊裡，玩家扮演卡坦島的新移民者，要拓荒開墾自己的領地。玩家輪流擲骰子決定哪個板塊可以生產資源，因此加入了一點機率和運氣成份。透過在不同的板塊取得的資源，玩家可以建造村莊和道路。當村莊數量越多，就可以從板塊收成越多的資源。玩家也可以和其他玩家交易，或是買發展卡來獲取額外的資源和機會。",
                "avgRating" to 8.5,
                "ratingQty" to 100,
                "createdTime" to Calendar.getInstance().timeInMillis + 1
            )

            gameCount.update("gameCount", FieldValue.increment(1))
            games.document("game${it + 1}")
                .set(data)
        }
    }
}





