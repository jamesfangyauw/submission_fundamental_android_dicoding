package com.james.submissiononefundamentalandroiddicoding.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.james.submissiononefundamentalandroiddicoding.db.UserDao
import com.james.submissiononefundamentalandroiddicoding.db.UserEntity
import com.james.submissiononefundamentalandroiddicoding.db.UserRoomDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository (application: Application) {
    private val  mUserDao : UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }
    fun getAllUsers(): LiveData<List<UserEntity>> = mUserDao.getAllUsers()
    fun getFavoriteUserByUsername(username : String): LiveData<List<UserEntity>> = mUserDao.getFavoriteUserByUsername(username = username )
    fun insert(note: UserEntity) {
        executorService.execute { mUserDao.insert(note) }
    }
    fun delete(note: UserEntity) {
        executorService.execute { mUserDao.delete(note) }
    }
}