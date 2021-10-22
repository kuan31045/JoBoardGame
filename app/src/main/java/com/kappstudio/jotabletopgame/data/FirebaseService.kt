package com.kappstudio.jotabletopgame.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import timber.log.Timber
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

object FirebaseService {

    fun joinParty(partyId:String){
        val party = FirebaseFirestore.getInstance()
            .collection("parties").document(partyId)
        party.update("playerIdList", FieldValue.arrayUnion("user9527"))


        }


    fun getAllParties(): MutableLiveData<List<Party>?> {
        Timber.d("-----Get All Parties------------------------------")

        val parties = MutableLiveData<List<Party>?>()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("parties").orderBy("partyTime", Query.Direction.ASCENDING)

        query
            .get()
            .addOnSuccessListener {
                query.addSnapshotListener { snapshots, e ->
                    val result =
                        snapshots?.toObjects(Party::class.java) ?: mutableListOf()

                    result.forEach {
                        //Set Host Name
                        val queryHost = db.collection("users").document(it.hostId)
                        queryHost.get().addOnSuccessListener { documentSnapshot ->
                            val user = documentSnapshot.toObject<User>()
                            if (user != null) {
                                it.hostName = user.name
                                Timber.d("user to object: ${user.name}")
                                if (result.last() == it) {
                                    parties.value = result
                                }
                            }
                        }
                    }
                }
            }

            .addOnFailureListener { exception ->
                Timber.d("Error getting documents., $exception")
                parties.value = null
            }

        return parties
    }

    fun getAllGames(): MutableLiveData<List<Game>?> {
        Timber.d("-----Get All Games------------------------------")

        var games = MutableLiveData<List<Game>?>()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("games").orderBy("createdTime", Query.Direction.ASCENDING)

        query
            .get()
            .addOnSuccessListener {
                query.addSnapshotListener { snapshots, e ->
                    games.value =
                        snapshots?.toObjects(Game::class.java) ?: mutableListOf()
                    Timber.d("Success getting documents: ${games.value}")
                }
            }

            .addOnFailureListener { exception ->
                Timber.d("Error getting documents., $exception")
                games.value = null
            }

        return games
    }

    fun addMockUser() {
        val games = FirebaseFirestore.getInstance().collection("users")
        val gameCount = FirebaseFirestore.getInstance()
            .collection("info").document("userCount")

        //user5
        repeat(5) {

            val data = hashMapOf(
                "id" to "user${it + 1}",
                "name" to "AKuan${it + 1}",
                "picture" to "image.xxx.com",
                "favoriteGame" to listOf("game${it + 1}", "game${it + 2}", "game${it + 3}"),
                "recentlyViewed" to listOf("game${it + 4}", "game${it + 5}", "game${it + 6}"),
                "status" to "好餓"
            )

            gameCount.update("userCount", FieldValue.increment(1))
            games.document("user${it + 1}")
                .set(data)
        }
    }

    fun addMockParty() {
        val parties = FirebaseFirestore.getInstance().collection("parties")

        //game20
        repeat(5) {
            val doc = parties.document()

            val data = hashMapOf(
                "id" to "${doc.id}",
                "hostId" to "${UserObject.mUserId}",
                "title" to "卡坦團${it + 1}",
                "partyTime" to Calendar.getInstance().timeInMillis + 1,
                "location" to "台北市-基隆路一段178號",
                "note" to "卡坦島(Catan)桌遊裡，玩家扮演卡坦島的新移民者，要拓荒開墾自己的領地。玩家輪流擲骰子決定哪個板塊可以生產資源，因此加入了一點機率和運氣成份。透過在不同的板塊取得的資源，玩家可以建造村莊和道路。當村莊數量越多，就可以從板塊收成越多的資源。玩家也可以和其他玩家交易，或是買發展卡來獲取額外的資源和機會。",
                "requirePlayerQty" to it + 3,
                "gameIdList" to listOf("game${it + 1}", "game${it + 2}", "game${it + 3}"),
                "gameNameList" to listOf("卡坦島${it + 1}", "卡坦島${it + 2}", "卡坦島${it + 3}"),
                "playerIdList" to listOf("user1", "user2", "user3")
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
                "totalRating" to 800,
                "ratingQty" to 100,
                "createdTime" to Calendar.getInstance().timeInMillis + 1
            )

            gameCount.update("gameCount", FieldValue.increment(1))
            games.document("game${it + 1}")
                .set(data)
        }

    }

}
