package com.nizarfadlan.aplikasigithubuser.ui.detailsScreen

import android.arch.core.executor.testing.InstantTaskExecutorRule
import com.nizarfadlan.aplikasigithubuser.data.GithubRepository
import com.nizarfadlan.aplikasigithubuser.data.Result
import com.nizarfadlan.aplikasigithubuser.data.local.entity.UserEntity
import com.nizarfadlan.aplikasigithubuser.helpers.MainCoroutineRule
import com.nizarfadlan.aplikasigithubuser.helpers.getOrAwaitValue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class DetailViewModelTest {
    @get:Rule
    val instantTaskExecutorRule: InstantTaskExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Mock
    private lateinit var repository: GithubRepository
    private lateinit var viewModel: DetailViewModel

    // My username github
    private val username = "nizarfadlan"

    @Before
    fun setUp() {
        repository = mock(GithubRepository::class.java)
        viewModel = DetailViewModel(repository)
    }

    @Test
    fun testUpdateFavoriteUser() = runTest {
        val userExcept = UserEntity(
            1,
            "nizarfadlan",
            "https://avatars.githubusercontent.com/u/47259008?v=4",
            "",
            0,
            0,
            "Nizar Fadlan",
            "",
            "",
            false
        )
        whenever(repository.getDetailUser(username)).thenReturn(flowOf(Result.Success(userExcept)))
        viewModel.getDetailUser(username)

        viewModel.updateFavoriteUser(true, username)

        val result = viewModel.detailUser.getOrAwaitValue()
        assert(result is Result.Success)
    }
}