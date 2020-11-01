package com.example.balouchi


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager.widget.ViewPager
import com.example.balouchi.recycler.ViewPagerAdapter
import com.example.balouchi.ui.chat.chat
import com.example.balouchi.ui.home.home
import com.example.balouchi.ui.notification.notification
import com.example.balouchi.util.changecolor
import com.example.balouchi.util.log
import com.example.balouchi.util.save_chat
import com.example.balouchi.util.toolbar_text
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.messages.*
import kotlinx.android.synthetic.main.messages.view_pager
import kotlin.time.ExperimentalTime


class messages : Fragment() {

   lateinit var v:View

   lateinit var mynotification:notification

   lateinit var mychat:chat

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         v=inflater.inflate(R.layout.messages,container,false)
         return v
    }

    fun prepare(){
        mychat= chat()
        mynotification= notification()
    }


    @ExperimentalTime
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepare()
        (requireActivity() as home).view.menu(true)
        requireActivity().apply {
            toolbar_text(" الرسائل و الاشعارات ")
            v.apply {
                tab.apply {
                    tab.setupWithViewPager(view_pager)
                    view_pager.adapter = ViewPagerAdapter(childFragmentManager, arrayOf(mychat, mynotification))
                    getTabAt(0)?.setIcon(R.drawable.nav_message)
                    getTabAt(1)?.setIcon(R.drawable.ic_notification)
                    changecolor(getTabAt(1), R.color.blue)
                    changecolor(getTabAt(0), R.color.white)
                    getTabAt(0)!!.text = "الرسائل"
                    getTabAt(1)!!.text = "الأشعارات"
                    getTabAt(1)!!.select()


                    addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                        override fun onTabReselected(tab: TabLayout.Tab?) {
                        }
                        override fun onTabSelected(tab: TabLayout.Tab?) {
                            changecolor(tab, R.color.blue)
                            if (tab?.position==1){
                                mynotification.retry()
                                return
                            }
                            mychat.retry()
                        }
                        override fun onTabUnselected(tab: TabLayout.Tab?) {
                            changecolor(tab, R.color.white)
                        }
                    })

                }
            }
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        (requireActivity() as home).view.menu(false)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setRetainInstance(true)
    }

}