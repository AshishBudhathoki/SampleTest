package com.demo.sampletest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.demo.sampletest.databinding.ActivityMainBinding
import com.demo.sampletest.features.users.UsersFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var usersFragment: UsersFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        usersFragment = UsersFragment()

        supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container, usersFragment, TAG_USER_FRAGMENT)
            .commit()
    }
}

private const val TAG_USER_FRAGMENT = "TAG_USERS_FRAGMENT"