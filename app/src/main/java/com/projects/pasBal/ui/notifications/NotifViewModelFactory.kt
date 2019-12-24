package com.projects.pasBal.ui.notifications

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.projects.pasBal.data.db.repository.NotifRepository

class NotifViewModelFactory(
    private val notifRepository: NotifRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return NotificationsViewModel(notifRepository) as T
    }
}