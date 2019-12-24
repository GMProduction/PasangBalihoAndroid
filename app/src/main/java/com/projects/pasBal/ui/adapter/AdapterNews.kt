package com.projects.pasBal.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.projects.pasBal.R
import com.projects.pasBal.data.db.entity.News
import com.projects.pasBal.data.network.api.URL_FOTO
import com.projects.pasBal.ui.detail.DetailBalihoActivity
import com.projects.pasBal.utils.tglSystemToView
import kotlinx.android.synthetic.main.temp_news.view.*

class AdapterNews : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<News> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return NewsViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.temp_news, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        when (holder) {
            is NewsViewHolder -> {
                holder.bind(items[position])
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun sumitList(newsList: List<News>) {
        items = newsList
    }

    class NewsViewHolder constructor(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val newsImage: ImageView = itemView.image_news
        private val newsTittle: TextView = itemView.text_tittle_news
        private val newsDeskripsi: TextView = itemView.text_isi_news
        private val buttonDetail: TextView = itemView.button_news
        private val newsCreatedAt: TextView = itemView.text_created_at

        fun bind(news: News) {

            val requestOptions = RequestOptions()
                .placeholder(R.color.backdrop)
                .error(R.color.backdrop)

            Glide.with(itemView.context)
                .applyDefaultRequestOptions(requestOptions)
                .load(URL_FOTO + news.gambar)
                .into(newsImage)

            newsTittle.text = news.judul
            newsDeskripsi.text = news.isi
            newsCreatedAt.text = tglSystemToView(news.createdAt!!)

            buttonDetail.setOnClickListener {
                val i = Intent(itemView.context, DetailBalihoActivity::class.java)
                i.putExtra("id", news.idNews)
                itemView.context.startActivity(i)
            }
        }
    }

}
