package com.ynab

//import android.content.Context
//import androidx.room.Room
//import com.ynab.data.dataSource.room.RoomDatabase
//import dagger.Module
//import dagger.Provides
//import dagger.hilt.android.qualifiers.ApplicationContext
//import dagger.hilt.components.SingletonComponent
//import dagger.hilt.testing.TestInstallIn
//import javax.inject.Singleton
//
//@Module
//@TestInstallIn(
//    components = [SingletonComponent::class],
//    replaces = [RoomModule::class]
//)
//object TestRoomModule {
//    @Provides
//    @Singleton
//    fun provideRoomDatabase(@ApplicationContext context: Context) =
//        Room.inMemoryDatabaseBuilder(context, RoomDatabase::class.java).allowMainThreadQueries().build()
//}