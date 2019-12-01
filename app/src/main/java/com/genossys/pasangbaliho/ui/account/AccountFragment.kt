package com.genossys.pasangbaliho.ui.account

import android.content.*
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.databinding.FragmentAccountBinding
import com.genossys.pasangbaliho.ui.MainActivity
import com.genossys.pasangbaliho.ui.auth.SignInActivity
import com.genossys.pasangbaliho.ui.auth.googleAuth.GsoBuilder
import com.genossys.pasangbaliho.ui.splashScreen.SplashScreen
import com.genossys.pasangbaliho.ui.transaksi.menuTransaksi.MenuTransaksi
import com.genossys.pasangbaliho.utils.customSnackBar.ChefSnackbar
import com.genossys.pasangbaliho.utils.firebaseServices.MyFirebaseMessagingService
import com.genossys.pasangbaliho.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.material.button.MaterialButton
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import kotlinx.android.synthetic.main.fragment_account.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AccountFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: AccountViewModelFactory by instance()
    var idPref: SharedPreferences? = null
    lateinit var binding: FragmentAccountBinding;
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private lateinit var accountViewModel: AccountViewModel
    private lateinit var signoutLayout: ConstraintLayout

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            val tittle: String? = intent?.getStringExtra("tittle")
            val body: String? = intent?.getStringExtra("body")

            ChefSnackbar.make(binding.root, R.mipmap.boyolali, tittle!!, body!!).show()
        }
    }

    //Dialog
    var dialog: DialogPlus? = null
    private lateinit var textJudulDialog: TextView
    private lateinit var textKonfirmasiDialog: TextView
    private lateinit var btnBatalDialog: MaterialButton
    private lateinit var btnKonfirmasiDialog: MaterialButton
    private lateinit var imageDialog: ImageView

    private var layoutHistory: ConstraintLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mGoogleSignInClient = context?.let { GsoBuilder.getGoogleSignInClient(it) }
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        accountViewModel = ViewModelProviders.of(this, factory).get(AccountViewModel::class.java)
        binding.accountViewModel = accountViewModel
        binding.lifecycleOwner = this


        initUser()
        initComponent()
        initButton()
        return binding.root
    }



    private fun initUser() {
        accountViewModel.getLoggedInAdvertiser().observe(this, Observer { advertiser ->
            Log.d("cek login", "advertiser $advertiser")

            if (advertiser != null) {
                binding.root.layout_login_accuont.visibility = View.GONE
            }else{
                signoutLayout.visibility = View.GONE
            }

        })
    }

    private fun initComponent() {
        layoutHistory = binding.root.findViewById(R.id.layout_history)
        signoutLayout = binding.root.findViewById(R.id.layout_sign_out)

    }

    private fun initButton() {
        binding.root.button_sign_in.setOnClickListener {
            val signIn = Intent(activity, SignInActivity::class.java)
            startActivity(signIn)
        }

        binding.root.layout_sign_out.setOnClickListener {

            dialogHandler()

        }

        layoutHistory!!.setOnClickListener {
            val i = Intent(activity, MenuTransaksi::class.java)
            startActivity(i)
        }
    }

    private fun dialogHandler() {
        dialog = DialogPlus.newDialog(activity)
            .setContentHolder(ViewHolder(R.layout.dialog_center))
            .setContentBackgroundResource(R.drawable.background_rounded4_corner)
            .setGravity(Gravity.CENTER)
            .setOnItemClickListener { dialog, item, view, position -> }
            .create()

        val textJudul = "SIGN OUT"
        val textKonfirmasi = "Apakah kamu yakin ingin melakukan sign out?"
        textKonfirmasiDialog = dialog?.holderView!!.findViewById(R.id.body_dialog_center)
        textJudulDialog = dialog?.holderView!!.findViewById(R.id.tittle_dialog_center)
        btnBatalDialog = dialog?.holderView!!.findViewById(R.id.btn_dialog_center_batal)
        btnKonfirmasiDialog = dialog?.holderView!!.findViewById(R.id.btn_dialog_center_ya)
        imageDialog = dialog?.holderView!!.findViewById(R.id.image_dialog_center)
        textKonfirmasiDialog.text = textKonfirmasi
        textJudulDialog.text = textJudul
        imageDialog.setImageResource(R.mipmap.konfirm)

        btnBatalDialog.setOnClickListener {
            dialog?.dismiss()
        }

        btnKonfirmasiDialog.setOnClickListener {
            signOut()
            dialog?.dismiss()
        }
        dialog?.show()
    }


    private fun signOut() {
        mGoogleSignInClient!!.signOut()
        accountViewModel.deleteAdvertiser()
        Intent(activity, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
            activity?.toast("logout berhasil")
        }
    }

    override fun onResume() {
        super.onResume()
        idPref = androidx.preference.PreferenceManager
            .getDefaultSharedPreferences(activity?.applicationContext)
        appContext = this.activity!!
        SplashScreen.STATE_ACTIVITY = "AccountFragment"

        LocalBroadcastManager.getInstance(activity?.applicationContext!!)
            .registerReceiver(
                broadCastReceiver,
                IntentFilter(MyFirebaseMessagingService.NOTIF_TRANSAKSI)
            )
    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(activity?.applicationContext!!)
            .unregisterReceiver(broadCastReceiver)
    }

    companion object {

        var appContext: Context? = null

    }
}