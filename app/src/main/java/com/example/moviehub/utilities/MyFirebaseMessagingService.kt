package com.example.moviehub.utilities

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import com.example.moviehub.R
import com.example.moviehub.activities.SplashActivity
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage


class MyFirebaseMessagingService : FirebaseMessagingService(){
    private fun generateNotification(title:String, message:String){
        val intent= Intent(this,SplashActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent=PendingIntent.getActivity(this,0,intent,
            PendingIntent.FLAG_ONE_SHOT or PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
           applicationContext, Constants.CHANNEL_ID)
            .setSmallIcon(R.drawable.logo)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000, 1000, 1000, 1000))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)
        
        builder = builder.setContent(
            getCustomDesign(title, message)
        )

        val notificationManager=getSystemService(Context.NOTIFICATION_SERVICE)as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH)
            notificationManager.createNotificationChannel(notificationChannel)
        }
        notificationManager.notify(0, builder.build())
        
    }

    private fun getCustomDesign(title: String, message: String): RemoteViews {
        val remoteView=RemoteViews(Constants.CHANNEL_NAME,R.layout.notification)
        remoteView.setTextViewText(R.id.notification_title,title)
        remoteView.setTextViewText(R.id.notification_mess,message)
        remoteView.setImageViewResource(R.id.notification_logo,R.drawable.logo)

        return remoteView
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.getNotification() != null) {
           generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        }
    }
}