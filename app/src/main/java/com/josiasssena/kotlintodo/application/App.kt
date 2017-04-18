package com.josiasssena.kotlintodo.application

import android.app.Application
import com.josiasssena.kotlintodo.realm.RealmModule
import io.realm.Realm
import io.realm.RealmConfiguration


/**
 * @author Josias Sena
 */
class App : Application() {

    override fun onCreate() {
        super.onCreate()

        initRealm()
    }

    private fun initRealm() {
        Realm.init(this)
        Realm.setDefaultConfiguration(RealmConfiguration.Builder()
                .name("koptlintodo.realm")
                .deleteRealmIfMigrationNeeded()
                .modules(RealmModule())
                .build())
    }

}