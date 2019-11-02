package com.genossys.pasangbaliho.ui.detail.maps

import android.annotation.SuppressLint
import android.os.Bundle
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.genossys.pasangbaliho.R
import kotlinx.android.synthetic.main.activity_street_web_view.*


class StreetWebViewActivity : AppCompatActivity() {
    var mywebview: WebView? = null
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_street_web_view)

        mywebview = findViewById(R.id.webview)
        mywebview!!.settings.javaScriptEnabled = true
        mywebview!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                view?.loadUrl(url)
                return true
            }
        }
        val streetWeb = intent.getStringExtra("streetView")
        text_nama.text = intent.getStringExtra("alamat")

        mywebview!!.loadUrl(streetWeb)
        button_back.setOnClickListener {
            finish()
        }
    }


}
