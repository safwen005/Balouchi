package com.example.balouchi.ui.login

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.balouchi.util.getvisibility
import com.example.balouchi.util.go
import com.example.balouchi.util.isInternetAvailable
import com.example.balouchi.util.setvisibility
import java.lang.Exception
import javax.inject.Inject
import javax.inject.Singleton


class NoInternetBroad @Inject constructor() : BroadcastReceiver() {


    override fun onReceive(context: Context?, intent: Intent?) {

            context!!.apply {
                if (!isInternetAvailable(this) && (getvisibility())) {
                    setvisibility(false)
                    go(NoInternet::class)
                }

                if (isInternetAvailable(this) && !(getvisibility())) {
                    if (this is NoInternet) {
                        (this as InternetListenner).Listen()
                        setvisibility(true)
                    }
                }

            }

        }


    }
