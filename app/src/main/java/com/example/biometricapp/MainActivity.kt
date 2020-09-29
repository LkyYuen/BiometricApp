package com.example.biometricapp

import android.content.Intent
import android.hardware.biometrics.BiometricManager.Authenticators.BIOMETRIC_STRONG
import android.hardware.biometrics.BiometricManager.Authenticators.DEVICE_CREDENTIAL
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.biometric.BiometricManager
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import java.util.concurrent.Executor

class MainActivity : AppCompatActivity() {

    private lateinit var executor: Executor
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        executor = ContextCompat.getMainExecutor(this)
        biometricPrompt = BiometricPrompt(this, executor,
                object : BiometricPrompt.AuthenticationCallback() {
                    override fun onAuthenticationError(errorCode: Int,
                                                       errString: CharSequence) {
                        super.onAuthenticationError(errorCode, errString)
                        Toast.makeText(applicationContext,
                                "Authentication error: $errString", Toast.LENGTH_SHORT)
                                .show()
                    }

                    override fun onAuthenticationSucceeded(
                            result: BiometricPrompt.AuthenticationResult) {
                        super.onAuthenticationSucceeded(result)
                        Toast.makeText(applicationContext,
                                "Authentication succeeded!", Toast.LENGTH_SHORT)
                                .show()
                    }

                    override fun onAuthenticationFailed() {
                        super.onAuthenticationFailed()
                        Toast.makeText(applicationContext, "Authentication failed",
                                Toast.LENGTH_SHORT)
                                .show()
                    }
                })

        promptInfo = BiometricPrompt.PromptInfo.Builder()
                .setTitle("Biometric login for my app")
                .setSubtitle("Log in using your biometric credential")
                .setNegativeButtonText("Cancel")
                .build()

        val biometricLoginButton = findViewById<Button>(R.id.biometric_login)
        biometricLoginButton.setOnClickListener {
            val biometricManager = BiometricManager.from(this)
            when (biometricManager.canAuthenticate()) {
                BiometricManager.BIOMETRIC_SUCCESS -> {
                    Log.d("MY_APP_TAG", "App can authenticate using biometrics.")
                    biometricPrompt.authenticate(promptInfo)
                }
                BiometricManager.BIOMETRIC_ERROR_NO_HARDWARE -> {
                    Log.d("MY_APP_TAG", "No biometric features available on this device.")
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
                BiometricManager.BIOMETRIC_ERROR_HW_UNAVAILABLE -> {
                    Log.d("MY_APP_TAG", "Biometric features are currently unavailable.")
                    Toast.makeText(applicationContext, "Authentication failed",
                        Toast.LENGTH_SHORT)
                        .show()
                }
//                BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED -> {
//                    // Prompts the user to create credentials that your app accepts.
//                    val enrollIntent = Intent(Settings.ACTION_BIOMETRIC_ENROLL).apply {
//                        putExtra(Settings.EXTRA_BIOMETRIC_AUTHENTICATORS_ALLOWED,
//                                BIOMETRIC_STRONG or DEVICE_CREDENTIAL)
//                    }
//                    startActivityForResult(enrollIntent, REQUEST_CODE)
//                }
                else -> {
                    Toast.makeText(applicationContext, "No Biometric Available",
                        Toast.LENGTH_SHORT)
                        .show()
                }
            }

        }
    }
}