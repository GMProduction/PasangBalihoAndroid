package com.projects.pasBal.ui.adapter

import android.content.Context
import android.content.Intent
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.projects.pasBal.R
import com.projects.pasBal.data.db.entity.Slider
import com.projects.pasBal.data.network.api.URL_SLIDER
import com.projects.pasBal.ui.link.Link

class AdapterSlider(
    val context: Context,
    val sliderList: List<Slider>
) : PagerAdapter() {

    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return sliderList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val sliderLayout = inflater.inflate(R.layout.image_slider_home, view, false)!!

        val imageView = sliderLayout
            .findViewById<ImageView>(R.id.image)
        val textTittle = sliderLayout.findViewById<TextView>(R.id.text_tittle)
        val textDeskripsi = sliderLayout.findViewById<TextView>(R.id.text_deskripsi)

//        imageView.setImageResource(sliderArrayList[position].getImageDrawable())


        val requestOptions = RequestOptions()
            .placeholder(R.color.backdrop)
            .error(R.mipmap.noimage)

        Glide.with(context)
            .applyDefaultRequestOptions(requestOptions)
            .load(URL_SLIDER + sliderList[position].urlFoto)
            .into(imageView)

        textTittle.text = sliderList[position].title
        textDeskripsi.text = sliderList[position].deskripsi

        sliderLayout.setOnClickListener {
            val i = Intent(context, Link::class.java)
            i.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            i.putExtra("tittle", sliderList[position].title)
            i.putExtra("link", sliderList[position].link)
            context.startActivity(i)
        }
        view.addView(sliderLayout, 0)

        return sliderLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}