package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.app.Application
import android.util.Log
import k12tt.luongvany.data.di.DataModules
import k12tt.luongvany.data_fb.di.DataFBModules
import k12tt.luongvany.domain.di.DomainModules
import k12tt.luongvany.nghiencuukhoahoc.di.AppModule
import k12tt.luongvany.nghiencuukhoahoc.utils.AppPreference
import k12tt.luongvany.presentation.di.PresentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class NotificationApp: Application(){
    override fun onCreate() {
        super.onCreate()
        Log.d("TEST", "Co Chay")

        startKoin {
            androidContext(this@NotificationApp)
        }

        loadAllModules()

        AppPreference.init(this)
    }

    private fun loadAllModules() {
        AppModule.load()
        DataFBModules.load()
        DataModules.load()
        DomainModules.load()
        PresentationModule.load()
    }
}