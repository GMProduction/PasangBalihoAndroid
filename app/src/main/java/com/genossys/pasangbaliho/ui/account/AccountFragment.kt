package com.genossys.pasangbaliho.ui.account

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.genossys.pasangbaliho.MainActivity
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.databinding.FragmentAccountBinding
import com.genossys.pasangbaliho.ui.sign_in.SignInActivity
import com.genossys.pasangbaliho.ui.sign_in.googleAuth.GsoBuilder
import com.genossys.pasangbaliho.utils.toast
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import kotlinx.android.synthetic.main.fragment_account.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class AccountFragment : Fragment(), KodeinAware {

    override val kodein by kodein()
    private val factory: AccountViewModelFactory by instance()

    private var mGoogleSignInClient: GoogleSignInClient? = null

    private lateinit var accountViewModel: AccountViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mGoogleSignInClient = context?.let { GsoBuilder.getGoogleSignInClient(it) }
        val binding: FragmentAccountBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_account, container, false)
        accountViewModel = ViewModelProviders.of(this, factory).get(AccountViewModel::class.java)
        binding.accountViewModel = accountViewModel
        binding.lifecycleOwner = this

        accountViewModel.getLoggedInAdvertiser().observe(this, Observer { advertiser ->
            Log.d("cek login", "advertiser $advertiser")

            if (advertiser != null) {
                binding.root.layout_login_accuont.visibility = View.GONE
            }

        })

        binding.root.button_sign_in.setOnClickListener {
            val signIn = Intent(activity, SignInActivity::class.java)
            startActivity(signIn)
        }

        binding.root.layout_sign_out.setOnClickListener {

            mGoogleSignInClient!!.signOut()

            accountViewModel.deleteAdvertiser()
            Intent(activity, MainActivity::class.java).also {
                it.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(it)
                activity?.toast("anda berhasil keluar")
            }


        }

        return binding.root
    }


}