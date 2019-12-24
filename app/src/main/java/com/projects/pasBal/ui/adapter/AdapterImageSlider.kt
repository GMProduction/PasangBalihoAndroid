package com.projects.pasBal.ui.adapter

import android.content.Context
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.projects.pasBal.R
import com.projects.pasBal.data.network.api.URL_FOTO
import com.projects.pasBal.utils.ImageSlider

class AdapterImageSlider(
    val context: Context,
    private val imageModelArrayList: ArrayList<ImageSlider>
) : PagerAdapter() {
    private val inflater: LayoutInflater = LayoutInflater.from(context)

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount(): Int {
        return imageModelArrayList.size
    }

    override fun instantiateItem(view: ViewGroup, position: Int): Any {
        val imageLayout = inflater.inflate(R.layout.image_slider_layout, view, false)!!

        val imageView = imageLayout
            .findViewById(R.id.image) as ImageView


//        imageView.setImageResource(imageModelArrayList[position].getImageDrawable())


        val requestOptions = RequestOptions()
            .placeholder(R.color.backdrop)
            .error(R.mipmap.noimage)

        Glide.with(context)
            .applyDefaultRequestOptions(requestOptions)
            .load(URL_FOTO +imageModelArrayList[position].getImageDrawable())
            .into(imageView)

        view.addView(imageLayout, 0)

        return imageLayout
    }

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return view == `object`
    }

    override fun restoreState(state: Parcelable?, loader: ClassLoader?) {}

    override fun saveState(): Parcelable? {
        return null
    }

}