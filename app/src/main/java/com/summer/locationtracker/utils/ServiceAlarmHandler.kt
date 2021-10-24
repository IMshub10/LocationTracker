package com.summer.locationtracker.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import com.summer.locationtracker.service.FetchLocationService

class ServiceAlarmHandler(private val mContext: Context) {

    /**
     * Initializing alarm and assigning a service
     */
    fun setAlarmManager() {
        val startServiceIntent = Intent(mContext, FetchLocationService::class.java)
        val sender = PendingIntent.getForegroundService(
            mContext,
            Constants.ALARM_PENDING_INTENT_REQUEST_CODE,
            startServiceIntent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            System.currentTimeMillis(),
            Constants.ALARM_TRIGGER,
            sender
        )
    }

    /**
     * Cancelling alarm and assigning a service
     */
    fun cancelAlarmManager() {
        val startServiceIntent = Intent(mContext, FetchLocationService::class.java)
        val sender = PendingIntent.getForegroundService(
            mContext,
            Constants.ALARM_PENDING_INTENT_REQUEST_CODE,
            startServiceIntent,
            0
        )
        val alarmManager = mContext.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.cancel(sender)
    }
}