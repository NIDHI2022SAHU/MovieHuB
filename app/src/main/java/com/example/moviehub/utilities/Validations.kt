package com.example.moviehub.utilities

import android.text.Editable
import android.text.TextWatcher
import android.util.Patterns
import android.widget.EditText
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout

class Validations {

    private var etName: TextInputEditText? = null
    private var etEmail: TextInputEditText? = null
    private var etMobile: TextInputEditText? = null
    private var etPassword: TextInputEditText? = null
    private var etCPass: TextInputEditText? = null

    private var textWatchers = mutableListOf<TextWatcher>()
    private val userNameRegex = Regex("[a-zA-Z ]+")

    // Validate username format
    fun nameValidation(nameText: TextInputEditText): Boolean {
        val name = nameText.text.toString()
        return when {
            name.isEmpty() -> {
                showError(nameText, "Please Fill Full Name")
                false
            }

            name.length < 3 -> {
                showError(nameText, "Username must be at least 3 characters")
                false
            }

            !name.matches(userNameRegex) -> {
                showError(nameText, "Invalid Username: only letters and spaces are allowed")
                false
            }
            else -> {
                clearError(nameText)
                true
            }
        }
    }

    // Validate email format
    fun emailValidation(emailEditText: TextInputEditText): Boolean {
        val email = emailEditText.text.toString()
        return when {
            email.isBlank() -> {
                showError(emailEditText, "Please fill Email")
                false
            }

            email.length < 5 -> {
                showError(emailEditText, "Email must be at least 5 characters")
                false
            }

            !email.contains("@".toRegex()) -> {
                showError(emailEditText, "Invalid Email: missing '@' symbol")
                false
            }

            !email.contains("\\.".toRegex()) -> {
                showError(emailEditText, "Invalid Email: missing '.' symbol")
                false
            }

            !Patterns.EMAIL_ADDRESS.matcher(email).matches() -> {
                showError(emailEditText, "Invalid Email: please check format")
                false
            }

            else -> {
                clearError(emailEditText)
                true
            }
        }
    }

    // Validate password strength
    fun passwordValidation(passwordEditText: TextInputEditText): Boolean {
        val password = passwordEditText.text.toString().trim()
        return if (password.isEmpty()) {
            showError(passwordEditText, "Please Fill Password")
            false
        } else if (password.length < 8) {
            showError(passwordEditText, "Password must be at least 8 characters")
            false
        } else if (password.firstOrNull { it.isDigit() } == null) {
            showError(passwordEditText, "Password must contain at least one digit")
            false
        } else if (password.filter { it.isLetter() }.firstOrNull { it.isUpperCase() } == null) {
            showError(passwordEditText, "Password must contain at least one uppercase letter")
            false
        } else if (password.filter { it.isLetter() }.firstOrNull { it.isLowerCase() } == null) {
            showError(passwordEditText, "Password must contain at least one lowercase letter")
            false
        } else if (password.firstOrNull { !it.isLetterOrDigit() } == null) {
            showError(passwordEditText, "Password must contain at least one special character")
            false
        } else {
            clearError(passwordEditText)
            true
        }
    }

    // Confirm password matches the original password
    fun confirmPassValidation(cpassText: TextInputEditText, passText: TextInputEditText): Boolean {
        val password = passText.text.toString().trim()
        val confirmPassword = cpassText.text.toString().trim()

        return when {
            confirmPassword.isEmpty() -> {
                showError(cpassText, "Please fill Again Password")
                false
            }

            confirmPassword.length < 8 -> {
                showError(cpassText, "Confirm Password must be at least 8 characters")
                false
            }

            confirmPassword != password -> {
                showError(cpassText, "Passwords do not match")
                false
            }

            else -> {
                clearError(cpassText)
                clearError(passText)
                true
            }
        }
    }

    // Validate mobile number format
    fun phoneValidation(mobileEditText: TextInputEditText): Boolean {
        val phone = mobileEditText.text.toString().trim()
        return when {
            phone.isEmpty() -> {
                showError(mobileEditText, "Please fill Mobile Number")
                false
            }
            phone.length != 10 -> {
                showError(mobileEditText, "Phone Number must be 10 digits")
                false
            }
            !phone.matches("\\d+".toRegex()) -> {
                showError(mobileEditText, "Mobile Number must contain only digits")
                false
            }
            else -> {
                clearError(mobileEditText)
                true
            }
        }
    }

    fun checkValidation(etText: TextInputEditText,value:String): Boolean {
        var result=false
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                when (value) {
                    "Email" -> {
                        result = emailValidation(etText)
                    }
                    "Password" -> {
                        result = passwordValidation(etText)
                    }
                    "Name" -> {
                        result = nameValidation(etText)
                    }
                    "Phone" -> {
                        result = phoneValidation(etText)
                    }
                }
            }
        }
        etText.addTextChangedListener(textWatcher)
        textWatchers.add(textWatcher)
        return result
    }
    fun checkValidation(etCText:TextInputEditText,etPText: TextInputEditText, value:String):Boolean {
        var result =false
        val textWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                result = if(value=="CPass"){
                    confirmPassValidation(etCText,etPText)
                }else
                    false
           }

        }
        etCText.addTextChangedListener(textWatcher)
        textWatchers.add(textWatcher)
        return result
    }
    fun validateUserInputForget(
        etEmail: TextInputEditText,
        etPassword: TextInputEditText,
        etCPass: TextInputEditText,
    ): Boolean {
        return emailValidation(etEmail) && passwordValidation(etPassword) && confirmPassValidation(
            etCPass,
            etPassword
        )
    }

    fun validateUserInputForget(
        etName: TextInputEditText,
        etEmail: TextInputEditText,
        etMobile: TextInputEditText,
        etPassword: TextInputEditText,
        etCPass: TextInputEditText,
    ): Boolean {
        return nameValidation(etName) && emailValidation(etEmail) && phoneValidation(etMobile) && passwordValidation(etPassword) && confirmPassValidation(
            etCPass,
            etPassword
        )
    }
    fun validateUserInputForget(
        etEmail: TextInputEditText,
        etPassword: TextInputEditText,
    ): Boolean {
        return emailValidation(etEmail) && passwordValidation(etPassword)

    }

    // Show error message for EditText views
    private fun showError(editText: EditText, errorMessage: String) {
        val parent = editText.parent.parent as? TextInputLayout
        parent?.error = errorMessage
    }

    // Clear error message for EditText views
    private fun clearError(editText: EditText) {
        val parent = editText.parent.parent as? TextInputLayout
        parent?.error = null
    }

    // Validate all user input fields
    fun removeTextWatchers() {
        etName?.removeTextChangedListener(textWatchers.first())
        etEmail?.removeTextChangedListener(textWatchers.first())
        etMobile?.removeTextChangedListener(textWatchers.first())
        etPassword?.removeTextChangedListener(textWatchers.first())
        etCPass?.removeTextChangedListener(textWatchers.first())
        textWatchers.clear()
    }
}