package com.demo.sampletest.features

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demo.sampletest.data.model.UserInfo
import com.demo.sampletest.data.repository.UsersRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import timber.log.Timber

class UsersViewModel constructor(private val repository: UsersRepository) : ViewModel() {

    private val allUsers: MutableLiveData<List<UserInfo>> = MutableLiveData()
    private val _areLaunchesLoading: LiveData<Boolean> = repository.getUsersLoadingStatus()
    private val _snackBar: MutableLiveData<String> = repository.getUsersSnackbar()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getAllUsers()
                .collect { users ->
                    Timber.d("COLLECTED")
                    Timber.d(" USERS: $users")

                    allUsers.postValue(users.body())
                    Timber.d("ALL USERS: ${allUsers.value}")
                }
        }
    }

    fun getAllUsers(): LiveData<List<UserInfo>> = allUsers

    fun getLaunchesLoadingStatus(): LiveData<Boolean> = _areLaunchesLoading

    // Wrapper for refreshing launches data
    fun refreshAllUsers() = viewModelScope.launch {
        Timber.d("SWIPED")
        repository.getAllUsers()
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