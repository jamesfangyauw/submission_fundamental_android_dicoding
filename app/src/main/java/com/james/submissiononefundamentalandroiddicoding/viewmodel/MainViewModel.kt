package com.james.submissiononefundamentalandroiddicoding.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.james.submissiononefundamentalandroiddicoding.Result
import com.james.submissiononefundamentalandroiddicoding.model.ItemsItem
import com.james.submissiononefundamentalandroiddicoding.repository.UserRepository

class MainViewModel(application: Application) : ViewModel() {

    private val mUserRepository: UserRepository = UserRepository(application)

    fun searchUser(query: String): LiveData<Result<List<ItemsItem>>> {
        return mUserRepository.searchUser(query)
    }
}


//    private val _isLoading = MutableLiveData<Boolean>()
//    val isLoading: LiveData<Boolean> = _isLoading
//
//    private val _listUser = MutableLiveData<List<ItemsItem>>()
//    val listUser: LiveData<List<ItemsItem>> = _listUser
//
//    private val _result = MutableLiveData<Result<List<ItemsItem>>>()
//    val result: LiveData<Result<List<ItemsItem>>> = _result

//    private val result = MediatorLiveData<Result<List<ItemsItem>>>()


//            val client = ApiConfig.getApiService().searchUser(query)
//            client.enqueue(object : Callback<GithubResponse> {
//                override fun onResponse(
//                    call: Call<GithubResponse>,
//                    response: Response<GithubResponse>
//                ) {
////                    _isLoading.value = false
//                    if (response.isSuccessful) {
////                        _listUser.value = response.body()?.items
//                        val items = response.body()?.items!!
//                        if (items.isNotEmpty()) {
//                            result.value = Result.Success(items)
//                        } else {
//                            result.value = Result.Error ("Response body is null")
//                        }
//                    } else {
//                        result.value = Result.Error("Response is not successful: ${response.message()}")
//                    }
//                }
//
//                override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
////                    _isLoading.value = false
//                    result.value = Result.Error("Request failed: ${t.message}")
//                    Log.e(TAG, "onFailure: ${t.message}")
//                }
//            })
//        return result
//        }
