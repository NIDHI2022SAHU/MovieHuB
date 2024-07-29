package com.example.moviehub.activities

import android.content.Intent
import android.graphics.Paint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import com.example.moviehub.R
import com.example.moviehub.databinding.ActivitySignInBinding
import com.example.moviehub.utilities.Constants
import com.example.moviehub.utilities.SharedPreferencesHelper
import com.example.moviehub.utilities.Utility
import com.example.moviehub.utilities.Validations

class SignInActivity : BaseActivity() ,View.OnFocusChangeListener{

    private lateinit var binding: ActivitySignInBinding
    private lateinit var pref: SharedPreferencesHelper
    private val validation = Validations()
    private var utility: Utility = Utility()
    private val views: View by lazy { findViewById(android.R.id.content) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySignInBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setUpPref()
        setupUI()
        setupListeners()

    }

    private fun setUpPref() {
        pref = SharedPreferencesHelper
        pref.initSharedPreferences(applicationContext)
    }

    private fun setupUI() {
        binding.textForgetPass.paintFlags = binding.textForgetPass.paintFlags or Paint.UNDERLINE_TEXT_FLAG
        binding.textSignUp.paintFlags = binding.textSignUp.paintFlags or Paint.UNDERLINE_TEXT_FLAG
    }

    private fun setupListeners() {
        binding.btnSignIn.setOnClickListener { handleSignIn() }
        binding.textForgetPass.setOnClickListener { navigateTo(ForgetPasswordActivity::class.java) }
        binding.textSignUp.setOnClickListener { navigateTo(SignUpActivity::class.java) }
        binding.etPassword.onFocusChangeListener = this
        binding.etEmail.onFocusChangeListener = this
    }


    private fun handleSignIn() {
        if (validateUserInput()) {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            dbViewModel.login(email, password) { isValid ->
                if (isValid) {
                    handleSuccessfulLogin()
                } else {
                    showErrorSnackBar("Email and Password Not Found !!")
                }
            }
        } else {
            showErrorSnackBar("Please fill in correct details")
        }
    }
    private fun navigateTo(activityClass: Class<*>) {
        startActivity(Intent(this, activityClass))
    }
    private fun validateUserInput(): Boolean {
        return validation.validateUserInputForget(binding.etEmail, binding.etPassword)
    }

    private fun handleSuccessfulLogin() {
        pref.saveValue(Constants.IS_LOGGED_IN, true)
        Toast.makeText(this, "Sign In successfully!", Toast.LENGTH_SHORT).show()
        navigateTo(MainActivity::class.java)
        finish()
    }

    private fun showErrorSnackBar(message: String) {
        utility.showSnackBar(views, message)
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus) {
            when (view?.id) {
                R.id.et_email -> {
                    val result= validation.checkValidation(binding.etEmail, "Email")
                    Log.d("Validation", "Email validation $result")
                }

                R.id.et_password -> {
                    val result=validation.checkValidation(binding.etPassword, "Password")
                    Log.d("Validation", " Password Validation: $result")
                }
            }
        }
    }
    override fun onDestroy() {
        super.onDestroy()
        validation.removeTextWatchers()
    }
}