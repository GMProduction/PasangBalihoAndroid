package com.genossys.pasangbaliho.ui.transaksi.menuTransaksi


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.genossys.pasangbaliho.R
import com.genossys.pasangbaliho.utils.CommonListener
import com.genossys.pasangbaliho.utils.Coroutines
import kotlinx.android.synthetic.main.fragment_menu_transaksi.view.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein

/**
 * A simple [Fragment] subclass.
 */
class MenuTransaksiFragment(isi: String) : Fragment(), KodeinAware, CommonListener {

    override val kodein by kodein()
    lateinit var root: View
    val isi = isi
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        root = inflater.inflate(R.layout.fragment_menu_transaksi, container, false)

        Coroutines.main {

        }

        root.text_kosong.text = isi
        return root
    }

    override fun onStarted() {
    }

    override fun onSuccess() {
    }

    override fun onStartJob() {
    }

    override fun onSuccessJob() {
    }

    override fun onTimeOut(message: String) {
    }

    override fun onFailure(message: String) {
    }
}
