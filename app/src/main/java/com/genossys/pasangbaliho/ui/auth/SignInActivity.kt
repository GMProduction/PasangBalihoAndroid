package com.genossys.pasangbaliho.ui.auth

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.data.db.entity.Advertiser
import com.genossys.pasangbaliho.databinding.ActivitySignInBinding
import com.genossys.pasangbaliho.ui.MainActivity
import com.genossys.pasangbaliho.ui.auth.googleAuth.GsoBuilder
import com.genossys.pasangbaliho.ui.splashScreen.SplashScreen
import com.genossys.pasangbaliho.utils.customSnackBar.ChefSnackbar
import com.genossys.pasangbaliho.utils.hide
import com.genossys.pasangbaliho.utils.show
import com.genossys.pasangbaliho.utils.snackbar
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class SignInActivity : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val googleSignInCode = 0

    var viewModel: AuthViewModel? = null
    lateinit var rootLayout : ConstraintLayout
    lateinit var btnSignUp : MaterialButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        mGoogleSignInClient = GsoBuilder.getGoogleSignInClient(this)

        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        val binding: ActivitySignInBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.viewmodel = viewModel
        viewModel?.authListener = this


        viewModel!!.getLoggedInAdvertiser().observe(this, Observer { advertiser ->
            Log.d("cek login", "advertiser $advertiser")

            if (advertiser != null) {
                Intent(this, MainActivity::class.java).also {
                    it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(it)
                }
            }
        })

        initComponen()
        cekIntent()

        progressbar.hide()

        initButton()

    }

    private fun initComponen(){
        rootLayout = findViewById(R.id.root_layout)
        btnSignUp = findViewById(R.id.button_sign_up)
    }

    private fun initButton(){
        button_sign_in_google.setOnClickListener {
            signIn()
        }

        btnSignUp.setOnClickListener {
            Intent(this, SignUp::class.java).also {
                startActivity(it)
            }
        }
    }

    private fun cekIntent() {
        if (intent.getIntExtra("login", 0) != 0) {
            ChefSnackbar.make(rootLayout, R.mipmap.flag, "Kamu belum login", "Login dulu ya...").show()
        }
    }

    private fun signIn() {
        val signInIntent = mGoogleSignInClient?.signInIntent
        startActivityForResult(signInIntent, googleSignInCode)
        Log.d("sign in google click", "try to login")
    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == googleSignInCode) {

            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }

    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {

        val binding: ActivitySignInBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_sign_in)
        binding.viewmodel = viewModel

        try {
            val account = completedTask.getResult(ApiException::class.java)

            FirebaseInstanceId.getInstance().instanceId
                .addOnCompleteListener(OnCompleteListener { task ->
                    if (!task.isSuccessful) {
                        Log.w(ContentValues.TAG, "getInstanceId failed", task.exception)
                        return@OnCompleteListener
                    }


                    // Get new Instance ID token
                    val token = task.result?.token

                    // Log and toast
//                val msg = getString(R.string.title_home, token)
                    Log.d(ContentValues.TAG, token!!)
                    viewModel?.onLoginByGoogle(account?.email, account?.displayName, token)
//                Toast.makeText(activity, token, Toast.LENGTH_LONG).show()
                })

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Sign in gagal", "signInResult:failed code=" + e.statusCode)
            Toast.makeText(
                this,
                "maaf, terjadi kesalahan saat login" + e.statusCode,
                Toast.LENGTH_LONG
            ).show()
        }


    }

    override fun onStarted() {
        progressbar.show()
    }

    override fun onResume() {
        super.onResume()
        SplashScreen.STATE_ACTIVITY = "SignInActivity"


    }
    override fun onSuccess(advertiser: Advertiser) {
        progressbar.hide()
        root_layout.snackbar("${advertiser.nama} is login")

    }

    override fun onFailure(message: String) {
        progressbar.hide()
        root_layout.snackbar(message)

    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }
}