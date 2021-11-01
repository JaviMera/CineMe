package com.merajavier.cineme

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.PendingIntent.FLAG_UPDATE_CURRENT
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.TimeUtils
import androidx.core.app.NotificationCompat
import androidx.paging.ExperimentalPagingApi
import com.merajavier.cineme.movies.upcoming.UpcomingMoviesActivity
import java.util.*

private const val NOTIFICATION_CHANNEL_ID = BuildConfig.APPLICATION_ID + ".channel"

@OptIn(ExperimentalPagingApi::class)
fun sendNotification(context: Context, upcomingTitles: List<String>) {

    val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
        && notificationManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID) == null)
    {
        val name = context.getString(R.string.notification_channel_name)
        val channel = NotificationChannel(
            NOTIFICATION_CHANNEL_ID,
            name,
            NotificationManager.IMPORTANCE_DEFAULT
        )

        notificationManager.createNotificationChannel(channel)
    }

    val notificationId = ((System.currentTimeMillis() % 10000).toInt())
    val intent = Intent(context, UpcomingMoviesActivity::class.java).apply {
        putExtra(context.getString(R.string.notification_id_key), notificationId)
    }

    val builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
        .setSmallIcon(R.drawable.ic_cineme_notification_icon)
        .setContentTitle(context.getString(R.string.notification_title))
        .setPriority(NotificationCompat.PRIORITY_HIGH)
        .addAction(
            R.drawable.ic_notifications_black_24dp,
            context.getString(R.string.notification_action_text),
            PendingIntent.getActivity(context, 0, intent, FLAG_UPDATE_CURRENT)
        )
        .setStyle(NotificationCompat.BigTextStyle()
            .bigText(context.getString(R.string.notification_text, upcomingTitles.joinToString())))
        .setAutoCancel(true)

    notificationManager.notify(notificationId, builder.build())
}

fun cancelNotification(context: Context, id: Int){
    val notificationManager = context
        .getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    notificationManager.cancel(id)
}

