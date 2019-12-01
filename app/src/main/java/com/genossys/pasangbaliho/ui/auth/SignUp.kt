package com.genossys.pasangbaliho.ui.auth

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.data.db.entity.Advertiser
import com.genossys.pasangbaliho.ui.MainActivity
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.snackbarSuccess
import com.genossys.pasangbaliho.utils.toast
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.wang.avi.AVLoadingIndicatorView
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance

class SignUp : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    var viewModel: AuthViewModel? = null

    private var email: String? = null
    private var nama: String? = null
    private var telp: String? = null
    private var alamat: String? = null
    private var password: String? = null
    private var confPassword: String? = null

    lateinit var root: ConstraintLayout
    lateinit var editEmail: TextInputEditText
    lateinit var editNama: TextInputEditText
    lateinit var editTelp: TextInputEditText
    lateinit var editAlamat: TextInputEditText
    lateinit var editPassword: TextInputEditText
    lateinit var editConfirmPassword: TextInputEditText
    lateinit var buttonSignUp: MaterialButton
    lateinit var pgbLoading: AVLoadingIndicatorView
    lateinit var btnReload: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        viewModel = ViewModelProviders.of(this, factory).get(AuthViewModel::class.java)
        viewModel!!.authListener = this

        initComponent()
        initButton()
    }


    private fun initComponent() {

        root = findViewById(R.id.root_layout)
        editEmail = findViewById(R.id.edit_email)
        editNama = findViewById(R.id.edit_nama)
        editTelp = findViewById(R.id.edit_telp)
        editAlamat = findViewById(R.id.edit_alamat)
        editPassword = findViewById(R.id.edit_password)
        editConfirmPassword = findViewById(R.id.edit_conf_password)
        buttonSignUp = findViewById(R.id.button_sign_up)
        pgbLoading = findViewById(R.id.progress_loading)
        btnReload = findViewById(R.id.reload)

        btnReload.visibility = View.GONE
        pgbLoading.visibility = View.GONE
    }

    private fun initButton() {
        buttonSignUp.setOnClickListener {
            Coroutines.main {
                prepareData()
                viewModel?.registerAdvertiser(
                    email!!,
                    nama!!,
                    telp!!,
                    alamat!!,
                    password!!,
                    confPassword!!
                )
            }
        }
    }

    private fun prepareData() {
        email = editEmail.text.toString()
        nama = editNama.text.toString()
        telp = editTelp.text.toString()
        alamat = editTelp.text.toString()
        password = editPassword.text.toString()
        confPassword = editConfirmPassword.text.toString()
    }

    override fun onStarted() {
        Coroutines.main {
            pgbLoading.visibility = View.VISIBLE
            btnReload.visibility = View.GONE

        }

    }

    override fun onSuccess(advertiser: Advertiser) {
        Coroutines.main {
            root.snackbarSuccess("sukses")
            pgbLoading.visibility = View.GONE
            btnReload.visibility = View.GONE

            Intent(this, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
            }
        }
    }

    override fun onFailure(message: String) {
        Coroutines.main {
            toast(message)
            pgbLoading.visibility = View.GONE
            btnReload.visibility = View.VISIBLE
        }
    }
}
