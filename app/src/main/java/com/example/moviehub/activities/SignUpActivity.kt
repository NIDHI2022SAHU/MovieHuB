package com.example.moviehub.activities

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.moviehub.R
import com.example.moviehub.databinding.ActivitySignUpBinding
import com.example.moviehub.db.entity.Users
import com.example.moviehub.utilities.Utility
import com.example.moviehub.utilities.Validations

class SignUpActivity : BaseActivity(), View.OnFocusChangeListener{
    private  lateinit var binding: ActivitySignUpBinding
    private var isAllFieldValid=false
    private var utility: Utility = Utility()
    private val view: View by lazy { findViewById(android.R.id.content) }
    private val validation = Validations()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupUi()
        setupListeners()
    }

    private fun setupUi() {
        binding.textSignIn.paintFlags = binding.textSignUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun setupListeners() {
        binding.btnSignUp.setOnClickListener { handleSignUp() }
        binding.textSignIn.setOnClickListener { navigateTo(SignInActivity::class.java) }
        binding.etName.onFocusChangeListener = this
        binding.etEmail.onFocusChangeListener = this
        binding.etPhone.onFocusChangeListener = this
        binding.etPassword.onFocusChangeListener = this
        binding.etCPass.onFocusChangeListener = this
    }
    private fun navigateTo(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
        finish()
    }
    private fun handleSignUp() {
        isAllFieldValid=validation.validateUserInputForget(binding.etName,binding.etEmail,binding.etPhone,binding.etPassword,binding.etCPass)
        if (isAllFieldValid) {
            val name = binding.etName.text.toString()
            val email = binding.etEmail.text.toString()
            val phone = binding.etPhone.text.toString()
            val password = binding.etPassword.text.toString()
            val user = Users(0, name, email, phone, password)

            dbViewModel.insertUser(user)
            Toast.makeText(this, "Register Successfully", Toast.LENGTH_SHORT).show()
            navigateTo(SignInActivity::class.java)
        } else {
            utility.showSnackBar(view, "Please Fill Correct Detail!")
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus) {
            when (view?.id) {
                R.id.et_name -> {
                    val result= validation.checkValidation(binding.etName, "Name")
                    Log.d("Validation", "Name validation $result")
                }

                R.id.et_email -> {
                    val result=validation.checkValidation(binding.etEmail, "Email")
                    Log.d("Validation", " Email Validation: $result")
                }
                R.id.et_phone -> {
                    val result=validation.checkValidation(binding.etPhone, "Phone")
                    Log.d("Validation", " Phone Validation: $result")
                }
                R.id.et_password -> {
                    val result=validation.checkValidation(binding.etPassword, "Password")
                    Log.d("Validation", " Password Validation: $result")
                }
                R.id.et_c_pass -> {
                    val result= validation.checkValidation(binding.etCPass, binding.etPassword, "CPass")
                    Log.d("Validation", "Confirm Password  $result")
                }
            }
        }
    }
}