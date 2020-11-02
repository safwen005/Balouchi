package com.example.balouchi.ui.home

import android.app.AlertDialog
import android.os.Build
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.appcompat.widget.SearchView
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.core.os.bundleOf
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.example.balouchi.R
import com.example.balouchi.all_settings
import com.example.balouchi.data.repository.login.user.product.product_data
import com.example.balouchi.ui.location.location
import com.example.balouchi.util.*
import com.facebook.Profile
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.InterstitialAd
import com.google.android.gms.ads.MobileAds
import kotlinx.android.synthetic.main.header.view.*
import kotlinx.android.synthetic.main.home.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap
import kotlin.time.ExperimentalTime
import kotlin.time.milliseconds


class Home_viewmodel : ViewModel() {

    lateinit var home: home
    lateinit var toolbar: Toolbar
    lateinit var NavController: NavController
    lateinit var header: View
    lateinit var dialog:AlertDialog
    var i=1
    var MenuItem:((MenuItem)->Boolean)?=MenuItem@{
        home.root.closeDrawer(GravityCompat.START)
        return@MenuItem true
    }

    @ExperimentalTime
    fun prepare() {
        home.apply {
            toolbar = (home.tool as Toolbar)
            toolbar_text("بلوشي")
            this@Home_viewmodel.NavController = Navigation.findNavController(this, R.id.frame)
            if (Build.VERSION.SDK_INT >= 21) {
                toolbar.elevation = 15f
            }
            dialog=getSpots()
            header = nav.getHeaderView(0)

            nav.menu.apply {
                findItem(R.id.logout).setOnMenuItemClickListener {
                    stopService(service)
                    saveuser("default", "")
                    auth.signOut()
                    finish()
                    return@setOnMenuItemClickListener true
                }

                findItem(R.id.setting).setOnMenuItemClickListener {
                    root.closeDrawer(GravityCompat.START)
                    go(all_settings::class)
                    running=false
                    return@setOnMenuItemClickListener true
                }

                findItem(R.id.profilee).setOnMenuItemClickListener {
                    root.closeDrawer(GravityCompat.START)
                    NavController.apply {
                        when (NavController.currentDestination?.getId()) {
                            R.id.profilee -> myprofile!!.apply { LoadInfo(); my_uid = null }
                            R.id.news -> navigate(R.id.action_news_to_profilee)
                            R.id.searchh -> navigate(R.id.action_searchh_to_profilee)
                            R.id.message -> navigate(R.id.action_message_to_profilee)
                            R.id.post2 -> navigate(R.id.action_post2_to_profilee)
                            R.id.complete_post -> navigate(R.id.action_complete_post_to_profilee)
                            R.id.product -> navigate(R.id.action_product_to_profilee)
                            R.id.all_categories -> navigate(R.id.action_all_categories_to_profilee)
                            R.id.specific_categorie -> navigate(R.id.action_specific_categorie_to_profilee)
                            R.id.conversation -> navigate(R.id.action_conversation_to_profilee)

                        }
                    }
                    return@setOnMenuItemClickListener true
                }

            }

            auth.currentUser!!.apply {
                header.apply {
                    background.alpha=30
                    displayName?.let {
                        fullname.text = it
                    }
                    photoUrl?.let {
                        if (isFacebook()) {
                            loaduri(
                                "https://graph.facebook.com/${
                                    Profile.getCurrentProfile()
                                        .getId()
                                }/picture?type=large&redirect=true&width=600&height=600",
                                profile_image
                            )
                            return@apply
                        }
                        loaduri(it, profile_image)
                        return@apply
                    }
                    profile_image.setBackgroundResource((R.drawable.ic_avatar))
                }
            }

            toolbar.apply {
                navigationIcon = svg(R.drawable.nav_menu)
                setNavigationOnClickListener {
                    home.root.apply {
                        if (isDrawerOpen(GravityCompat.START))
                            closeDrawer(GravityCompat.START)
                        else openDrawer(GravityCompat.START)
                    }
                }

                inflateMenu(R.menu.other_menu)
                setOnMenuItemClickListener {
                      if (it.itemId == R.id.location)
                        location().show(supportFragmentManager, null)
                    return@setOnMenuItemClickListener true

                }
                (menu.findItem(R.id.search).actionView as SearchView).apply {
                    maxWidth = Integer.MAX_VALUE

                      findViewById<EditText>(R.id.search_src_text)
                        .setTextColor(ContextCompat.getColor(context, R.color.white))


                    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                        override fun onQueryTextSubmit(query: String?): Boolean {
                            clearFocus()
                            query?.isEmpty()?.let {
                                if (it) {
                                    toastr("لاينبغي ان يكون فارغا !")
                                    return true
                                }
                                dialog.show()
                                home.manageUser.Function<ArrayList<HashMap<*, *>>>(
                                    "search",
                                    hashMapOf("name" to query,"location" to false)
                                ).observe(home, {
                                    dialog.dismiss()
                                    val list = ArrayList<product_data?>()
                                    it.forEach {
                                        list.add(fromJson(it, product_data::class))
                                    }
                                    if (list.size > 0) {
                                        NavController.apply {
                                            when (NavController.currentDestination?.getId()) {
                                                R.id.profilee -> navigate(
                                                    R.id.action_profilee_to_specific_categorie,
                                                    bundleOf(
                                                        "products" to list
                                                    )
                                                )
                                                R.id.news -> navigate(
                                                    R.id.action_news_to_specific_categorie,
                                                    bundleOf(
                                                        "products" to list
                                                    )
                                                )
                                                R.id.searchh -> navigate(
                                                    R.id.action_searchh_to_specific_categorie,
                                                    bundleOf(
                                                        "products" to list
                                                    )
                                                )
                                                R.id.message -> navigate(
                                                    R.id.action_message_to_specific_categorie,
                                                    bundleOf(
                                                        "products" to list
                                                    )
                                                )
                                                R.id.post2 -> navigate(
                                                    R.id.action_post2_to_specific_categorie,
                                                    bundleOf(
                                                        "products" to list
                                                    )
                                                )
                                                R.id.complete_post -> navigate(
                                                    R.id.action_complete_post_to_specific_categorie,
                                                    bundleOf(
                                                        "products" to list
                                                    )
                                                )
                                                R.id.product -> navigate(
                                                    R.id.action_product_to_specific_categorie,
                                                    bundleOf(
                                                        "products" to list
                                                    )
                                                )
                                                R.id.specific_categorie -> specific?.search(list)
                                                R.id.conversation -> navigate(R.id.action_conversation_to_specific_categorie,
                                                    bundleOf(
                                                        "products" to list
                                                    ))
                                            }
                                        }
                                        onActionViewCollapsed()
                                        return@observe
                                    }
                                    toastr("لا توجد نتائج !")
                                })
                                return true
                            }
                            toast("هنالك مشكلة !")

                            return true
                        }

                        override fun onQueryTextChange(newText: String?): Boolean {
                            newText?.let {
                                if (it.length > 30)
                                    setQuery(it.substring(0, 30), false)
                            }
                            return true
                        }
                    })
                }
            }
            NavigationUI.setupWithNavController(nav, this@Home_viewmodel.NavController)
        }
    }

    fun menu(listener:Boolean){
        home.nav.menu.findItem(R.id.message).setOnMenuItemClickListener(if (listener) MenuItem else null)
    }


    fun navigateUp(): Boolean = NavigationUI.navigateUp(NavController, home.root)

    }


