package com.kappstudio.joboardgame.login

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
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
import com.kappstudio.joboardgame.VMFactory
import com.kappstudio.joboardgame.appInstance
import com.kappstudio.joboardgame.data.User
import com.kappstudio.joboardgame.databinding.ActivityLoginBinding
import tech.gujin.toast.ToastUtil
import timber.log.Timber

class LoginActivity : AppCompatActivity() {
    lateinit var binding: ActivityLoginBinding
    val viewModel: LoginViewModel by viewModels {
        VMFactory {
            LoginViewModel(appInstance.provideJoRepository())
        }
    }
    lateinit var startActivityLauncher: StartActivityLauncher
    lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val mAlert = AlertDialog.Builder(this)
        mAlert.setTitle("Jo桌遊使用條款")
        mAlert.setMessage(getString(R.string.google_play_want_see_this2))
        mAlert.setCancelable(false)
        mAlert.setPositiveButton("接受") { _, _ ->
        }
        mAlert.setNegativeButton("拒絕") { _, _ ->
            finishApp()
        }
        val mAlertDialog = mAlert.create()
        mAlertDialog.show()
        auth = Firebase.auth
        startActivityLauncher = StartActivityLauncher(this)

        binding.btnLogin.setOnClickListener {
            firebaseLogin()
            binding.lottiePoker.visibility = View.VISIBLE
            binding.btnLogin.visibility = View.GONE
        }

        viewModel.navToMain.observe(this, {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        })
    }

    private fun firebaseLogin() {
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
                else -> {
                    ToastUtil.show(getString(R.string.login_fail))
                    binding.lottiePoker.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
                }
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
                    ToastUtil.show(getString(R.string.login_fail))
                    binding.lottiePoker.visibility = View.GONE
                    binding.btnLogin.visibility = View.VISIBLE
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