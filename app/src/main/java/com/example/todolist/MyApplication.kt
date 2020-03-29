package com.example.todolist

import android.app.Application
import io.realm.Realm

// 애플리케이션 객체에서 Realm 초기화
class MyApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        Realm.init(this)
    }
}