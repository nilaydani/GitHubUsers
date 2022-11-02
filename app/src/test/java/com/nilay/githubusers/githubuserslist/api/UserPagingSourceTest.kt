package com.nilay.githubusers.githubuserslist.api

import android.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.PagingSource
import com.nilay.githubusers.api.UserPagingSource
import com.nilay.githubusers.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations
import kotlin.test.assertEquals

/**
 * [UserPagingSourceTest]  sanity class to the test the [UserPagingSource]
 * paged results by a using an instance of the [FakeUsersApi] class
 * */
@OptIn(ExperimentalCoroutinesApi::class)
class UserPagingSourceTest {

    // swaps the background executor using a different one to execute task synchronously
    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    // initialise mockito
    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    // default query for testing
    companion object {
        const val QUERY = "nilaydani"
    }

    // create a list of fake users
    private val fakeUsers = listOf(
        User(
            1,
            "6356686",
            "nilaydani",
            "1.0",
            "https://avatars.githubusercontent.com/u/6356686?v=4",
            "https://api.github.com/users/nilaydani",
            "https://github.com/nilaydani"
        ),
        User(
            1,
            "6356686",
            "nilaydani",
            "1.0",
            "https://avatars.githubusercontent.com/u/6356686?v=4",
            "https://api.github.com/users/nilaydani",
            "https://github.com/nilaydani"
        ),
        User(
            1,
            "6356686",
            "nilaydani",
            "1.0",
            "https://avatars.githubusercontent.com/u/6356686?v=4",
            "https://api.github.com/users/nilaydani",
            "https://github.com/nilaydani"
        ),
        User(
            1,
            "6356686",
            "nilaydani",
            "1.0",
            "https://avatars.githubusercontent.com/u/6356686?v=4",
            "https://api.github.com/users/nilaydani",
            "https://github.com/nilaydani"
        ),
        User(
            1,
            "6356686",
            "nilaydani",
            "1.0",
            "https://avatars.githubusercontent.com/u/6356686?v=4",
            "https://api.github.com/users/nilaydani",
            "https://github.com/nilaydani"
        ),
        User(
            1,
            "6356686",
            "nilaydani",
            "1.0",
            "https://avatars.githubusercontent.com/u/6356686?v=4",
            "https://api.github.com/users/nilaydani",
            "https://github.com/nilaydani"
        )
    )

    /**
     * instance of [FakeUsersApi] class
     * **/
    private val fakeApi = FakeUsersApi().apply {
        for (// add fake users to the list of user in the base class
        user in fakeUsers) {
            addUser(user)
        }
    }


    @Test
    fun userPagingSource() = runBlockingTest {
        // given an instance of user paging source
        val pagingSource = UserPagingSource(fakeApi, QUERY)

        // when
        val expected = PagingSource.LoadResult.Page(
            data = fakeUsers,
            prevKey = 1,
            nextKey = 2
        )
        val actual = pagingSource.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 1,
                placeholdersEnabled = false
            )
        )

        // then
        assertEquals(
            (expected as PagingSource.LoadResult.Page<Any, Any>).data.size,
            (actual as PagingSource.LoadResult.Page<Any, Any>).data.size
        )
    }

}