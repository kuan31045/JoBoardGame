package com.kappstudio.jotabletopgame.data

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import timber.log.Timber
import java.io.Serializable
import java.util.*
import kotlin.collections.HashMap

object FirebaseService {
    fun getAllGames(): MutableLiveData<List<Game>?> {
        Log.d("Firebase","-----Get Games------------------------------")

        var games = MutableLiveData<List<Game>?>()
        val db = FirebaseFirestore.getInstance()
        val query = db.collection("games").orderBy("createdTime", Query.Direction.ASCENDING)

        query
            .get()
            .addOnSuccessListener {
                query.addSnapshotListener { snapshots, e ->
                    games.value =
                        snapshots?.toObjects(Game::class.java) ?: mutableListOf()
                    Log.d("Firebase","${games.value}")
                }
            }

            .addOnFailureListener { exception ->
                Log.d("Firebase","Error getting documents., $exception")
                games.value = null
            }

        return games
    }

    fun addMockParty(data: HashMap<String, Serializable?>) {
        val articles = FirebaseFirestore.getInstance()
            .collection("articles")
        val document = articles.document()
        data["id"] = document.id
        document.set(data)
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
                "type" to arrayListOf<String>("策略", "貿易"),
                "time" to 70,
                "minPlayerQty" to 3,
                "maxPlayerQty" to 4,
                "desc" to "卡坦島(Catan)桌遊裡，玩家扮演卡坦島的新移民者，要拓荒開墾自己的領地。玩家輪流擲骰子決定哪個板塊可以生產資源，因此加入了一點機率和運氣成份。透過在不同的板塊取得的資源，玩家可以建造村莊和道路。當村莊數量越多，就可以從板塊收成越多的資源。玩家也可以和其他玩家交易，或是買發展卡來獲取額外的資源和機會。",
                "totalRating" to 800,
                "ratingQty" to 100,
                "createdTime" to Calendar.getInstance()
                    .timeInMillis
            )

            gameCount.update("gameCount", FieldValue.increment(1))
            games.document("game${it + 1}")
                .set(data)
        }

    }
}
