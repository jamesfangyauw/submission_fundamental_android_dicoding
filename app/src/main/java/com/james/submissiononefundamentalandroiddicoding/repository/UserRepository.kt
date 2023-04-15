package com.james.submissiononefundamentalandroiddicoding.repository

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.james.submissiononefundamentalandroiddicoding.Result
import com.james.submissiononefundamentalandroiddicoding.api.ApiConfig
import com.james.submissiononefundamentalandroiddicoding.db.UserDao
import com.james.submissiononefundamentalandroiddicoding.db.UserEntity
import com.james.submissiononefundamentalandroiddicoding.db.UserRoomDatabase
import com.james.submissiononefundamentalandroiddicoding.model.DetailUserResponse
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class UserRepository(application: Application) {
    private val mUserDao: UserDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = UserRoomDatabase.getDatabase(application)
        mUserDao = db.userDao()
    }

    fun getAllUsers(): LiveData<List<UserEntity>> = mUserDao.getAllUsers()
    fun getFavoriteUserByUsername(username: String): LiveData<List<UserEntity>> =
        mUserDao.getFavoriteUserByUsername(username = username)

    fun insert(note: UserEntity) {
        executorService.execute { mUserDao.insert(note) }
    }
    fun delete(note: UserEntity) {
        executorService.execute { mUserDao.delete(note) }
    }

    fun searchUser(query: String) : LiveData<Result<List<ItemsItem>>> = liveData {
        emit (Result.Loading)
        try {
            val response = ApiConfig.getApiService().searchUser(query)
            if (response.items.isNotEmpty()){
                emit(Result.Success(response.items))
            } else{
                emit (Result.Error("Data tidak ditemukan"))
            }
        }catch (exception: Exception) {
            emit(Result.Error(exception.message ?: "Unknown error"))
        }
    }

    fun getDetailUser(login: String?) : LiveData <Result<DetailUserResponse>> = liveData {
        emit(Result.Loading)
        try {
            val response = ApiConfig.getApiService().getDetailUser(login)
            if (response != null) {
                emit(Result.Success(response))
            } else {
                emit(Result.Error("Failed to get detail user"))
            }
        } catch (exception: Exception) {
            emit(Result.Error(exception.message ?: "Unknown error"))
        }
    }


}