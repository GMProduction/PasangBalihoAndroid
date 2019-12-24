package com.projects.pasBal.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.projects.pasBal.data.db.entity.PageNews
import com.projects.pasBal.data.db.repository.NewsRepository
import com.projects.pasBal.utils.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.withContext
import java.net.SocketTimeoutException

class NewsViewModel(
    private val newsRepository: NewsRepository
) : ViewModel(

) {

    var commonListener: CommonListener? = null
    lateinit var job: Job

    suspend fun getDataNews(page: Int, awal: Boolean): MutableLiveData<PageNews> {

        if (awal) {
            commonListener?.onStarted()
        } else {
            commonListener?.onStartJob()
        }
        val news = MutableLiveData<PageNews>()
        job = Job()
        job = Coroutines.io {
            try {
                val newsResponse = newsRepository.getDataNews(page)
                newsResponse.data.let {
                    news.postValue(it)
                    withContext(Dispatchers.Main) {
                        if (it.news.isEmpty()) {
                            withContext(Dispatchers.Main) {
                                commonListener?.onEmpty()
                            }
                        } else {
                            withContext(Dispatchers.Main) {
                                commonListener?.onSuccess()
                            }
                        }
                    }
                }
            } catch (e: ApiException) {
                commonListener?.onFailure(ERROR_API)
                job.cancel()
            } catch (e: NoInternetException) {
                commonListener?.onFailure(ERROR_INTERNET)
                job.cancel()
            } catch (e: SocketTimeoutException) {
                commonListener?.onTimeOut("Timeout")
                job.cancel()
            }

        }
        return news
    }
}