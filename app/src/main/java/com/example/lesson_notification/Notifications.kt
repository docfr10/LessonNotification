package com.example.lesson_notification

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat

class Notifications : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        // Создание намерения входа в приложение
        val intent1 = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Создание PendingIntent для входа в приложение
        val pendingIntent =
            PendingIntent.getActivity(context, 0, intent1, PendingIntent.FLAG_IMMUTABLE)
        // Создание уведомления
        val notification = NotificationCompat.Builder(context, "channelID2")
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setContentTitle("Заголовок 2") // Заголовок уведомления
            .setContentText("Текст 2") // Текст уведомления
            .setDefaults(Notification.DEFAULT_ALL) // Выставление настроек уведомления(звук, вибрация) по умолчанию
            .setContentIntent(pendingIntent) // Действие при нажатии на уведомление
            .setAutoCancel(false) // Закрытие уведомления при нажатии на него
            .build()
        // Отправка уведомления на канал
        val manager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(2, notification)
    }
}
