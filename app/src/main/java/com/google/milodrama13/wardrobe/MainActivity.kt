package com.google.milodrama13.wardrobe

import android.app.*
import android.content.Context
import android.content.Intent
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.support.design.widget.CoordinatorLayout
import android.support.design.widget.Snackbar
import android.support.v4.app.NotificationCompat
import android.support.v4.app.NotificationManagerCompat
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View

import kotlinx.android.synthetic.main.activity_main.*
import java.time.Instant
import java.util.*

class MainActivity : AppCompatActivity() {

    companion object {
        val NotifyDirtyClothes = 0
        val NotifyClothChoice = 1

        val WardrobeChannelId = "WARDROBE"

        val WearNotifDuration = 3 * 3600 * 1000L
        val WashNotifDuration = 5 * 3600 * 1000L

        fun createAlarms(context:Context) {
            val dao = MyDatabase.getInstance(context).getDao()
            val alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Creating alarm"))

            val intent = Intent(context, AlarmReceiver::class.java).let {
                intent -> PendingIntent.getBroadcast(context,0, intent, PendingIntent.FLAG_UPDATE_CURRENT )
            }

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }
            calendar.set(Calendar.HOUR_OF_DAY, 7)
            calendar.set(Calendar.MINUTE,0)
            calendar.set(Calendar.SECOND,0)
            calendar.set(Calendar.MILLISECOND,0)
            if (calendar.time.compareTo(Date()) < 0)
                calendar.add(Calendar.DAY_OF_MONTH, 1)

            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, intent)

            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Alarm set"))
        }

        fun createNotificationChannel(context:Context) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

                val oldChannels = mutableListOf<String>()
                for (channel:NotificationChannel in notificationManager.notificationChannels){
                    if (channel.id != WardrobeChannelId)
                        oldChannels.add(channel.id)
                }
                oldChannels.forEach { s: String -> notificationManager.deleteNotificationChannel(s) }

                notificationManager.createNotificationChannel(NotificationChannel(WardrobeChannelId, context.getString(R.string.notif_channel_name), NotificationManager.IMPORTANCE_DEFAULT).apply {
                    description = context.getString(R.string.notif_channel_description)
                    importance = NotificationManager.IMPORTANCE_DEFAULT
                })
            }
        }

        fun checkDirtyClothes(context:Context){
            val dao = MyDatabase.getInstance(context).getDao()

            val laundry = checkLaundry(context)
            if (laundry != null){
                dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "There is laundry to do"))

                val notification = when(laundry){
                    ClothType.WINTER_TOP -> context.getString(R.string.msg_check_winter)
                    ClothType.SUMMER_TOP -> context.getString(R.string.msg_check_summer)
                    ClothType.PANTS -> context.getString(R.string.msg_check_pants)
                }

                val builder = NotificationCompat.Builder(context, WardrobeChannelId)
                        .setSmallIcon(R.drawable.ic_laundry)
                        .setContentTitle(context.getString(R.string.title_laundry))
                        .setContentText(notification)
                        .setAutoCancel(true)
                        .setTimeoutAfter(WashNotifDuration)

                dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Laundry notification"))

                with(NotificationManagerCompat.from(context)){
                    notify(NotifyDirtyClothes, builder.build())
                }

                dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Should be notified"))
            }
        }

        private fun checkLaundry(context:Context) : ClothType?{
            val dao = MyDatabase.getInstance(context).getDao()
            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Checking laundry"))

            val calendar = Calendar.getInstance().apply {
                timeInMillis = System.currentTimeMillis()
            }

            var mainType = ClothType.WINTER_TOP
            var otherType = ClothType.SUMMER_TOP
            val month = calendar.get(Calendar.MONTH)
            if (month >= Calendar.MAY && month <= Calendar.SEPTEMBER){
                mainType = ClothType.SUMMER_TOP
                otherType = ClothType.WINTER_TOP

                dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Switching to summer mode"))
            }

            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            val oneWeekAgo = calendar.time.toInstant()
            calendar.add(Calendar.WEEK_OF_YEAR, -1)
            val twoWeeksAgo = calendar.time.toInstant()
            calendar.timeInMillis = System.currentTimeMillis()
            calendar.add(Calendar.MONTH, -1)
            val oneMonthAgo = calendar.time.toInstant()

            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Checking current season cloths"))

            var lastTime = dao.getOldestLaundry(mainType, 2)
            if (lastTime != null && lastTime.isBefore(oneWeekAgo))
                return mainType
            lastTime = dao.getOldestLaundry(mainType, 1)
            if (lastTime != null && lastTime.isBefore(twoWeeksAgo))
                return mainType

            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Checking previous season cloths"))

            lastTime = dao.getOldestLaundry(otherType, 1)
            if (lastTime != null && lastTime.isBefore(oneMonthAgo))
                return otherType

            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Checking pants"))

            lastTime = dao.getOldestLaundry(ClothType.PANTS, 1)
            if (lastTime != null && lastTime.isBefore(oneMonthAgo))
                return ClothType.PANTS

            return null
        }

        fun notifyClothChoice(context:Context){
            val dao = MyDatabase.getInstance(context).getDao()

            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Creating Quick wear intent"))

            val intent = Intent(context, QuickWearActivity::class.java)
            val pendingIntent = TaskStackBuilder.create(context).run{
                addNextIntentWithParentStack(intent)
                getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT)
            }

            val builder = NotificationCompat.Builder(context, WardrobeChannelId)
                    .setSmallIcon(R.drawable.ic_wear)
                    .setContentTitle(context.getString(R.string.title_wear_today))
                    .setContentText(context.getString(R.string.msg_what_cloth_today))
                    .setContentIntent(pendingIntent)
                    .setAutoCancel(true)
                    .setTimeoutAfter(WearNotifDuration)

            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Quick wear notification"))

            with(NotificationManagerCompat.from(context)){
                notify(NotifyClothChoice, builder.build())
            }

            dao.trace(TraceItem(Instant.now(), TracingLevel.Verbose, "Should be notified"))
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        createNotificationChannel(this)
        createAlarms(this)
    }
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
            R.id.action_add_cloth -> addCloth()
            R.id.action_quick_wear -> {
                val intent = Intent(this, QuickWearActivity::class.java)
                startActivity(intent)
                //notifyClothChoice(this)
                true
            }
            R.id.action_tracing -> {
                val intent = Intent(this, TracingActivity::class.java)
                startActivity(intent)
                true
            }
            R.id.action_clear_tracing -> {
                val dao = MyDatabase.getInstance(this).getDao()
                dao.clearTracing()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun addCloth(): Boolean {
        val intent = Intent(this, AddClothActivity::class.java)
        startActivityForResult(intent, AddClothActivity.RequestCode)
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == AddClothActivity.RequestCode && resultCode == RESULT_OK) {
            Snackbar.make(findViewById<CoordinatorLayout>(R.id.myCoordLayout), getString(R.string.msg_new_cloth), Snackbar.LENGTH_SHORT).show()
        }
        else
            super.onActivityResult(requestCode, resultCode, data)
    }

    fun showPants(view:View){
        val intent = Intent(this, ClothsActivity::class.java).apply {
            putExtra("CLOTH_TYPE", ClothType.PANTS)
        }
        startActivity(intent)
    }

    fun showSummerTops(view:View){
        val intent = Intent(this, ClothsActivity::class.java).apply {
            putExtra("CLOTH_TYPE", ClothType.SUMMER_TOP)
        }
        startActivity(intent)
    }

    fun showWinterTops(view:View){
        val intent = Intent(this, ClothsActivity::class.java).apply {
            putExtra("CLOTH_TYPE", ClothType.WINTER_TOP)
        }
        startActivity(intent)
    }

}
