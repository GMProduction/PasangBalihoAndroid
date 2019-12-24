package com.projects.pasBal.ui.news

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.projects.pasBal.R
import com.projects.pasBal.data.db.entity.News
import com.projects.pasBal.ui.adapter.AdapterNews
import com.projects.pasBal.ui.splashScreen.SplashScreen
import com.projects.pasBal.utils.*
import com.projects.pasBal.utils.customSnackBar.ChefSnackbar
import com.projects.pasBal.utils.firebaseServices.MyFirebaseMessagingService
import com.wang.avi.AVLoadingIndicatorView
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance

class NewsFragment : CommonListener, KodeinAware, Fragment(), GenosDialog {


    override val kodein by kodein()
    private val factory: NewsViewModelFactory by instance()
    private lateinit var newsAdapter: AdapterNews
    private lateinit var newsViewModel: NewsViewModel
    private lateinit var root: View

    var listNews: MutableList<News> = mutableListOf()

    private var page: Int = 1
    private var totalPage: Int = 0
    private var readyToLoad = false
    private var btnReloadReady = false
    private var isLoadAwalOk = false

    private lateinit var recycleViewNews: RecyclerView
    private lateinit var layoutKosong: ConstraintLayout
    private lateinit var btnReload: ImageView
    private lateinit var cardLoading: CardView
    private lateinit var loadingMid: AVLoadingIndicatorView
    private lateinit var reload: ImageView

