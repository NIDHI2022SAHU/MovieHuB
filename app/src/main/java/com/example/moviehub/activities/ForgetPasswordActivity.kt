package com.example.moviehub.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import com.example.moviehub.R
import com.example.moviehub.databinding.ActivityForgetPasswordBinding
import com.example.moviehub.utilities.Utility
import com.example.moviehub.utilities.Validations

class ForgetPasswordActivity : BaseActivity(),View.OnFocusChangeListener {

    private lateinit var binding: ActivityForgetPasswordBinding
    private var isAllFieldValid:Boolean=false
    private var utility: Utility = Utility()
    private val validation = Validations()
    private val view: View by lazy { findViewById(android.R.id.content) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgetPasswordBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupListeners()
    }

    private fun setupListeners() {
        binding.btnSave.setOnClickListener { handleUpdatePassword() }
        binding.forgetEmail.onFocusChangeListener = this
        binding.newPassword.onFocusChangeListener = this
        binding.newCPass.onFocusChangeListener = this
    }

    private fun handleUpdatePassword() {
        isAllFieldValid=validation.validateUserInputForget(binding.forgetEmail,binding.newPassword,binding.newCPass)
        if (isAllFieldValid) {
            val email = binding.forgetEmail.text.toString()
            val newPass = binding.newPassword.text.toString()

            dbViewModel.updateUserPassword(email, newPass) { id ->
                if (id > 0) {
                    utility.showToast(this@ForgetPasswordActivity, "Password updated successfully!")
                    startActivity(Intent(this, SignInActivity::class.java))
                    finish()
                } else {
                    utility.showSnackBar(view, "Email Does not Exist!!")
                }
            }
        } else {
            utility.showSnackBar(view, "Please Fill Correct Detail!")
        }
    }

    override fun onFocusChange(view: View?, hasFocus: Boolean) {
        if (hasFocus) {
            when (view?.id) {
                R.id.forget_email -> {
                    val result= validation.checkValidation(binding.forgetEmail, "Email")
                    utility.debug("Email validation:  $result")
                }

                R.id.new_password -> {
                    val result=validation.checkValidation(binding.newPassword, "Password")
                    utility.debug("Password validation: $result")
                }

                R.id.new_c_pass -> {
                   val result= validation.checkValidation(binding.newCPass, binding.newPassword, "CPass")
                    utility.debug("Confirm Password validation $result")
                }
            }
        }
    }
}