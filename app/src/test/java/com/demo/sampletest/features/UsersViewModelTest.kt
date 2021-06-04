package com.demo.sampletest.features

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.demo.sampletest.data.model.UserInfo
import com.demo.sampletest.data.repository.UsersRepository
import com.demo.sampletest.features.users.UsersViewModel
import com.demo.sampletest.testUtils.getValue
import com.demo.sampletest.testUtils.testUser1
import com.demo.sampletest.testUtils.testUser2
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
@RunWith(JUnit4::class)
class UsersViewModelTest {
    private val testUsersList = listOf(testUser1, testUser2)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    private val testDispatcher = TestCoroutineDispatcher()

    @Mock
    private lateinit var usersRepository: UsersRepository

    // class under test
    private lateinit var usersViewModel: UsersViewModel

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)
        Dispatchers.setMain(testDispatcher)
    }

    @After
    fun cleanup() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getUsersTest() = runBlockingTest {
        val userList = flowOf(testUsersList)
        Mockito.`when`(usersRepository.getAllUsers()).thenAnswer {
            return@thenAnswer userList
        }

        usersViewModel = UsersViewModel(usersRepository)

        val result: List<UserInfo> = getValue(usersViewModel.getAllUsers())

        assertEquals(testUsersList, result)
    }

    @Test
    fun refreshUsersTest_verifyCalls() = runBlockingTest {
        val userList = flowOf(testUsersList)
        Mockito.`when`(usersRepository.getAllUsers()).thenAnswer {
            return@thenAnswer userList
        }

        usersViewModel = UsersViewModel(usersRepository)

        usersViewModel.refreshAllUsers()

        verify(usersRepository, times(1)).getAllUsers()
    }

}