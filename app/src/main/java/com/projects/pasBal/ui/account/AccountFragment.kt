package com.projects.pasBal.ui.account

import android.content.*
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import com.projects.pasBal.R
import com.projects.pasBal.ui.auth.SignInActivity
import com.projects.pasBal.ui.auth.googleAuth.GsoBuilder
import com.projects.pasBal.ui.link.Link
import com.projects.pasBal.ui.splashScreen.SplashScreen
import com.projects.pasBal.ui.transaksi.menuTransaksi.MenuTransaksi
import com.projects.pasBal.utils.Coroutines
import com.projects.pasBal.utils.GenosDialog
import com.projects.pasBal.utils.customSnackBar.ChefSnackbar
import com.projects.pasBal.utils.firebaseServices.MyFirebaseMessagingService
import com.projects.pasBal.utils.gotoMenuUtama
import com.wang.avi.AVLoadingIndicatorView
import kotlinx.android.synthetic.main.fragment_account.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.util.*
import kotlin.concurrent.schedule


class AccountFragment : Fragment(), KodeinAware, AccountListener, GenosDialog {

    override val kodein by kodein()
    private val factory: AccountViewModelFactory by instance()
    private lateinit var root: View
    private lateinit var viewModel: AccountViewModel

    var idPref: SharedPreferences? = null
    private var mGoogleSignInClient: GoogleSignInClient? = null

    private lateinit var progressBar: AVLoadingIndicatorView
    private lateinit var signoutLayout: ConstraintLayout
    private lateinit var privacyLayout: ConstraintLayout
    private lateinit var beriNilaiLayout: ConstraintLayout
    private lateinit var textFullName: TextView
    private lateinit var textEmail: TextView

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            val tittle: String? = intent?.getStringExtra("tittle")
            val body: String? = intent?.getStringExtra("body")

            ChefSnackbar.make(root, R.mipmap.boyolali, tittle!!, body!!).show()
        }
    }


    private var layoutHistory: ConstraintLayout? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mGoogleSignInClient = context?.let { GsoBuilder.getGoogleSignInClient(it) }

        //ViewModel
        viewModel =
            ViewModelProviders.of(this, factory).get(AccountViewModel::class.java)
        root = inflater.inflate(R.layout.fragment_account, container, false)
        viewModel.accountListener = this

        initUser()
        initComponent()
        initButton()
        return root
    }


    private fun initUser() {
        viewModel.getLoggedInAdvertiser().observe(this, Observer { advertiser ->

            if (advertiser != null) {
                root.layout_login_accuont.visibility = View.GONE
                textFullName.text = advertiser.nama
                textEmail.text = advertiser.email
            } else {
                signoutLayout.visibility = View.GONE
            }

        })
    }

    private fun initComponent() {
        progressBar = root.findViewById(R.id.progressBar)
        layoutHistory = root.findViewById(R.id.layout_history)
        signoutLayout = root.findViewById(R.id.layout_sign_out)
        beriNilaiLayout = root.findViewById(R.id.layout_beri_nilai)
        textFullName = root.findViewById(R.id.textview_fullname)
        textEmail = root.findViewById(R.id.textview_email)
        privacyLayout = root.findViewById(R.id.layout_privacy_policy)

    }

    private fun initButton() {
        root.button_sign_in.setOnClickListener {
            val signIn = Intent(activity, SignInActivity::class.java)
            startActivity(signIn)
        }

        root.layout_sign_out.setOnClickListener {
            dialogHandler(
                activity!!,
                "SIGN OUT, ",
                "Apakah anda yakin?",
                R.drawable.ic_choose,
                "warning"
            )
        }

        beriNilaiLayout.setOnClickListener {
            val uri: Uri = Uri.parse("market://details?id=" + context!!.packageName)
            val goToMarket = Intent(Intent.ACTION_VIEW, uri)
            goToMarket.addFlags(
                Intent.FLAG_ACTIVITY_NO_HISTORY or
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK
            )
            try {
                startActivity(goToMarket)
            } catch (e: ActivityNotFoundException) {
                startActivity(
                    Intent(
                        Intent.ACTION_VIEW,
                        Uri.parse("http://play.google.com/store/apps/details?id=" + context!!.packageName)
                    )
                )
            }
        }


        layoutHistory!!.setOnClickListener {
            val i = Intent(activity, MenuTransaksi::class.java)
            startActivity(i)
        }

        privacyLayout.setOnClickListener {
            Intent(activity, Link::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                it.putExtra("tittle", "Kebijakan Privasi")
                it.putExtra("link", "https://www.pasangbaliho.com/syarat-dan-ketentuan")
                startActivity(it)
            }
        }
    }


    private fun signOut() {

        mGoogleSignInClient!!.signOut()

        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    Log.w(ContentValues.TAG, "getInstanceId failed", task.exception)
                    return@OnCompleteListener
                }
                val token = task.result?.token
                Coroutines.main {
                    viewModel.signOut(token!!)
                }
            })
    }


    override fun onResume() {
        super.onResume()
        progressBar.visibility = View.GONE
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

    override fun onStartedSignOut() {
        Coroutines.main {
            progressBar.visibility = View.VISIBLE

        }
    }

    override fun onSuccessSignOut() {
        Coroutines.main {
            progressBar.visibility = View.GONE

            dialogHandler(
                activity!!,
                "Berhasil",
                "Sign out berhasil",
                R.drawable.ic_welcome,
                "pesan"
            )
            Timer("loading", false).schedule(2000) {
                activity?.gotoMenuUtama()
            }
        }
    }

    override fun onFailureSignOut(message: String) {
        Coroutines.main {
            progressBar.visibility = View.GONE
            Timer("loading", false).schedule(1000) {
                Coroutines.main {
                    dialogHandler(
                        activity!!,
                        "Gagal Sign Out, ",
                        message,
                        R.drawable.ic_notif,
                        "pesan"
                    )
                }
            }
        }
    }

    override fun action() {
        signOut()
    }
}