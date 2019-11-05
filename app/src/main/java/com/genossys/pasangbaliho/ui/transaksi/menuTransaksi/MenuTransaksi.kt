package com.genossys.pasangbaliho.ui.transaksi.menuTransaksi

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.genossys.pasangbaliho.R
import kotlinx.android.synthetic.main.activity_menu_transaksi.*

class MenuTransaksi : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu_transaksi)

        val fragmentAdapter = AdapterMenuTransaksi(supportFragmentManager)
        pager_transaksi.adapter = fragmentAdapter

        tab_transaksi.setupWithViewPager(pager_transaksi)
    }
}
