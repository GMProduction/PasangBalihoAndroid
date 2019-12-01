package com.genossys.pasangbaliho.ui.auth.googleAuth

import android.content.Context
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

object GsoBuilder {

    fun getGoogleSignInClient(context: Context) : GoogleSignInClient{

        var mGoogleSignInClient: GoogleSignInClient? = null

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestEmail()
            .build()

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso)

        return  mGoogleSignInClient

    }


}