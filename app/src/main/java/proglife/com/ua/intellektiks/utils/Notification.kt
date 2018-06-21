package proglife.com.ua.intellektiks.utils

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v4.content.ContextCompat
import android.util.Log
import android.widget.RemoteViews
import proglife.com.ua.intellektiks.R
import proglife.com.ua.intellektiks.data.Constants.Field.EXTRA_STATE
import proglife.com.ua.intellektiks.data.Constants.Field.MEDIA_NOTIFICATION
import proglife.com.ua.intellektiks.data.Constants.Field.STATE_DELETE
import proglife.com.ua.intellektiks.data.Constants.Field.STATE_PLAY

class Notification(private val context: Context) : Notification() {

    private val CHANNEL_ID = "media"
    private var mNotificationManager: NotificationManager
    private var remoteViews: RemoteViews
    var builder: Notification.Builder

    init {
        val ns = Context.NOTIFICATION_SERVICE

        builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification.Builder(context,  createNotificationChannel())
        } else {
            Notification.Builder(context)
        }
        mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder.setSmallIcon(R.mipmap.ic_logo)
            builder.setColor(ContextCompat.getColor(context, R.color.colorPrimary))
        } else {
            builder.setSmallIcon(R.mipmap.ic_launcher)
        }
        builder.setOngoing(true)
        builder.setVisibility(Notification.VISIBILITY_PUBLIC)
        builder.setCategory(NotificationCompat.CATEGORY_TRANSPORT)

        remoteViews = RemoteViews(context.packageName, R.layout.notification_media)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            builder.setCustomContentView(remoteViews)
        } else{
            builder.setContent(remoteViews)
        }
        builder.setContentTitle("Intelectics")
        builder.setContentText("Intelectics")
        builder.setShowWhen(false)

       val notification = setImage(R.drawable.ic_pause)
        mNotificationManager.notify(548853, notification)
    }

    private fun createNotificationChannel(): String {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = context.getString(R.string.channel_name)
            val description = context.getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance)
            channel.setSound(null,null)
            channel.description = description
            channel.enableLights(true)
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)

            return channel.id
        }
        return "0"
    }

    fun setText(text: String){
        remoteViews.setTextViewText(R.id.tvDescription, text)
        val notification = builder.build()

        notification.icon = R.mipmap.ic_launcher

        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
        mNotificationManager.notify(548853,notification)
    }

    fun setImage(res: Int): Notification{

        val delete = Intent(MEDIA_NOTIFICATION)
        delete.putExtra(EXTRA_STATE, STATE_DELETE)
        val radio = Intent(MEDIA_NOTIFICATION)
        radio.putExtra(EXTRA_STATE, STATE_PLAY)
        remoteViews.setOnClickPendingIntent(R.id.btnPlay, PendingIntent.getBroadcast(context, 4426, radio, PendingIntent.FLAG_UPDATE_CURRENT))
        remoteViews.setImageViewResource(R.id.btnPlay, res)
        remoteViews.setOnClickPendingIntent(R.id.btnClose, PendingIntent.getBroadcast(context, 4154, delete, PendingIntent.FLAG_UPDATE_CURRENT))
        remoteViews.setImageViewResource(R.id.btnClose, R.drawable.ic_close)
        val notification = builder.build()

        notification.icon = R.mipmap.ic_launcher

        notification.flags = notification.flags or Notification.FLAG_ONGOING_EVENT
        return notification
    }

    fun show(res: Int){
        mNotificationManager.notify(548853, setImage(res))
    }

    fun destroy() {
        Log.d("LOGS", "destroy")
        mNotificationManager.cancel(548853)
    }
}