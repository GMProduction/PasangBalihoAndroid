package com.genossys.pasangbaliho.ui.news

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.genossys.pasangbaliho.data.db.entity.PageNews
import com.genossys.pasangbaliho.data.db.repository.NewsRepository
import com.genossys.pasangbaliho.utils.ApiException
import com.genossys.pasangbaliho.utils.CommonListener
import com.genossys.pasangbaliho.utils.Coroutines
import com.genossys.pasangbaliho.utils.NoInternetException
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
                commonListener?.onFailure(e.message!!)
                job.cancel()
            } catch (e: NoInternetException) {
                commonListener?.onFailure(e.message!!)
                job.cancel()
            } catch (e: SocketTimeoutException) {
                commonListener?.onTimeOut("Timeout")
                job.cancel()
            }

        }
        return news
    }
}