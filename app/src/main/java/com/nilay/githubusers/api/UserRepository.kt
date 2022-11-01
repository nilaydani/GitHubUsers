package com.nilay.githubusers.api

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.liveData
import com.nilay.githubusers.model.User
import com.nilay.githubusers.room.UserDao
import javax.inject.Inject
import javax.inject.Singleton

/**
 * This class returns the paged data consumed by the [UserPagingSource]
 * @Singleton annotation makes the class a single instance in the dependencies graph
 * during the entire applications life time
 * @Inject annotation provides the same instance of this class
 * */
@Singleton
class UserRepository @Inject constructor(
    private val usersApi: UsersApi,
    private val userDetailsApi: UserDetailsApi,
    private val userDao: UserDao
) {

    /**
     * Returns a [liveData] paged results
     * params -> query: required to enable quering by the keyword provided
     * */
    fun getSearchResults(query: String) =
        Pager(
            config = PagingConfig(
                pageSize = PAGE_SIZE,
                maxSize = MAX_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { UserPagingSource(usersApi, query) }
        ).liveData

    companion object {
        const val PAGE_SIZE = 30
        const val MAX_SIZE = 100
    }

    fun getUserDetails(name: String) =
        userDetailsApi.getUserDetail(name)

    fun addUserToFav(user: User) {
        userDao.insertUser(user)
    }

    fun getAllfavUsers(): List<User> {
        return userDao.getAllUsers()
    }

    fun getUserById(id: String): Boolean {
        return userDao.isRowIsExist(id)
    }

    fun removeFav(user: User) {
        userDao.deleteUser(user.id)
    }
}