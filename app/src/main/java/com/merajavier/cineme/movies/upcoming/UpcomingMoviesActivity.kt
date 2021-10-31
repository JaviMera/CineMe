package com.merajavier.cineme.movies.upcoming

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.merajavier.cineme.R
import com.merajavier.cineme.cancelNotification

class UpcomingMoviesActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upcoming_movies)

        val notificationId = intent.getIntExtra(getString(R.string.notification_id_key), -1)
        if(notificationId != -1){
            cancelNotification(this, notificationId)
        }
    }
}