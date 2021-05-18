package edu.uw.echee

import android.app.Application
import edu.uw.echee.requestpermissionandfetchlocationspr21.manager.SimpleLocationManager

class MyApplication: Application() {

    lateinit var simpleLocationManager: SimpleLocationManager

    override fun onCreate() {
        super.onCreate()
        this.simpleLocationManager = SimpleLocationManager(this)
    }

}
