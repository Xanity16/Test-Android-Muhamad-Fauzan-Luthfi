package keda.tech.android.catalog

import android.app.Application
import android.content.Context
import android.support.multidex.MultiDex
import keda.tech.android.catalog.db.AppDatabase
import keda.tech.android.catalog.utils.SPUtils

class App : Application() {
    private lateinit var ctx: Context
    private lateinit var db: AppDatabase
    private var pref: SPUtils? = null

    override fun onCreate() {
        super.onCreate()

        ctx = applicationContext
        pref = SPUtils.getInstance(this)
        db = AppDatabase.getAppDataBase(this)

        MultiDex.install(this)
    }

    override fun onTerminate() {
        AppDatabase.destroyDataBase()
        super.onTerminate()
    }

    fun getDb(): AppDatabase {
        return db
    }
}