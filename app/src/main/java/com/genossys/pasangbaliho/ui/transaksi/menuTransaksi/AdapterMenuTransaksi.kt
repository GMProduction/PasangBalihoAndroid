package com.genossys.pasangbaliho.ui.transaksi.menuTransaksi

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class AdapterMenuTransaksi(fm: FragmentManager) :
    FragmentPagerAdapter(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {
    override fun getItem(position: Int): Fragment {
        return when (position) {
            0 -> MenuTransaksiFragment("permintaan")
            1 -> MenuTransaksiFragment("negoharga")
            2 -> MenuTransaksiFragment("negomateri")
            3 -> MenuTransaksiFragment("pembayaran")
            else -> {
                return MenuTransaksiFragment("selesai")
            }
        }
    }

    override fun getCount(): Int {
        return 5
    }

    override fun getPageTitle(position: Int): CharSequence {
        return when (position) {
            0 -> "Permintaan Harga"
            1 -> "Negosiasi Harga"
            2 -> "Negosiasi materi"
            3 -> "pembayaran"
            else -> {
                return "Selesai"
            }
        }
    }
}