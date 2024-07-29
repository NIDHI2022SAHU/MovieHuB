package com.example.moviehub.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.moviehub.db.dao.UserDao
import com.example.moviehub.db.databse.AppDatabase
import com.example.moviehub.repo.DbRepository
import com.example.moviehub.viewmodel.DbViewModel

abstract class BaseActivity : AppCompatActivity() {

    protected lateinit var dbViewModel: DbViewModel
    private lateinit var dbRepository: DbRepository
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initDatabase()
    }
    private fun initDatabase() {
        userDao = AppDatabase.getDatabase(applicationContext).userDao()
        dbRepository = DbRepository(userDao)
        dbViewModel = DbViewModel(dbRepository)
    }
}