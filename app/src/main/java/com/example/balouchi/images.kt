package com.example.balouchi

import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.balouchi.recycler.images_viewpager
import kotlinx.android.synthetic.main.images.*

class images : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.images)

        if (Build.VERSION.SDK_INT>=21) {
            view_pager.transitionName="img"
        }

        view_pager.adapter=images_viewpager(this,intent.getStringArrayListExtra("pictures"))
        tab_layout.setupWithViewPager(view_pager,true)
    }

}