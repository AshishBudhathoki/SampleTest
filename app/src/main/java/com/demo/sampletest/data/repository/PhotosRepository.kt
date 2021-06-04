package com.demo.sampletest.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.demo.sampletest.api.ApiService
import com.demo.sampletest.data.model.UserPhotos
import com.demo.sampletest.utils.InjectorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import timber.log.Timber
import java.io.IOException

class PhotosRepository constructor(private val apiService: ApiService) {

    // Variables for showing/hiding loading indicators
    private val areUsersLoading: MutableLiveData<Boolean> = MutableLiveData(false)

    // Set value to message to be shown in snackbar
    private val launchesSnackBar = MutableLiveData<String>()

    fun getUsersLoadingStatus(): LiveData<Boolean> = areUsersLoading

    fun getUsersSnackbar(): MutableLiveData<String> = launchesSnackBar

    suspend fun getAllPhotos(): Flow<List<UserPhotos>?> {
        areUsersLoading.postValue(true)
        return flow {
            withContext(Dispatchers.IO) {
                try {
                    emit(apiService.getAllPhotos().body())
                } catch (exception: Exception) {
                    when (exception) {
                        is IOException -> launchesSnackBar.postValue("Network problem occurred")
                        else -> {
                            Timber.d("API CALL ERROR IS : ${exception.message}")
                            launchesSnackBar.postValue("Unexpected problem occurred")
                        }
                    }
                }
            }
            areUsersLoading.postValue(false)
        }
    }
}