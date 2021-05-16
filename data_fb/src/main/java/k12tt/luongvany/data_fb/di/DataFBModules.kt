package k12tt.luongvany.data_fb.di

import k12tt.luongvany.data.source.FBData
import k12tt.luongvany.data_fb.FBDataImpl
import org.koin.core.context.loadKoinModules
import org.koin.dsl.module

object DataFBModules {
    fun load(){
        loadKoinModules(module{
            factory<FBData> { FBDataImpl() }
        })
    }
}