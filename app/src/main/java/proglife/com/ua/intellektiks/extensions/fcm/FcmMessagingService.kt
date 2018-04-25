package proglife.com.ua.intellektiks.extensions.fcm

import android.annotation.TargetApi
import android.app.Notification
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.content.ContextCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import java.util.*
import android.app.NotificationChannel
import android.app.PendingIntent
import android.content.Intent
import android.graphics.Color
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants
import proglife.com.ua.intellektiks.data.models.NotificationMessagePreview
import proglife.com.ua.intellektiks.ui.notifications.show.NotificationShowActivity


/**
 * Created by Evhenyi Shcherbyna on 27.12.2017.
 */
class FcmMessagingService : FirebaseMessagingService() {

    //var broadcast: BroadcastReceiver? = null

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "Default"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage) {
        super.onMessageReceived(remoteMessage)
        val builder = with(NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)) {
//            priority = PRIORITY_HIGH
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                setSmallIcon(R.mipmap.ic_logo)
                color = ContextCompat.getColor(this@FcmMessagingService, R.color.colorPrimary)
            } else {
                setSmallIcon(R.mipmap.ic_launcher)
            }
            setShowWhen(true)
            setContentTitle(remoteMessage.notification?.title)
            setContentText(remoteMessage.notification?.body)
            setAutoCancel(true)

            val intent = Intent(this@FcmMessagingService, NotificationShowActivity::class.java)
            remoteMessage.data.entries.forEach { intent.putExtra(it.key, it.value) }
            val resultPendingIntent = PendingIntent.getActivity(applicationContext, remoteMessage.hashCode(), intent, PendingIntent.FLAG_ONE_SHOT)
            setContentIntent(resultPendingIntent)
            setDefaults(Notification.DEFAULT_ALL)
        }

        sendBroadcast(Intent(Constants.Field.NOTIFICATION_UPDATE))
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) createChannel(notificationManager)
        notificationManager.notify("intellectics", Random().nextInt(), builder.build())
    }

    @TargetApi(Build.VERSION_CODES.O)
    private fun createChannel(notificationManager: NotificationManager) {
        val notificationChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, "Notifications", NotificationManager.IMPORTANCE_DEFAULT)

        // Configure the notification channel.
        notificationChannel.description = "Default notification channel"
        notificationChannel.enableLights(true)
        notificationChannel.lightColor = Color.RED
        notificationChannel.vibrationPattern = longArrayOf(0, 1000, 500, 1000)
        notificationChannel.enableVibration(true)
        notificationManager.createNotificationChannel(notificationChannel)
    }

}