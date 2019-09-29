package com.genossys.pasangbaliho.ui.sign_in


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.genossys.pasangbaliho.R
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.android.synthetic.main.fragment_sign_in.view.*

/**
 * A simple [Fragment] subclass.
 */
class SignInFragment : Fragment() {


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view: View = inflater.inflate(R.layout.fragment_sign_in, container, false)

        view.button_sign_in.setOnClickListener {
            Log.d("sign in click", "try to login")

            val email = edit_email.text.toString().trim()
            val password = edit_password.text.toString().trim()

            if (email.isEmpty()) {
                edit_email.error = "email required"
                edit_email.requestFocus()
                return@setOnClickListener
            }

            if (password.isEmpty()) {
                edit_password.error = "password required"
                edit_password.requestFocus()
                return@setOnClickListener
            }

        }

        return view
    }


}
