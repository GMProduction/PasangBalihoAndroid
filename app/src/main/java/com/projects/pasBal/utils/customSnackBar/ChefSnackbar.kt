package com.projects.pasBal.utils.customSnackBar

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.projects.pasBal.R

class ChefSnackbar(
    parent: ViewGroup,
    content: ChefSnackbarView
) : BaseTransientBottomBar<ChefSnackbar>(parent, content, content) {

    init {
        getView().setBackgroundColor(
            ContextCompat.getColor(
                view.context,
                R.color.frontdrop
            )
        )
        getView().setPadding(0, 0, 0, 0)
    }

    companion object {

        fun make(view: View, imgsrc: Int, tittle: String, body: String): ChefSnackbar {

            // First we find a suitable parent for our custom view
            val parent = view.findSuitableParent() ?: throw IllegalArgumentException(
                "No suitable parent found from the given view. Please provide a valid view."
            )

            // We inflate our custom view
            val customView = LayoutInflater.from(view.context).inflate(
                R.layout.layout_snackbar_chef,
                parent,
                false
            ) as ChefSnackbarView

            val textTittle = customView.findViewById<TextView>(R.id.tittle_notif)
            val textBody = customView.findViewById<TextView>(R.id.body_notif)
            val imgNotif = customView.findViewById<ImageView>(R.id.image_notif)
            textTittle.text = tittle
            textBody.text = body
            imgNotif.setImageResource(R.drawable.ic_notif)

            // We create and return our Snackbar
            return ChefSnackbar(
                parent,
                customView
            )
        }

    }

}