import android.app.NotificationManager
import androidx.core.app.NotificationCompat
import com.galeria.medtracker2.core.notifications.NotificationConstants
import dagger.hilt.android.qualifiers.ApplicationContext

@Singleton
class MedsNotificationManager @Inject constructor(
    @ApplicationContext
    private val context: Context,
) {

    private val notificationManager =
        context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager


    fun showMedsNotification(
        title: String,
        message: String,
    ) {
        // Строим уведомление.
        val notification = NotificationCompat.Builder(context, NotificationConstants.MED_CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_background)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true) // смахнется после клика.    
            .build()

        // id должен быть уникальным для каждого уведомления.
        val notificationId = System.currentTimeMillis().toInt()

        // Показываем уведомление.
        notificationManager.notify(notificationId, notification)
    }

}