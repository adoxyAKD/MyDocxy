package com.example.mydocxy.pdfManager

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.mydocxy.R
import android.webkit.WebView
import android.webkit.WebViewClient

import android.content.Intent
import android.view.View
import android.widget.Button
import com.example.mydocxy.Homepage
import com.example.mydocxy.MainActivity

class PDFViewerActivity : AppCompatActivity() {

    private lateinit var webView: WebView
    private lateinit var btnClose: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pdfviewer)

        webView = findViewById(R.id.webView)
        btnClose = findViewById(R.id.btnClose)

        val pdfUrl = "https://fssservices.bookxpert.co/GeneratedPDF/Companies/nadc/2024-2025/BalanceSheet.pdf"
        val googleDocsUrl = "http://drive.google.com/viewerng/viewer?embedded=true&url=$pdfUrl"

        webView.settings.javaScriptEnabled = true
        webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)

                Toast.makeText(this@PDFViewerActivity, "PDF Loaded Successfully ✅", Toast.LENGTH_SHORT).show()
                btnClose.visibility = View.VISIBLE  // Show close button after PDF loads
            }
        }
        webView.loadUrl(googleDocsUrl)

        btnClose.setOnClickListener {
            val intent = Intent(this@PDFViewerActivity, Homepage::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
            startActivity(intent)
            finish()  // Close PDFViewerActivity
        }
    }
}


