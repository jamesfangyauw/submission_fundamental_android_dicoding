package com.james.submissiononefundamentalandroiddicoding.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.james.submissiononefundamentalandroiddicoding.api.ApiConfig
import com.james.submissiononefundamentalandroiddicoding.db.UserEntity
import com.james.submissiononefundamentalandroiddicoding.model.DetailUserResponse
import com.james.submissiononefundamentalandroiddicoding.repository.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class DetailViewModel (application: Application) : ViewModel() {
    private val mUserRepository: UserRepository = UserRepository(application)

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _detailUser = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = _detailUser

    fun getDetailUser(login: String?) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetailUser(login)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    _detailUser.value = response.body()
                } else {
                    Log.e(TAG, "onFailure : ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure : ${t.message}")
            }
        })
    }

    fun insert(user: UserEntity) {
        mUserRepository.insert(user)
    }

    fun delete(user: UserEntity) {
        mUserRepository.delete(user)
    }

    fun getFavoriteUserByUsername (username : String) : LiveData<List<UserEntity>>{
       return mUserRepository.getFavoriteUserByUsername(username)
    }

    companion object {
        private const val TAG = "DetailViewModel"
    }
}