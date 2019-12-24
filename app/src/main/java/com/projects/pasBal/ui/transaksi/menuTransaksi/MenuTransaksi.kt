package com.projects.pasBal.ui.transaksi.menuTransaksi

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.viewpager.widget.ViewPager
import com.projects.pasBal.R
import com.projects.pasBal.ui.MainActivity
import com.projects.pasBal.utils.customSnackBar.ChefSnackbar
import com.projects.pasBal.utils.firebaseServices.MyFirebaseMessagingService
import kotlinx.android.synthetic.main.activity_menu_transaksi.*

class MenuTransaksi : AppCompatActivity() {

    lateinit var root: ViewPager

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            val tittle: String? = intent?.getStringExtra("tittle")
            val body: String? = intent?.getStringExtra("body")

            ChefSnackbar.make(root, R.mipmap.boyolali, tittle!!, body!!).show()

        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_transaksi)

        initComponent()
        LocalBroadcastManager.getInstance(this)
            .registerReceiver(
                broadCastReceiver,
                IntentFilter(MyFirebaseMessagingService.NOTIF_TRANSAKSI)
            )

        val fragmentAdapter = AdapterMenuTransaksi(supportFragmentManager)
        pager_transaksi.adapter = fragmentAdapter

        tab_transaksi.setupWithViewPager(pager_transaksi)
    }

    private fun initComponent(){
    root = findViewById(R.id.pager_transaksi)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        LocalBroadcastManager.getInstance(this)
            .unregisterReceiver(broadCastReceiver)
        Intent(this, MainActivity::class.java).also {
            it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(it)
        }
    }



}
