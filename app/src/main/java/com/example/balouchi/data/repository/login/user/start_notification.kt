package com.example.balouchi.data.repository.login.user

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.AudioAttributes
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.balouchi.R
import com.example.balouchi.ui.home.home


class start_notification(var context: Context){

    lateinit var notificationChannel:NotificationChannel
    lateinit var notificationManager:NotificationManager
    lateinit var intent:Intent
    lateinit var pendingIntent:PendingIntent
    lateinit var notificationCompatbuilder:NotificationCompat.Builder

    fun notification_channel(){
        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            notificationChannel=NotificationChannel(
                "balouchi",
                "balouchi",
                NotificationManager.IMPORTANCE_HIGH
            )
            notificationChannel.apply {
                description="balouchi"
                setShowBadge(true)
            }
            notificationManager=context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }
    fun start_notification(title: String, text: String, info:String){

        notification_channel()

        intent=Intent(context, home::class.java)

        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)

        intent.putExtra("info",info)
        pendingIntent= PendingIntent.getActivities(
            context,
            22,
            arrayOf(intent),
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        notificationCompatbuilder=NotificationCompat.Builder(context, "balouchi")
            .setSmallIcon(R.drawable.ic_face_smile)
            .setLargeIcon(BitmapFactory.decodeResource(context.resources, R.drawable.ic_avatar))
            .setContentTitle(title)
            .setContentText(text)
            .setStyle(NotificationCompat.BigTextStyle().bigText(text))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setChannelId("balouchi")
            .setAutoCancel(true)

        if (Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            val attributes = AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_NOTIFICATION)
                .build()
            notificationChannel.setSound(
                Uri.parse(
                    "android.resource://"
                            + context.getPackageName() + "/"
                            + R.raw.juntos
                ), attributes
            )
        }
        else {
            notificationCompatbuilder.setSound(
                Uri.parse(
                    "android.resource://"
                            + context.getPackageName() + "/"
                            + R.raw.juntos
                )
            )
        }

        NotificationManagerCompat.from(context).notify(2, notificationCompatbuilder.build())

    }
}
