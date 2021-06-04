package com.demo.sampletest.data.repository

import com.demo.sampletest.api.ApiService
import com.demo.sampletest.data.model.UserInfo
import com.demo.sampletest.testUtils.testUser1
import com.demo.sampletest.testUtils.testUser2
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations


@RunWith(JUnit4::class)
class UsersRepositoryTest {

    private val testUsersList = listOf(
        testUser1,
        testUser2
    )

    @Mock
    private lateinit var apiService: ApiService

    // Class under test
    private lateinit var usersRepository: UsersRepository

    @Before
    fun setup() {
        MockitoAnnotations.initMocks(this)

        usersRepository = UsersRepository(apiService)
    }

    @Test
    fun getAllUsersTest() = runBlocking {
        val usersFlow = flowOf(testUsersList)

        Mockito.`when`(apiService.getAllUsers()).thenAnswer {
            return@thenAnswer usersFlow
        }
        var result = listOf<UserInfo>()
        usersRepository.getAllUsers().collect {
            result = it.body()!!
        }
        Assert.assertEquals(result, testUsersList)
    }
}
