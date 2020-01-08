package com.projects.pasBal.ui.auth

import android.content.ContentValues
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.tasks.OnCompleteListener
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.iid.FirebaseInstanceId
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import com.projects.pasBal.R
import com.projects.pasBal.data.db.entity.Advertiser
import com.projects.pasBal.ui.link.Link
import com.projects.pasBal.utils.Coroutines
import com.projects.pasBal.utils.gotoMenuUtama
import com.projects.pasBal.utils.isEmailValid
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.activity_sign_up.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.concurrent.schedule


class SignUp : AppCompatActivity(), AuthListener, KodeinAware {

    override val kodein by kodein()
    private val factory: AuthViewModelFactory by instance()
    var viewModel: AuthViewModel? = null


    private var email: String? = null
    private var nama: String? = null
    private var namaInstansi: String? = null
    private var telp: String? = null
    private var alamat: String? = null
    private var password: String? = null
    private var confPassword: String? = null

    lateinit var root: ConstraintLayout
    lateinit var editEmail: TextInputEditText
    lateinit var editNama: TextInputEditText
    lateinit var editNamaInstansi: TextInputEditText
    lateinit var editTelp: TextInputEditText
    lateinit var editAlamat: TextInputEditText
    lateinit var editPassword: TextInputEditText
    lateinit var editConfirmPassword: TextInputEditText
    lateinit var buttonSignUp: MaterialButton
    lateinit var pgbLoading: AVLoadingIndicatorView
    lateinit var btnReload: ImageView
    lateinit var btnBack: ImageView
    lateinit var checkBoxSyarat: CheckBox
    lateinit var syaratdanketentuan: TextView


    //Dialog
    var dialog: DialogPlus? = null
    private lateinit var textJudulDialog: TextView
    private lateinit var textKonfirmasiDialog: TextView
    private lateinit var btnBatalDialog: MaterialButton
    private lateinit var btnKonfirmasiDialog: MaterialButton
    private lateinit var imageDialog: ImageView

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
        editNamaInstansi = findViewById(R.id.edit_nama)
        editTelp = findViewById(R.id.edit_telp)
        editAlamat = findViewById(R.id.edit_alamat)
        editPassword = findViewById(R.id.edit_password)
        editConfirmPassword = findViewById(R.id.edit_conf_password)
        buttonSignUp = findViewById(R.id.button_sign_up)
        pgbLoading = findViewById(R.id.progress_loading)
        btnReload = findViewById(R.id.reload)
        btnBack = findViewById(R.id.button_back)
        checkBoxSyarat = findViewById(R.id.checkbox_syaratketentuan)
        syaratdanketentuan = findViewById(R.id.text_syaratketentuan)

        btnReload.visibility = View.GONE
        pgbLoading.visibility = View.GONE

