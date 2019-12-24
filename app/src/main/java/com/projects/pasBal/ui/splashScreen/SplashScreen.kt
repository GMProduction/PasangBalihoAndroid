package com.projects.pasBal.ui.splashScreen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.projects.pasBal.R
import com.projects.pasBal.ui.MainActivity
import com.projects.pasBal.utils.Coroutines
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_splash_screen.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance


class SplashScreen : AppCompatActivity(), KodeinAware {

    override val kodein by kodein()
    private val factory: SplashViewModelFactory by instance()
    lateinit var viewModel: SplashViewModel

    private val waktuLoading = 1200
    private val waktuFadeOut = 1000


//    private val sharedPref2: SharedPreferences =
//        getSharedPreferences(EMAIL_ADVERTISER, PRIVATE_MODE)
//    private val sharedPref3: SharedPreferences = getSharedPreferences(NAMA_ADVERTISER, PRIVATE_MODE)
//    private val sharedPref4: SharedPreferences = getSharedPreferences(API_TOKEN, PRIVATE_MODE)
//    private val sharedPref5: SharedPreferences = getSharedPreferences(TELP_ADVERTUSER, PRIVATE_MODE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        viewModel = ViewModelProviders.of(this, factory).get(SplashViewModel::class.java)
        val sharedPref1 = androidx.preference.PreferenceManager
            .getDefaultSharedPreferences(this)
        Coroutines.main {
            viewModel.getUser().observe(this, Observer {
                try {
                    sharedPref1.edit().putInt(ID_ADVERTISER, it.id!!).apply()
                    sharedPref1.edit().putString(API_TOKEN, it.apiToken!!).apply()
                }catch (e: NullPointerException){
                    sharedPref1.edit().putInt(ID_ADVERTISER, 0).apply()
                    sharedPref1.edit().putString(API_TOKEN, "").apply()
                }
            })
        }
        val animFadeIn = AnimationUtils.loadAnimation(
            this,
            R.anim.fade_in
        )
        img_splash_screen.startAnimation(animFadeIn)


        Handler().postDelayed({
            val animFadeOut = AnimationUtils.loadAnimation(
                this,
                R.anim.fade_out
            )
            img_splash_screen.startAnimation(animFadeOut)
        }, waktuFadeOut.toLong())

        val account = GoogleSignIn.getLastSignedInAccount(this)

        Handler().postDelayed({
            //setelah loading maka akan langsung berpindah ke home activity
            if (account != null) {
                val home = Intent(this, MainActivity::class.java)
                startActivity(home)
                finish()
            } else {
                val home = Intent(this, MainActivity::class.java)
                startActivity(home)
                finish()
            }

        }, waktuLoading.toLong())
    }

    override fun onResume() {
        super.onResume()
        STATE_ACTIVITY = "splashScreen"
    }
    companion object {
        val ID_ADVERTISER = "id_advertiser"
        var STATE_ACTIVITY: String = "splashScreen"
        val EMAIL_ADVERTISER = "email_advertiser"
        val API_TOKEN = "api_token"
        val NAMA_ADVERTISER = "nama_advertiser"
        val TELP_ADVERTUSER = "telp_advertiser"
        val PRIVATE_MODE = 0
    }

}
