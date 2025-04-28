package com.example.mydocxy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.mydocxy.entity.AuthViewModel
import com.example.mydocxy.loginManager.UserDatabase
import com.example.mydocxy.loginManager.UserEntity
import com.example.mydocxy.pdfManager.PDFViewerActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private val RC_SIGN_IN = 100
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient

    private lateinit var authViewModel: AuthViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()
        authViewModel = ViewModelProvider(this).get(AuthViewModel::class.java)



        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Set up Google Sign-In button click listener
        val signInButton: Button = findViewById(R.id.sign_in_button)
        signInButton.setOnClickListener {
            signIn()
        }
    }

    // Start Google Sign-In Intent
    private fun signIn() {
        Log.d("FirebaseAuthActivity", "Starting Google Sign-In")
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // Handle the result of Google Sign-In
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign-In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)
                Log.d("FirebaseAuthActivity", "Google Sign-In success: ${account.displayName}")
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign-In failed
                Log.e("FirebaseAuthActivity", "Google Sign-In failed", e)
                Toast.makeText(this, "Google Sign-In failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Authenticate with Firebase using Google credentials
    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign-In was successful
                    Log.d("FirebaseAuthActivity", "Firebase Authentication successful")
                    val user = auth.currentUser
                    Toast.makeText(this, "Welcome ${user?.displayName}", Toast.LENGTH_SHORT).show()
                    //saveUserDetails(user)


                    // Save user details in the ViewModel (now separated from the activity)
                    authViewModel.saveUserDetails(user)

                    // Navigate to PDF Viewer Activity
                    val intent = Intent(this, Homepage::class.java)
                    startActivity(intent)
                    finish()


                } else {
                    // Sign-In failed
                    Log.e("FirebaseAuthActivity", "Firebase Authentication failed", task.exception)
                    Toast.makeText(this, "Authentication failed", Toast.LENGTH_SHORT).show()
                }
            }
    }

    // Save user details to Room Database
    private fun saveUserDetails(user: FirebaseUser?) {
        val userEntity = UserEntity(user?.uid ?: "", user?.displayName ?: "", user?.email ?: "")
        val userDao = UserDatabase.getDatabase(applicationContext).userDao()

        GlobalScope.launch {
            try {
                userDao.insert(userEntity)
                Log.d("FirebaseAuthActivity", "User details saved to Room DB")
            } catch (e: Exception) {
                Log.e("FirebaseAuthActivity", "Error saving user details", e)
            }
        }
    }
}