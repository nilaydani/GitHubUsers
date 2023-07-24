package com.nilay.githubusers.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nilay.githubusers.api.UserRepository
import com.nilay.githubusers.model.User
import com.nilay.githubusers.model.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import org.json.JSONObject
import retrofit2.awaitResponse
import javax.inject.Inject

/**
 * [UserViewModel] class to prepare date for the UI -> [MainActivity]
 * @ViewModelInject makes this class to be retrieved and made available to activities
 * */

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: UserRepository,
    state: SavedStateHandle
) : ViewModel() {
    private val liveDataSearchResults = state.getLiveData(CURRENT_QUERY, DEFAULT_QUERY)

    // return the live date of users to be observed the activity
    val users = liveDataSearchResults.switchMap { queryString ->
        repository.getSearchResults(queryString).cachedIn(viewModelScope)
    }

    private val _liveDataUserDetail: MutableLiveData<UserDetails> = MutableLiveData<UserDetails>()
    val liveDataUserDetail: MutableLiveData<UserDetails> = _liveDataUserDetail

    private val _liveDataFav : MutableLiveData<List<User>> = MutableLiveData()
    val liveDataFav: MutableLiveData<List<User>> = _liveDataFav

    // query to get users
    fun searchUsers(query: String) {
        CoroutineScope(Dispatchers.IO).launch {
            liveDataSearchResults.postValue(query)
        }

    }

    fun callUserDetailsApi(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _liveDataUserDetail.postValue(
                repository.getUserDetails(username)?.awaitResponse()?.body()
            )
        }
    }

    fun addUsrToDB(user: User) = viewModelScope.launch(Dispatchers.IO) {
        if (!repository.getUserById(user.id))
            repository.addUserToFav(user)
        else {
            repository.removeFav(user)
        }
    }

    fun getAllFavUsers() = viewModelScope.launch(Dispatchers.IO) {
        _liveDataFav.postValue(repository.getAllfavUsers())
    }

    suspend fun checkIfUserExist(user: User): Boolean {
        var isExist: Boolean
        withContext(Dispatchers.IO) {
            isExist = repository.getUserById(user.id)
        }
        return isExist
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "android"
    }
}