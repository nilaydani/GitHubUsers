package com.nilay.githubusers.viewmodel

import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.nilay.githubusers.api.UserRepository
import com.nilay.githubusers.model.UserDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
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

    // query to get users
    fun searchUsers(query: String) {
        GlobalScope.launch(Dispatchers.Main) {
            liveDataSearchResults.postValue(query)
        }

    }

    fun callUserDetailsApi(username: String) {
        CoroutineScope(Dispatchers.IO).launch {
            _liveDataUserDetail.postValue(repository.getUserDetails(username)?.awaitResponse()?.body())
        }
    }

    companion object {
        private const val CURRENT_QUERY = "current_query"
        private const val DEFAULT_QUERY = "android"
    }
}