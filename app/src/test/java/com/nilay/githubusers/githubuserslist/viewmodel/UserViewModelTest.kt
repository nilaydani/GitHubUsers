package com.nilay.githubusers.githubuserslist.viewmodel

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.nilay.githubusers.api.UserDetailsApi
import com.nilay.githubusers.api.UserRepository
import com.nilay.githubusers.api.UserResponse
import com.nilay.githubusers.api.UsersApi
import com.nilay.githubusers.model.User
import com.nilay.githubusers.room.UserDao
import com.nilay.githubusers.viewmodel.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.MockitoAnnotations
import org.powermock.api.mockito.PowerMockito
import org.powermock.modules.junit4.PowerMockRunner

/**
 * Sanity class to the test [UserViewModel] class
 * */
@RunWith(PowerMockRunner::class)
class UserViewModelTest {

    // swaps the background executor using a different one to execute task synchronously
    @get:Rule
    var rule = InstantTaskExecutorRule()

    //late init variables
    private lateinit var userRepository: UserRepository
    private lateinit var savedStateHandle: SavedStateHandle
    private lateinit var userViewModel: UserViewModel

    lateinit var mUserApi: UsersApi

    lateinit var mUserDetailsApi: UserDetailsApi

    lateinit var mUserDao: UserDao

    @ExperimentalCoroutinesApi
    @Before
    fun init() = runBlockingTest {
        //initiliase mockito
        MockitoAnnotations.initMocks(this)
        savedStateHandle = SavedStateHandle()
        mUserApi = PowerMockito.mock(UsersApi::class.java)
        mUserDetailsApi = PowerMockito.mock(UserDetailsApi::class.java)
        mUserDao = PowerMockito.mock(UserDao::class.java)
        val meUser = User(1,"test","Nilay Dani","999","https://avatars.githubusercontent.com/u/6356686?v=4","https://github.com/nilaydani","https://github.com/nilaydani")
        PowerMockito.`when`(mUserApi.searchUsers("Nilaydani",1,10)).thenReturn(UserResponse(1,
            listOf(meUser)))
        Dispatchers.setMain(TestCoroutineDispatcher())
    }

    @ExperimentalCoroutinesApi
    @Test
    fun testViewModel() = runBlockingTest {
        userRepository = UserRepository(mUserApi,mUserDetailsApi,mUserDao)
        userViewModel = UserViewModel(repository = userRepository, state = savedStateHandle)
        Assert.assertEquals(1, mUserApi.searchUsers("Nilaydani",1,10).totalCount)
    }

    @After
    fun tearDown() {
        // Reset the main dispatcher to the original dispatcher after each test
        Dispatchers.resetMain()
    }
}