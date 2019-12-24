package com.projects.pasBal.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.button.MaterialButton
import com.orhanobut.dialogplus.DialogPlus
import com.orhanobut.dialogplus.ViewHolder
import com.projects.pasBal.R

//Dialog
var dialog: DialogPlus? = null
private lateinit var textJudulDialog: TextView
private lateinit var textKonfirmasiDialog: TextView
private lateinit var btnBatalDialog: MaterialButton
private lateinit var btnKonfirmasiDialog: MaterialButton
private lateinit var imageDialog: ImageView

interface GenosDialog {

    fun dialogHandler(context: Context, judul: String, body: String, gambar: Int, type: String) {
        dialog = DialogPlus.newDialog(context)
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

        when (type) {
            "pesan" -> {
                btnBatalDialog.text = context.getString(R.string.ok)
                btnKonfirmasiDialog.visibility = View.GONE
            }
            "warning" -> {
                btnBatalDialog.text = context.getString(R.string.batal)
                btnKonfirmasiDialog.setOnClickListener {
                    dialog?.dismiss()
                    action()
                }
            }
            else -> {
                btnKonfirmasiDialog.visibility = View.GONE
                btnBatalDialog.visibility = View.GONE
            }
        }

        btnBatalDialog.setOnClickListener {
            dialog?.dismiss()
        }

        dialog?.show()
    }

    fun action()
}