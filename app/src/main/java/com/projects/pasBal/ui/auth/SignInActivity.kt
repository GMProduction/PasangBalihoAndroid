package com.projects.pasBal.ui.auth

import android.content.ContentValues
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import android.os.Bundle
import android.util.Log
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.gms.tasks.Task
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.iid.FirebaseInstanceId
import com.projects.pasBal.R
import com.projects.pasBal.data.db.entity.Advertiser
import com.projects.pasBal.ui.MainActivity
import com.projects.pasBal.ui.auth.googleAuth.GsoBuilder
import com.projects.pasBal.ui.link.Link
import com.projects.pasBal.ui.splashScreen.SplashScreen
import com.projects.pasBal.utils.*
import com.projects.pasBal.utils.customSnackBar.ChefSnackbar
import kotlinx.android.synthetic.main.activity_sign_in.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.concurrent.schedule


class SignInActivity : AppCompatActivity(), AuthListener, KodeinAware, GenosDialog {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    lateinit var viewModel: AuthViewModel

    private var mGoogleSignInClient: GoogleSignInClient? = null
    private val googleSignInCode = 0

    lateinit var rootLayout: ConstraintLayout
    lateinit var btnSignUp: MaterialButton
    lateinit var btnSignIn: MaterialButton
    lateinit var progressbar: ProgressBar
    lateinit var textEmail: TextInputEditText
    lateinit var textSyarat: TextView
    lateinit var textPassword: TextInputEditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mGoogleSignInClient = GsoBuilder.getGoogleSignInClient(this)

        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        viewModel.authListener = this

        initComponen()
        cekIntent()
        initButton()

    }

    private fun isSignedIn(): Boolean {
        return GoogleSignIn.getLastSignedInAccount(this) != null

    }

    private fun initComponen() {

        rootLayout = findViewById(R.id.root_layout)
        btnSignUp = findViewById(R.id.button_sign_up)
        btnSignIn = findViewById(R.id.button_sign_in)
        progressbar = findViewById(R.id.progressbar)
        textEmail = findViewById(R.id.edit_email)
        textSyarat = findViewById(R.id.syaratdanketentuan)
        textPassword = findViewById(R.id.edit_password)


//        syaratdanketentuan.movementMethod = LinkMovementMethod.getInstance()
        progressbar.hide()

    }

    private fun initButton() {
        button_sign_in_google.setOnClickListener {
            signInGoogle()
        }

        btnSignUp.setOnClickListener {
            Intent(this, SignUp::class.java).also {
                startActivity(it)
            }
        }

        btnSignIn.setOnClickListener {
            signIn()
        }

        syaratdanketentuan.setOnClickListener {
            Intent(this, Link::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                it.putExtra("tittle", "Syarat dan Ketentuan")
                it.putExtra("link", "https://www.pasangbaliho.com/syarat-dan-ketentuan")
                startActivity(it)
            }
        }
    }

    private fun cekIntent() {
        if (intent.getIntExtra("login", 0) != 0) {
            ChefSnackbar.make(rootLayout, R.mipmap.flag, "Kamu belum login", "Login dulu ya...")
                .show()
        }
    }

    private fun signIn() {
        Coroutines.main {
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
                    viewModel.signIn(textEmail.text.toString(), textPassword.text.toString(), token)

//                Toast.makeText(activity, token, Toast.LENGTH_LONG).show()
                })

        }
    }


    private fun signInGoogle() {
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


        try {

            val account = completedTask.getResult(ApiException::class.java)

            if (!account?.email.isNullOrEmpty()) {
                viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)

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
                        viewModel.onLoginByGoogle(account?.email, account?.displayName, token)
//                Toast.makeText(activity, token, Toast.LENGTH_LONG).show()
                    })
            }
        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("Sign in gagal", "signInResult:failed code=" + e.statusCode)
            progressbar.hide()
        }


    }

    override fun onStarted() {
        progressbar.show()
    }

    override fun onResume() {
        super.onResume()
        if (isSignedIn()) {
            mGoogleSignInClient!!.signOut()
        }
        progressbar.hide()
        SplashScreen.STATE_ACTIVITY = "SignInActivity"
    }

    override fun onSuccess(advertiser: Advertiser) {
        progressbar.hide()
        dialogHandler(
            this,
            "Login Berhasil",
            "Selamat datang ${advertiser.nama}",
            R.drawable.ic_welcome,
            "sukses"
        )

        Timer("loading", false).schedule(2000) {
            this@SignInActivity.gotoMenuUtama()
        }

    }

    override fun onFailure(message: String) {
        progressbar.hide()

        if (message == ERROR_INTERNET) {
            dialogHandler(
                this,
                "Internet Error",
                ERROR_INTERNET,
                R.drawable.ic_nointernet,
                "pesan"
            )
        } else {
            dialogHandler(this, "Error", ERROR_API, R.drawable.ic_nointernet, "pesan")
        }


    }

    override fun onBelumTerdaftar(email: String, nama: String) {
        Intent(this, SignUp::class.java).also {
            it.putExtra("extra", 1)
            it.putExtra("email", email)
            it.putExtra("nama", nama)
            startActivity(it)
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }

    override fun action() {

    }
}