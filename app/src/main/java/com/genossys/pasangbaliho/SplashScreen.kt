package com.genossys.pasangbaliho

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.transition.Explode
import android.transition.Fade
import android.view.Window
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat.startActivity
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


        Handler().postDelayed({
            //setelah loading maka akan langsung berpindah ke home activity
            val home = Intent(this, MainActivity::class.java)
            startActivity(home)
            finish()
        }, waktuLoading.toLong())
    }


}
