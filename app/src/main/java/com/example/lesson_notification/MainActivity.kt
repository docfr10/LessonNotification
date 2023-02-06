package com.example.lesson_notification

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.DatePicker
import android.widget.TimePicker
import androidx.core.app.NotificationCompat
import com.example.lesson_notification.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Календарь для сохранения даты и времени уведомления
        val calendar: Calendar = Calendar.getInstance()

        binding = ActivityMainBinding.inflate(layoutInflater)
        // Кнопка создания простого уведомления
        binding.buttonShowNotification.setOnClickListener {
            createNotificationChannelForSimpleNotification()
            showSimpleNotification()
        }
        // Кнопка создания уведомления по расписанию
        binding.buttonScheduleNotification.setOnClickListener {
            createDatePicker(calendar)
            createNotificationChannelForNotificationAtDate()
        }
        setContentView(binding.root)
    }

    // Функция создания канала уведомлений для простого уведомления
    private fun createNotificationChannelForSimpleNotification() {
        // Класс для уведомления пользователя о событиях, которые происходят
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Имя канала
        val name = "channelID1"
        // Описание канала
        val desc = "A Description of the Channel"
        // Степень важности уведомления
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        // Объявление канала уведомлений
        val channel = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(name, name, importance)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        // Присвоение каналу его описания
        channel.description = desc
        // Создание канала уведомлений, на который можно отправлять уведомления
        notificationManager.createNotificationChannel(channel)
    }

    // Функция создания и показа простого уведомления
    private fun showSimpleNotification() {
        // Создание намерения входа в приложение
        val intent1 = Intent(this, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        // Создание PendingIntent для входа в приложение
        val pendingIntent =
            PendingIntent.getActivity(this, 0, intent1, PendingIntent.FLAG_IMMUTABLE)
        // Создание уведомления
        val notification = NotificationCompat.Builder(this, "channelID1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Заголовок1") // Заголовок уведомления
            .setContentText("Текст1") // Текст уведомления
            .setContentIntent(pendingIntent) // Действие при нажатии на уведомление
            .setAutoCancel(true) // Закрытие уведомления при нажатии на него
            .build()
        // Отправка уведомления на канал
        val manager = this.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification)
    }

    // Функция выставления даты уведомления
    private fun createDatePicker(calendar: Calendar) {
        DatePickerDialog(
            this,
            { _: DatePicker, mYear: Int, mMonth: Int, mDayOfMonth: Int ->
                calendar.set(mYear, mMonth, mDayOfMonth)
                createTimePicker(calendar)
            },
            calendar[Calendar.YEAR],
            calendar[Calendar.MONTH],
            calendar[Calendar.DAY_OF_MONTH]
        ).show()
    }

    // Функция выставления времени уведомления
    private fun createTimePicker(calendar: Calendar) {
        TimePickerDialog(
            this,
            { _: TimePicker, mHour: Int, mMinute: Int ->
                calendar.set(Calendar.HOUR_OF_DAY, mHour)
                calendar.set(Calendar.MINUTE, mMinute)
                createNotificationAtDate(calendar)
            },
            calendar[Calendar.HOUR_OF_DAY],
            calendar[Calendar.MINUTE],
            true,
        ).show()
    }

    // Функция создания канала уведомлений для уведомления на дату
    private fun createNotificationChannelForNotificationAtDate() {
        // Класс для уведомления пользователя о событиях, которые происходят
        val notificationManager = this.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        // Имя канала
        val name = "channelID2"
        // Описание канала
        val desc = "A Description of the Channel"
        // Степень важности уведомления
        val importance = NotificationManager.IMPORTANCE_HIGH
        // Объявление канала уведомлений
        val channel1 = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel(name, name, importance)
        } else {
            TODO("VERSION.SDK_INT < O")
        }
        // Присвоение каналу его описания
        channel1.description = desc
        // Создание канала уведомлений, на который можно отправлять уведомления
        notificationManager.createNotificationChannel(channel1)
    }

    // Функция создания уведомления на дату
    private fun createNotificationAtDate(calendar: Calendar) {
        // Создание уведомления
        val intent = Intent(this, Notifications::class.java)
        // Класс предоставляет доступ к службам системных сигналов
        val alarmManager = this.getSystemService(ALARM_SERVICE) as AlarmManager
        // Создание широковещательного сигнала для отправки уведомления
        val pendingIntent =
            PendingIntent.getBroadcast(
                this,
                0,
                intent,
                PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
            )
        // Запись даты когда нужно отправить уведомлние
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }
}