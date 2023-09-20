package com.kappstudio.joboardgame.ui.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.dylanc.activityresult.launcher.StartActivityLauncher
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.kappstudio.joboardgame.MainActivity
import com.kappstudio.joboardgame.R
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.databinding.ActivityLoginBinding
import com.kappstudio.joboardgame.util.ConnectivityUtil
import com.kappstudio.joboardgame.util.ToastUtil
import timber.log.Timber
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModel()
    private lateinit var startActivityLauncher: StartActivityLauncher
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAlert = AlertDialog.Builder(this)
        mAlert.setTitle(getString(R.string.term))
        mAlert.setMessage(getString(R.string.google_play_want_see_this2))
        mAlert.setCancelable(false)
        mAlert.setPositiveButton(getString(R.string.accept)) { _, _ ->
        }
        mAlert.setNegativeButton(getString(R.string.reject)) { _, _ ->
            finishApp()
        }
        val mAlertDialog = mAlert.create()
        mAlertDialog.show()

        auth = Firebase.auth
        startActivityLauncher = StartActivityLauncher(this)

        binding.btnLogin.setOnClickListener {
            firebaseLogin()
        }

        viewModel.navToMain.observe(this) {
            if (it == true) {
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
        }
    }

    private fun showLoginFailedUi() {
        ToastUtil.show(getString(R.string.login_fail))
        binding.lottiePoker.visibility = View.GONE
        binding.btnLogin.visibility = View.VISIBLE
    }

    private fun firebaseLogin() {
        if (ConnectivityUtil.isNotConnected()) {
            ToastUtil.show(getString(R.string.check_internet))
            return
        }

        binding.lottiePoker.visibility = View.VISIBLE
        binding.btnLogin.visibility = View.GONE

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val intent = googleSignInClient.signInIntent

        startActivityLauncher.launch(intent) { resultCode, data ->
            when (resultCode) {
                Activity.RESULT_OK -> {
                    val task = GoogleSignIn.getSignedInAccountFromIntent(data)
                    try {
                        val account = task.getResult(ApiException::class.java)

                        account.email?.let {
                            UserManager.userToken = account.email
                            firebaseAuthWithGoogle(account.idToken!!, account)
                        }
                        Timber.d("google signInResult successful")
                    } catch (e: ApiException) {
                        Timber.d("google signInResult:failed code=" + e.message)
                    }
                }

                else -> showLoginFailedUi()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String, google: GoogleSignInAccount) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Timber.d("signInWithCredential:success, user:$task")
                    onFirebaseSignInSuccess(google)
                } else {
                    Timber.d("signInWithCredential:failure", task.exception)
                    showLoginFailedUi()
                }
            }
    }

    private fun onFirebaseSignInSuccess(account: GoogleSignInAccount) {
        val user = User(
            id = account.email ?: "",
            name = account.displayName ?: "",
            image = (account.photoUrl ?: "").toString(),
        )
        viewModel.addUser(user)
    }

    private fun finishApp() {
        finish()
    }
}