        if (intent.getIntExtra("extra", 0) == 1) {
            email = intent.getStringExtra("email")
            nama = intent.getStringExtra("nama")

            layout_edit_email.endIconMode = TextInputLayout.END_ICON_NONE
            layout_edit_nama.endIconMode = TextInputLayout.END_ICON_NONE

            editEmail.setText(email)
            editNama.setText(nama)

            editEmail.isEnabled = false
            editNama.isEnabled = false

        }
    }

    private fun initTextEmail(): Boolean {
        val isValid: Boolean

        email = editEmail.text.toString()
        when {
            email!!.isEmpty() -> {
                layout_edit_email.error = "email harus di isi."
                isValid = false
            }
            !isEmailValid(email) -> {
                layout_edit_email.error = "harus di isi format email."
                isValid = false
            }
            else -> {
                layout_edit_email.error = null
                isValid = true
            }
        }
        return isValid
    }

    private fun initTextNama(): Boolean {
        val isValid: Boolean
        nama = editNama.text.toString()
        when {
            nama!!.isEmpty() -> {
                layout_edit_nama.error = "nama harus di isi."
                isValid = false
            }
            else -> {
                layout_edit_nama.error = null
                isValid = true
            }
        }
        return isValid
    }


    private fun initTextNamaInstansi(): Boolean {
        val isValid: Boolean
        namaInstansi = editNamaInstansi.text.toString()
        when {
            namaInstansi!!.isEmpty() -> {
                layout_edit_nama_instansi.error = "nama harus di isi."
                isValid = false
            }
            else -> {
                layout_edit_nama_instansi.error = null
                isValid = true
            }
        }
        return isValid
    }

    private fun initTextNoTelp(): Boolean {
        val isValid: Boolean
        telp = editTelp.text.toString()
        when {
            telp!!.isEmpty() -> {
                layout_edit_telp.error = "no telp harus di isi."
                isValid = false
            }
            telp!!.length > 14 -> {
                layout_edit_telp.error = "no telp tidak valid."
                isValid = false
            }
            else -> {
                layout_edit_telp.error = null
                isValid = true
            }
        }
        return isValid
    }

    private fun initTextAlamat(): Boolean {
        val isValid: Boolean
        alamat = editAlamat.text.toString()
        when {
            alamat!!.isEmpty() -> {
                layout_edit_alamat.error = "alamat harus di isi."
                isValid = false
            }
            else -> {
                layout_edit_alamat.error = null
                isValid = true
            }
        }

        return isValid
    }

    private fun initTextPassword(): Boolean {
        val isValid: Boolean
        password = editPassword.text.toString()
        when {
            password!!.isEmpty() -> {
                layout_edit_password.error = "password harus di isi."
                isValid = false
            }
            password!!.length < 6 -> {
                layout_edit_password.error = "password harus lebih dari 6 karakter."
                isValid = false
            }
            else -> {
                layout_edit_password.error = null
                isValid = true
            }
        }
        return isValid
    }

    private fun initTextConfirmPasswordt(): Boolean {
        val isValid: Boolean
        confPassword = editConfirmPassword.text.toString()
        when {
            confPassword != password -> {
                layout_edit_conf_password.error =
                    "konfirmasi password harus sama dengan password."
                isValid = false
            }
            else -> {
                layout_edit_conf_password.error = null
                isValid = true
            }
        }
        return isValid
    }

    private fun initButton() {

        btnBack.setOnClickListener {
            finish()
        }

        buttonSignUp.setOnClickListener {
            Coroutines.main {
                if (!initTextEmail() || !initTextNama() || !initTextNamaInstansi()
                    || !initTextNoTelp() || !initTextAlamat() || !initTextPassword() || !initTextConfirmPasswordt()
                ) {
                    initTextEmail()
                    initTextNama()
                    initTextNamaInstansi()
                    initTextNoTelp()
                    initTextAlamat()
                    initTextPassword()
                    initTextConfirmPasswordt()

                    dialogHandler(
                        "Pendaftaran gagal",
                        "Cek kembali inputan mu...",
//                        "Cek kembali inputan anda",
                        R.drawable.ic_warnin,
                        false
                    )
                } else if (!checkBoxSyarat.isChecked) {
                    dialogHandler(
                        "Pendaftaran gagal",
                        "Anda harus menyetujui syarat dan ketentuan...",
//                        "Cek kembali inputan anda",
                        R.drawable.ic_warnin,
                        false
                    )
                } else {

                    FirebaseInstanceId.getInstance().instanceId
                        .addOnCompleteListener(OnCompleteListener { task ->
                            if (!task.isSuccessful) {
                                Log.w(ContentValues.TAG, "getInstanceId failed", task.exception)
                                return@OnCompleteListener
                            }
                            Coroutines.main {
                                val token = task.result?.token
                                viewModel?.registerAdvertiser(
                                    email!!,
                                    nama!!,
                                    namaInstansi!!,
                                    telp!!,
                                    password!!,
                                    alamat!!,
                                    token!!
                                )
                            }
                        })
                }
            }
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


    private fun dialogHandler(judul: String, body: String, gambar: Int, tanpaTombol: Boolean) {
        dialog = DialogPlus.newDialog(this)
            .setContentHolder(ViewHolder(R.layout.dialog_center))
            .setContentBackgroundResource(R.drawable.background_rounded4_corner)
            .setGravity(Gravity.CENTER)
            .create()

        textKonfirmasiDialog = dialog?.holderView!!.findViewById(R.id.body_dialog_center)
        textJudulDialog = dialog?.holderView!!.findViewById(R.id.tittle_dialog_center)
        btnBatalDialog = dialog?.holderView!!.findViewById(R.id.btn_dialog_center_batal)
        btnKonfirmasiDialog = dialog?.holderView!!.findViewById(R.id.btn_dialog_center_ya)
        imageDialog = dialog?.holderView!!.findViewById(R.id.image_dialog_center)
        textKonfirmasiDialog.text = body
        textJudulDialog.text = judul
        imageDialog.setImageResource(gambar)

        btnKonfirmasiDialog.visibility = View.GONE
        btnBatalDialog.text = getString(R.string.ok)

        btnBatalDialog.setOnClickListener {
            dialog?.dismiss()
        }

        if (tanpaTombol) {
            btnBatalDialog.visibility = View.GONE
        }

        dialog?.show()
    }

    override fun onStarted() {
        Coroutines.main {
            pgbLoading.visibility = View.VISIBLE
            btnReload.visibility = View.GONE

        }

    }

    override fun onSuccess(advertiser: Advertiser) {
        Coroutines.main {

            pgbLoading.visibility = View.GONE
            btnReload.visibility = View.GONE
            dialogHandler(
                "Pendaftaran berhasil",
                "Selamat datang ${advertiser.nama}",
                R.drawable.ic_welcome,
                true
            )

            Timer("loading", false).schedule(2000) {
                this@SignUp.gotoMenuUtama()
            }
        }
    }

    override fun onFailure(message: String) {
        Coroutines.main {
            dialogHandler("Pendaftaran gagal", message, R.drawable.ic_notif, false)
            pgbLoading.visibility = View.GONE
            btnReload.visibility = View.GONE
        }
    }

    override fun onBelumTerdaftar(email: String, nama: String) {

    }

}
