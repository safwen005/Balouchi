package com.example.balouchi.recycler

import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.viewpager.widget.PagerAdapter
import com.example.balouchi.data.repository.login.user.tools
import com.example.balouchi.images


class images_viewpager(var Activity: images, var list: ArrayList<String>) : PagerAdapter(){

    override fun isViewFromObject(view: View, `object`: Any): Boolean {
      return view==`object`
    }

    override fun instantiateItem(container: ViewGroup, position: Int): Any {
      val img=ImageView(container.context)
      tools().glide(Activity.application, list[position], img)
      container.addView(img)
      return img
    }

    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View)
    }

    override fun getCount()=list.size
}