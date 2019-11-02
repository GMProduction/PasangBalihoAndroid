package com.genossys.pasangbaliho

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AppCompatActivity
import com.genossys.pasangbaliho.ui.sign_in.SignInActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_splash_screen.*


class SplashScreen : AppCompatActivity() {

    private val waktuLoading = 1200
    private val waktuFadeOut = 1000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val animFadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        img_splash_screen.startAnimation(animFadeIn)


        Handler().postDelayed({
            val animFadeOut = AnimationUtils.loadAnimation(this, R.anim.fade_out)
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


}
