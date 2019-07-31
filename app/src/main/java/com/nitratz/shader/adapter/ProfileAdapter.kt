package com.nitratz.shader.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.nitratz.shader.model.profile.UserResult
import androidx.viewpager.widget.ViewPager
import com.nitratz.shader.R


class ProfileAdapter(private val mContext: Context, private val mProfiles: List<UserResult>) : PagerAdapter() {

    override fun instantiateItem(collection: ViewGroup, position: Int): Any {
        val profile = mProfiles[position].user
        val inflater = LayoutInflater.from(mContext)
        val layout = inflater.inflate(R.layout.item_profile_viewpager, collection, false) as ViewGroup
        val viewPager = layout.findViewById<ViewPager>(R.id.viewPager)

        val photosAdapter = PhotosAdapter(mContext, profile.photos)
        viewPager.adapter = photosAdapter

        collection.addView(layout)
        return layout
    }

    override fun isViewFromObject(view: View, obj: Any): Boolean = view === obj

    override fun getCount(): Int = mProfiles.size

    override fun destroyItem(collection: ViewGroup, position: Int, view: Any) {
        collection.removeView(view as View)
    }
}