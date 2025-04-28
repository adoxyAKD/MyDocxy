package com.example.mydocxy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mydocxy.apiDataManager.ui.ObjectListActivity
import com.example.mydocxy.cameraManager.ImagePickerActivity
import com.example.mydocxy.pdfManager.PDFViewerActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.auth.FirebaseAuth

class Homepage : AppCompatActivity() {

    private lateinit var btnPdfViewer: Button
    private lateinit var btnImageCapture: Button
    private lateinit var btnFetchApiData: Button
    private lateinit var btnOption4: Button
    private lateinit var btnOption5: Button
    private lateinit var btnLogout: Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_homepage)

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        btnLogout = findViewById(R.id.btnLogout)
        btnPdfViewer = findViewById(R.id.btnPdfViewer)
        btnImageCapture = findViewById(R.id.btnImageCapture)
        btnFetchApiData = findViewById(R.id.btnFetchApiData)

        btnPdfViewer.setOnClickListener {
            Log.d("HomeActivity", "PDF Viewer button clicked")
            Toast.makeText(this, "Opening PDF Viewer 📄", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, PDFViewerActivity::class.java))
        }

        btnImageCapture.setOnClickListener {
            Log.d("HomeActivity", "Image Capture button clicked")
            Toast.makeText(this, "Opening Image Capture 📸", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ImagePickerActivity::class.java))
        }

        btnFetchApiData.setOnClickListener {
            Log.d("HomeActivity", "Fetch API button clicked")
            Toast.makeText(this, "Fetching API..", Toast.LENGTH_SHORT).show()
            startActivity(Intent(this, ObjectListActivity::class.java))

        }


        btnLogout.setOnClickListener {
            logoutGoogleUser()
        }


    }

    // Sign out only the Google user
    fun logoutGoogleUser() {
        val googleSignInClient: GoogleSignInClient = GoogleSignIn.getClient(this, GoogleSignInOptions.DEFAULT_SIGN_IN)

        googleSignInClient.signOut().addOnCompleteListener {
            // Show a toast message to notify the user
            Toast.makeText(this, "You have been logged out from Google", Toast.LENGTH_SHORT).show()

            // Redirect back to the MainActivity
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)

            // Optionally finish the current activity (if you don't want the user to return here)
            if (true) {
                finish()
            }
        }
    }
}