    private val broadCastReceiver = object : BroadcastReceiver() {
        override fun onReceive(contxt: Context?, intent: Intent?) {

            val tittle: String? = intent?.getStringExtra("tittle")
            val body: String? = intent?.getStringExtra("body")

            ChefSnackbar.make(root, R.mipmap.boyolali, tittle!!, body!!).show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //ViewModel
        newsViewModel =
            ViewModelProviders.of(this, factory).get(NewsViewModel::class.java)
        newsViewModel.commonListener = this

        root = inflater.inflate(R.layout.fragment_news, container, false)

        initComponent()
        loadDataAwal()
        initButton()
        recycleViewOnBottom()
        return root
    }

    private fun initComponent() {
        recycleViewNews = root.findViewById(R.id.recycle_view_news)
        btnReload = root.findViewById(R.id.reload)
        cardLoading = root.findViewById(R.id.card_loading)
        loadingMid = root.findViewById(R.id.progress_loading_mid)
        layoutKosong = root.findViewById(R.id.layout_kosong)
        reload = root.findViewById(R.id.reload)

        layoutKosong.visibility = View.GONE
        recycleViewNews.visibility = View.GONE
        cardLoading.visibility = View.GONE
    }

    private fun initButton() {
        btnReload.setOnClickListener {
            if (btnReloadReady) {
                if (isLoadAwalOk) {
                    Coroutines.main {
                        val balihos =
                            newsViewModel.getDataNews(
                                page + 1,
                                false
                            )
                        balihos.observe(this@NewsFragment, Observer {
                            for (i in it.news) {
                                listNews.add(i!!)
                            }
                            newsAdapter.sumitList(listNews)
                            page = it.currentPage!!
                        })
                    }
                } else {
                    Coroutines.main {
                        val balihos =
                            newsViewModel.getDataNews(1, false)
                        balihos.observe(this@NewsFragment, Observer {
                            for (i in it.news) {
                                listNews.add(i!!)
                            }
                            newsAdapter.sumitList(listNews)
                            page = it.currentPage!!
                            totalPage = it.lastPage!!
                            isLoadAwalOk = true
                        })
                    }
                }
            }

        }
    }

    private fun loadDataAwal() {
        Coroutines.main {
            newsViewModel.getDataNews(page, true).observe(this, Observer {
                listNews.clear()
                initRecycleView()

                for (i in it.news) {
                    listNews.add(i!!)
                }

                newsAdapter.sumitList(listNews)
                page = it.currentPage!!
                totalPage = it.lastPage!!

                isLoadAwalOk = true
            })
        }
    }

    private fun initRecycleView() {
        recycleViewNews.apply {
            layoutManager = LinearLayoutManager(activity?.applicationContext)
            newsAdapter = AdapterNews()
            adapter = newsAdapter
            isNestedScrollingEnabled = false
        }
    }

    private fun recycleViewOnBottom() {
        recycleViewNews.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    if (page != totalPage) {
                        if (readyToLoad) {
                            readyToLoad = false
                            Coroutines.main {
                                newsViewModel.getDataNews(page + 1, false)
                                    .observe(this@NewsFragment, Observer {
                                        for (i in it.news) {
                                            listNews.add(i!!)
                                        }
                                        newsAdapter.sumitList(listNews)
                                        page = it.currentPage!!
                                    })
                            }
                        }
                    }
                }
            }
        })
    }


    override fun onStarted() {
        Coroutines.main {
            layoutKosong.visibility = View.GONE
            cardLoading.visibility = View.VISIBLE
            loadingMid.visibility = View.VISIBLE
            btnReload.visibility = View.GONE
            recycleViewNews.visibility = View.GONE
        }
    }

    override fun onSuccess() {
        readyToLoad = true
        Coroutines.main {
            layoutKosong.visibility = View.GONE
            cardLoading.visibility = View.GONE
            loadingMid.visibility = View.GONE
            btnReload.visibility = View.GONE
            recycleViewNews.visibility = View.VISIBLE
        }
    }

    override fun onFailure(message: String) {
        btnReloadReady = true
        Coroutines.main {
            layoutKosong.visibility = View.GONE
            cardLoading.visibility = View.VISIBLE
            loadingMid.visibility = View.GONE
            btnReload.visibility = View.VISIBLE
            recycleViewNews.visibility = View.GONE

            if(message == ERROR_INTERNET){
                dialogHandler(activity!!, "Internet Error", ERROR_INTERNET, R.drawable.ic_nointernet, "pesan")
            }else{
                dialogHandler(activity!!, "Error", ERROR_API, R.drawable.ic_nointernet, "pesan")
            }
        }
    }

    override fun onTimeOut(message: String) {
        btnReloadReady = true
        Coroutines.main {
            layoutKosong.visibility = View.GONE
            cardLoading.visibility = View.VISIBLE
            loadingMid.visibility = View.GONE
            btnReload.visibility = View.VISIBLE
            recycleViewNews.visibility = View.GONE

            dialogHandler(activity!!, "Time Out", ERROR_INTERNET, R.drawable.ic_nointernet, "pesan")
        }
    }

    override fun onStartJob() {
        btnReloadReady = false
        Coroutines.main {
            layoutKosong.visibility = View.GONE
            cardLoading.visibility = View.VISIBLE
            loadingMid.visibility = View.VISIBLE
            btnReload.visibility = View.GONE
            recycleViewNews.visibility = View.VISIBLE
        }
    }

    override fun onSuccessJob() {
    }

    override fun onEmpty() {
        Coroutines.main {
            layoutKosong.visibility = View.VISIBLE
            cardLoading.visibility = View.GONE
            loadingMid.visibility = View.GONE
            btnReload.visibility = View.GONE
            recycleViewNews.visibility = View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        appContext = this.activity!!
        SplashScreen.STATE_ACTIVITY = "NewsFragment"

        LocalBroadcastManager.getInstance(activity?.applicationContext!!)
            .registerReceiver(
                broadCastReceiver,
                IntentFilter(MyFirebaseMessagingService.NOTIF_TRANSAKSI)
            )

    }

    override fun onPause() {
        super.onPause()
        LocalBroadcastManager.getInstance(activity?.applicationContext!!)
            .unregisterReceiver(broadCastReceiver)

    }

    companion object {

        var appContext: Context? = null

    }

    override fun action() {

    }
}