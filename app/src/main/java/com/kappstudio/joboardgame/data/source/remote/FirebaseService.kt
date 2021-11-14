package com.kappstudio.joboardgame.data.source.remote

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.storage.FirebaseStorage
import com.kappstudio.joboardgame.PageType
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.*
import tech.gujin.toast.ToastUtil
import timber.log.Timber
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.HashMap
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

private const val PATH_PHOTOS = "photos"
private const val PATH_GAMES = "games"
private const val PATH_PARTIES = "parties"
private const val PATH_USERS = "users"
private const val PATH_RATINGS = "ratings"

private const val FIELD_PLAYER_ID_LIST = "playerIdList"
private const val FIELD_PLAYER_LIST = "playerList"
private const val FIELD_PHOTOS = "photos"

object FirebaseService {
    suspend fun searchGame(search: String): List<Game> =
        suspendCoroutine { continuation ->
            FirebaseFirestore.getInstance()
                .collection(PATH_GAMES)
                .orderBy("name")
                .startAt(search)
                .endAt("$search\uf8ff")
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val game =
                            task.result?.toObjects(Game::class.java) ?: mutableListOf()
                        Timber.d("search count = ${game.size} ")

                        continuation.resume(game)
                    } else {
                        task.exception?.let {
                            Timber.w("[${this::class.simpleName}] Error . ${it.message}")
                            return@addOnCompleteListener
                        }
                    }
                }
        }


    suspend fun addPartyPhoto(partyId: String, photo: String): Result<String> =
        suspendCoroutine { continuation ->
            Timber.d("-----Add Party Photo------------------------------")
            FirebaseFirestore.getInstance()
                .collection(PATH_PARTIES).document(partyId)
                .update(FIELD_PHOTOS, FieldValue.arrayUnion(photo))
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        FirebaseFirestore.getInstance()
                            .collection(PATH_USERS).document(UserManager.user["id"] ?: "")
                            .update(FIELD_PHOTOS, FieldValue.arrayUnion(photo))

                        continuation.resume(Result.Success("success"))
                    } else {
                        task.exception?.let {
                            Timber.w("[${this::class.simpleName}] Error adding photo. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(appInstance.getString(R.string.nothing))
                        )
                    }
                }
        }


    suspend fun uploadPhoto(imgUri: Uri): Result<String> =
        suspendCoroutine { continuation ->
            Timber.d("-----Upload Photo------------------------------")

            val uploadTime = Calendar.getInstance().timeInMillis
            val fileName = UserManager.user["id"] + "-" + uploadTime

            val storageRef =
                FirebaseStorage.getInstance().reference
                    .child("$PATH_PHOTOS/${UserManager.user["id"]}/$fileName")

            storageRef.putFile(imgUri)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        task.result.storage.downloadUrl.addOnCompleteListener {
                            if (it.isSuccessful) {
                                Timber.d("Upload Photo Success: $fileName")
                                continuation.resume(Result.Success(it.result.toString()))
                            } else {
                                task.exception?.let { exception ->

                                    Timber.w("[${this::class.simpleName}] Error uploading img. ${exception.message}")
                                    continuation.resume(Result.Error(exception))
                                    return@let
                                }
                                continuation.resume(
                                    Result.Fail(appInstance.getString(R.string.nothing))
                                )
                            }
                        }
                    } else {
                        task.exception?.let {
                            Timber.w("[${this::class.simpleName}] Error uploading img. ${it.message}")
                            continuation.resume(Result.Error(it))
                            return@addOnCompleteListener
                        }
                        continuation.resume(
                            Result.Fail(appInstance.getString(R.string.nothing))
                        )
                    }
                }

        }

    suspend fun removeRating(rating: Rating): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Remove Rating------------------------------")
        val game = FirebaseFirestore.getInstance()
            .collection(PATH_GAMES).document(rating.gameId)
        game.update("totalRating", FieldValue.increment(-(rating.score.toDouble())))
        game.update("ratingQty", FieldValue.increment(-1))

        FirebaseFirestore.getInstance().collection(PATH_RATINGS).document(rating.id)
            .delete()
            .addOnSuccessListener {
                continuation.resume(true)
            }
            .addOnFailureListener {
                continuation.resume(false)
            }

    }

    fun updateRating(rating: Rating, addScore: Int) {
        val game = FirebaseFirestore.getInstance()
            .collection(PATH_GAMES).document(rating.gameId)
        game.update("totalRating", FieldValue.increment(addScore.toDouble()))

        if (rating.id == "") {
            game.update("ratingQty", FieldValue.increment(1))
        }

    }

    suspend fun sendRating(rating: NewRating): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Send Rating------------------------------")

        val ratings = FirebaseFirestore.getInstance().collection(PATH_RATINGS)
        if (rating.id.isEmpty()) {
            rating.id = ratings.document().id
        }

        ratings.document(rating.id)
            .set(rating)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Send Party Msg Successful: $rating")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    suspend fun getRating(game: Game): Rating =
        suspendCoroutine { continuation ->
            Timber.d("-----Get My Rating------------------------------")

            FirebaseFirestore.getInstance()
                .collection(PATH_RATINGS)
                .whereEqualTo("userId", UserManager.user["id"])
                .whereEqualTo("gameId", game.id)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.size() != 0) {
                            Timber.d("Has Rating: ${game.name}")
                            val rating =
                                task.result?.toObjects(Rating::class.java)?.first() ?: Rating()

                            continuation.resume(rating)
                        } else {
                            Timber.d("Has Not Rating: ${game.name}")
                            continuation.resume(
                                Rating(
                                    gameId = game.id,
                                    game = game,
                                    userId = UserManager.user["id"] ?: ""
                                )
                            )
                        }

                    } else {
                        task.exception?.let {

                            Timber.w("Error getting documents. ${it.message}")
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    fun getLiveRatings(id: String): MutableLiveData<List<Rating>> {
        Timber.d("-----Get Live Ratings------------------------------")

        val liveData = MutableLiveData<List<Rating>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_RATINGS).whereEqualTo("userId", id)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                liveData.value = snapshot?.toObjects(Rating::class.java) ?: mutableListOf()
                Timber.d("Current data: ${liveData.value}")
            }

        return liveData
    }


    fun getLiveFavorites(): MutableLiveData<List<Game>> {
        Timber.d("-----Get Live Favorites------------------------------")

        val games = MutableLiveData<List<Game>>()

        FirebaseFirestore.getInstance().collection(PATH_USERS)
            .document(UserManager.user["id"] ?: "")
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Timber.d("Current data: ${snapshot.data}")
                    val user = snapshot.toObject<User>()
                    if (user != null) {
                        games.value = user.favoriteGames ?: mutableListOf()
                    }
                } else {
                    Timber.d("Current data: null")
                }
            }

        return games
    }


    suspend fun checkFavorite(gameMap: HashMap<String, String>): Boolean =
        suspendCoroutine { continuation ->
            Timber.d("-----Check Favorite------------------------------")
            FirebaseFirestore.getInstance()
                .collection(PATH_USERS)
                .whereEqualTo("id", UserManager.user["id"])
                .whereArrayContains("favoriteGames", gameMap)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        if (task.result?.size() != 0) {
                            Timber.d("Is favorite: $gameMap")
                            continuation.resume(true)
                        } else {
                            Timber.d("Not favorite: $gameMap")
                            continuation.resume(false)
                        }

                    } else {
                        task.exception?.let {

                            Timber.w("Error getting documents. ${it.message}")
                            return@addOnCompleteListener
                        }
                    }
                }
        }

    fun addToFavorite(gameMap: HashMap<String, String>) {
        Timber.d("-----Add To Favorite------------------------------")
        FirebaseFirestore.getInstance()
            .collection(PATH_USERS).document(UserManager.user["id"] ?: "")
            .update("favoriteGames", FieldValue.arrayUnion(gameMap))

        ToastUtil.show(appInstance.getString(R.string.favorite_in))
    }

    fun removeFavorite(gameMap: HashMap<String, String>) {
        Timber.d("-----Remove Favorite------------------------------")
        FirebaseFirestore.getInstance()
            .collection(PATH_USERS).document(UserManager.user["id"] ?: "")
            .update("favoriteGames", FieldValue.arrayRemove(gameMap))


        ToastUtil.show(appInstance.getString(R.string.favorite_out))
    }


    fun getLivePartyMsgs(id: String): MutableLiveData<List<PartyMsg>> {
        Timber.d("-----Get Live Party Msgs------------------------------")


        val liveData = MutableLiveData<List<PartyMsg>>()

        FirebaseFirestore.getInstance()
            .collection("partyMsgs").whereEqualTo("partyId", id)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                liveData.value = snapshot?.toObjects(PartyMsg::class.java) ?: mutableListOf()
                Timber.d("Current data: ${liveData.value}")
            }

        return liveData
    }

    suspend fun sendPartyMsg(msg: NewPartyMsg): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Send Party Msg------------------------------")

        val msgs = FirebaseFirestore.getInstance().collection("partyMsgs")
        val document = msgs.document()

        msg.id = document.id

        document
            .set(msg)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Send Party Msg Successful: $msg")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    suspend fun setUserStatus(status: String): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Set User Status------------------------------")

        FirebaseFirestore.getInstance().collection(PATH_USERS)
            .document(UserManager.user["id"] ?: "")
            .update("status", status)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Set User Status Successful")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Set User Status  Error. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    suspend fun getUserHosts(userId: String): List<Party> =
        suspendCoroutine { continuation ->
            Timber.d("-----Get User Hosts------------------------------")

            FirebaseFirestore.getInstance()
                .collection(PATH_PARTIES).whereEqualTo("hostId", userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = task.result?.toObjects(Party::class.java) ?: mutableListOf()
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

    fun getLiveUserById(userId: String): MutableLiveData<User> {
        Timber.d("-----Get Live User By Id------------------------------")

        val user = MutableLiveData<User>()

        FirebaseFirestore.getInstance().collection(PATH_USERS).document(userId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Timber.d("Current data: ${snapshot.data}")
                    user.value = snapshot.toObject<User>()
                } else {
                    Timber.d("Current data: null")
                }
            }

        return user
    }

    fun getLiveGameById(gameId: String): MutableLiveData<Game> {
        Timber.d("-----Get Live Game By Id------------------------------")

        val game = MutableLiveData<Game>()

        FirebaseFirestore.getInstance().collection(PATH_GAMES).document(gameId)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                if (snapshot != null && snapshot.exists()) {
                    Timber.d("Current data: ${snapshot.data}")
                    game.value = snapshot.toObject<Game>()?.apply {
                        viewedTime = Calendar.getInstance().timeInMillis
                    }
                } else {
                    Timber.d("Current data: null")
                }
            }

        return game
    }

    suspend fun getUserParties(userId: String): List<Party> =
        suspendCoroutine { continuation ->
            Timber.d("-----Get User Parties------------------------------")

            FirebaseFirestore.getInstance()
                .collection(PATH_PARTIES).whereArrayContains(FIELD_PLAYER_ID_LIST, userId)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        val list = task.result?.toObjects(Party::class.java) ?: mutableListOf()
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

    suspend fun createParty(party: NewParty): Boolean = suspendCoroutine { continuation ->
        Timber.d("-----Create Party------------------------------")

        val parties = FirebaseFirestore.getInstance().collection(PATH_PARTIES)
        val document = parties.document()

        party.id = document.id

        document
            .set(party)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("Create Party Successful: $party")
                    continuation.resume(true)
                } else {
                    task.exception?.let {

                        Timber.w("[${this::class.simpleName}] Error getting documents. ${it.message}")
                        continuation.resume(false)
                        return@addOnCompleteListener
                    }
                    continuation.resume(false)
                }
            }
    }

    suspend fun getGameByName(gameName: String): Game =
        suspendCoroutine { continuation ->
            Timber.d("-----Get Game By Name------------------------------")


            FirebaseFirestore.getInstance()
                .collection(PATH_GAMES).whereEqualTo("name", gameName)
                .get()
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {

                        // 若資料庫內找不到此Game，給予一個新的Game物件，name自設


                        continuation.resume(
                            try {
                                task.result?.first()?.toObject(Game::class.java)
                                    ?: Game(name = gameName)
                            } catch (e: Exception) {
                                Timber.d("Game not found: $gameName")
                                Game(name = gameName)
                            }
                        )

                    } else {
                        task.exception?.let {
                            //   continuation.resume(Result.Error(it))
                            Timber.w("Error getting documents.,$it")
                            return@addOnCompleteListener
                        }
                    }
                }

        }

    suspend fun getGamesByNames(gameNameList: List<String>): List<Game> =
        suspendCoroutine { continuation ->
            Timber.d("-----Get Games By Names------------------------------")
            var games = mutableListOf<Game>()

            if (gameNameList.isEmpty()) {
                Timber.d("Game list is empty.")
                continuation.resume(games)
            }

            gameNameList.forEach {
                FirebaseFirestore.getInstance()
                    .collection(PATH_GAMES).whereEqualTo("name", it)
                    .get()
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {

                            // 若資料庫內找不到此Game，給予一個新的Game物件，name自設
                            games.add(
                                try {
                                    task.result?.first()?.toObject(Game::class.java)
                                        ?: Game(name = it)
                                } catch (e: Exception) {
                                    Timber.d("Game not found: $it")
                                    Game(name = it)
                                }
                            )

                            Timber.d("Games.add data: $it")
                            if (it == gameNameList.last()) {
                                Timber.d("Current data: $games")
                                games.sortBy {
                                    it.createdTime
                                }
                                continuation.resume(games)
                            }
                        } else {
                            task.exception?.let {
                                //   continuation.resume(Result.Error(it))
                                Timber.w("Error getting documents.,$it")
                                return@addOnCompleteListener
                            }
                        }
                    }

            }

        }


    fun getLivePartyById(partyId: String): MutableLiveData<Party> {
        Timber.d("-----Get Live Party By Id------------------------------")

        val party = MutableLiveData<Party>()

        FirebaseFirestore.getInstance().collection(PATH_PARTIES).document(partyId)
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
        Timber.d("-----Get All Live Parties------------------------------")

        val liveData = MutableLiveData<List<Party>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_PARTIES).orderBy("partyTime", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                val result = snapshot?.toObjects(Party::class.java) ?: mutableListOf()
                val openParties =
                    result.filter { it.partyTime + 3600000 >= Calendar.getInstance().timeInMillis }
                val overParties =
                    result.filter { it.partyTime + 3600000 < Calendar.getInstance().timeInMillis }

                liveData.value = openParties + overParties.sortedByDescending { it.partyTime }

                    Timber.d("Current data: ${liveData.value}")
            }
        return liveData
    }

    fun getLiveUsers(): MutableLiveData<List<User>> {
        Timber.d("-----Get All Live Users------------------------------")

        val liveData = MutableLiveData<List<User>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_USERS).orderBy("name", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                liveData.value = snapshot?.toObjects(User::class.java) ?: mutableListOf()
                Timber.d("Current data: ${liveData.value}")
            }
        return liveData
    }

    fun getLiveGames(): MutableLiveData<List<Game>> {
        Timber.d("-----Get All Live Games------------------------------")

        val liveData = MutableLiveData<List<Game>>()

        FirebaseFirestore.getInstance()
            .collection(PATH_GAMES).orderBy("createdTime", Query.Direction.ASCENDING)
            .addSnapshotListener { snapshot, e ->
                if (e != null) {
                    Timber.w("Listen failed.", e)
                    return@addSnapshotListener
                }

                liveData.value = snapshot?.toObjects(Game::class.java) ?: mutableListOf()
                Timber.d("Current data: ${liveData.value}")
            }
        return liveData
    }

    fun leaveParty(partyId: String) {
        Timber.d("-----Leave Party------------------------------")
        FirebaseFirestore.getInstance()
            .collection(PATH_PARTIES).document(partyId)
            .update(FIELD_PLAYER_ID_LIST, FieldValue.arrayRemove(UserManager.user["id"]))
        FirebaseFirestore.getInstance()
            .collection(PATH_PARTIES).document(partyId)
            .update(
                FIELD_PLAYER_LIST, FieldValue.arrayRemove(
                    UserManager.user
                )
            )
        ToastUtil.show(appInstance.getString(R.string.bye))
    }

    fun joinParty(partyId: String) {
        Timber.d("-----Join Party------------------------------")
        FirebaseFirestore.getInstance()
            .collection(PATH_PARTIES).document(partyId)
            .update(FIELD_PLAYER_ID_LIST, FieldValue.arrayUnion(UserManager.user["id"]))
        FirebaseFirestore.getInstance()
            .collection(PATH_PARTIES).document(partyId)
            .update(FIELD_PLAYER_LIST, FieldValue.arrayUnion(UserManager.user))
        ToastUtil.show(appInstance.getString(R.string.welcome))
    }

    fun addMockUser() {
        val users = FirebaseFirestore.getInstance().collection(PATH_USERS)
        val userCount = FirebaseFirestore.getInstance()
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

            userCount.update("userCount", FieldValue.increment(1))
            users.document("user${n + 1}")
                .set(data)
        }
    }

    fun addMockParty() {
        val parties = FirebaseFirestore.getInstance().collection(PATH_PARTIES)

        //game20
        repeat(3) {
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
                FIELD_PLAYER_ID_LIST to listOf("user${it + 1}", "user${it + 2}", "user${it + 3}"),
                FIELD_PLAYER_LIST to listOf(

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
                    )
                )
            )

            doc.set(data)

        }
    }

    fun addMockGame() {
        val games = FirebaseFirestore.getInstance().collection(PATH_GAMES)
        val gameCount = FirebaseFirestore.getInstance()
            .collection("info").document("gameCount")

        //game20
        repeat(10) {
            val data = hashMapOf(
                "id" to "game${it + 11}",
                "name" to "卡坦島${it + 1}",
                "image" to "https://firebasestorage.googleapis.com/v0/b/jo-tabletop-game.appspot.com/o/catan.png?alt=media&token=d5aea421-5119-471f-9fa3-0ce7da5d5140",
                "type" to listOf("策略", "貿易"),
                "time" to 70,
                "minPlayerQty" to 3,
                "maxPlayerQty" to 4,
                "desc" to "卡坦島(Catan)桌遊裡，玩家扮演卡坦島的新移民者，要拓荒開墾自己的領地。玩家輪流擲骰子決定哪個板塊可以生產資源，因此加入了一點機率和運氣成份。透過在不同的板塊取得的資源，玩家可以建造村莊和道路。當村莊數量越多，就可以從板塊收成越多的資源。玩家也可以和其他玩家交易，或是買發展卡來獲取額外的資源和機會。",
                "totalRating" to 85,
                "ratingQty" to 10,
                "createdTime" to Calendar.getInstance().timeInMillis + 1
            )

            gameCount.update("gameCount", FieldValue.increment(1))
            games.document("game${it + 11}")
                .set(data)
        }
    }


}





