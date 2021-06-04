package com.demo.sampletest.features.photos

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.sampletest.data.model.UserInfo
import com.demo.sampletest.data.model.UserPhotos
import com.demo.sampletest.data.repository.PhotosRepository
import com.demo.sampletest.data.repository.UsersRepository
import com.demo.sampletest.utils.InjectorUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class UserPhotosViewModel constructor(private val repository: PhotosRepository) : ViewModel() {


    private val userPhotos: MutableLiveData<List<UserPhotos>> = MutableLiveData()
    private val _areLaunchesLoading: LiveData<Boolean> = repository.getUsersLoadingStatus()
    private val _snackBar: MutableLiveData<String> = repository.getUsersSnackbar()

    fun getAllPhotos(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllPhotos()
                .collect { userPhotoList ->
                    userPhotos.postValue(userPhotoList?.filter { it.albumId == userId })
                    Timber.d("ALL PHOTOS: ${userPhotos.value}")
                }
        }
    }

    fun getUserPhotos(): LiveData<List<UserPhotos>> = userPhotos

    fun getLaunchesLoadingStatus(): LiveData<Boolean> = _areLaunchesLoading

    // Wrapper for refreshing launches data
    fun refreshAllPhotos() = viewModelScope.launch {
        Timber.d("SWIPED")
        repository.getAllPhotos()
    }

    /**
     * Request a snackbar to display a string.
     */
    val snackbar: LiveData<String>
        get() = _snackBar

    /**
     * Called immediately after the UI shows the snackbar.
     */
    fun onSnackbarShown() {
        _snackBar.value = null
    }

}