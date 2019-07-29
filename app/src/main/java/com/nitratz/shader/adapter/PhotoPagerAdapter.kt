package com.nitratz.shader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.bumptech.glide.Glide
import com.nitratz.shader.model.profile.Photo
import com.nitratz.shader.R
import com.nitratz.shader.RestClient


class PhotoPagerAdapter(private val mContext: Context, private val mPhotos: List<Photo>) : PagerAdapter() {

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.item_photo_profile, container, false) as ViewGroup
        val photoIv: ImageView = layout.findViewById(R.id.photo)

        Glide.with(mContext).load(RestClient.getUrl(mPhotos[position].url)).into(photoIv)

        container.addView(layout)
        return layout
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean = obj == view

    override fun getCount(): Int = mPhotos.size

    override fun destroyItem(container: ViewGroup, position: Int, view: Any) {
        container.removeView(view as View)
    }

}