package com.example.balouchi.util

import android.content.Context
import android.graphics.BlendMode
import android.graphics.BlendModeColorFilter
import android.graphics.PorterDuff
import android.os.Build
import android.transition.Slide
import android.view.Gravity
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import com.example.balouchi.R
import com.example.balouchi.ui.home.home
import com.google.android.material.tabs.TabLayout
import com.google.android.material.textfield.TextInputEditText

fun Context.visibility(v1:TextInputEditText,v: View, t:Boolean) {
    v.apply {
        if (t) {
            if (v1.text.toString() == "") {
                animation = AnimationUtils.loadAnimation(this@visibility,
                    R.anim.hide
                )
                visibility = View.INVISIBLE
            }
            v1.animation = AnimationUtils.loadAnimation(this@visibility,
                R.anim.show_edittext_background
            )
            v1.setBackgroundResource(R.drawable.layout_edittext_blue)
            return
        }
        if (v1.text.toString() == "") {
            animation = AnimationUtils.loadAnimation(this@visibility,
                R.anim.show
            )
            visibility = View.VISIBLE
        }
            v1.animation = AnimationUtils.loadAnimation(this@visibility,
                R.anim.show_edittext_background
            )
            v1.setBackgroundResource(R.drawable.edit_gray)
        }
}
fun Context.font(vararg v: TextView,id:Int= R.font.et){
    v.forEach {
        it.typeface= ResourcesCompat.getFont(this,id)
    }
}



fun Context.changecolor(tab: TabLayout.Tab?, color:Int){
    val tabIconColor = ContextCompat.getColor(this, color)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
        tab?.icon?.setColorFilter(BlendModeColorFilter(tabIconColor, BlendMode.SRC_ATOP))
    } else {
        tab?.icon?.setColorFilter(tabIconColor, PorterDuff.Mode.SRC_ATOP)
    }
}
