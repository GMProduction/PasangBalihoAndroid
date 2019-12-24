package com.projects.pasBal.ui.transaksi.menuTransaksi

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AdapterMenuTransaksi(fm: FragmentManager) :
    FragmentPagerAdapter(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MenuTransaksiFragment("permintaan")
            1 -> MenuTransaksiFragment("negoharga")
            2 -> MenuTransaksiFragment("pembayaran")
            3 -> MenuTransaksiFragment("negomateri")
            4 -> MenuTransaksiFragment("selesai")
            else -> {
                return MenuTransaksiFragment("batal")
            }
        }
    }

    override fun getCount(): Int {
        return 6
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Permintaan Harga"
            1 -> "Negosiasi Harga"
            2 -> "Negosiasi materi"
            3 -> "pembayaran"
            4 -> "selesai"
            else -> {
                return "batal"
            }
        }
    }
}