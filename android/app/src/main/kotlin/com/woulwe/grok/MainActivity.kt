package com.woulwe.grok

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.CookieManager
import android.webkit.GeolocationPermissions
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient

class MainActivity : Activity() {
    private var webView: WebView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        openGrok(savedInstanceState)
    }

    private fun openGrok(savedInstanceState: Bundle?) {
        try {
            val view = WebView(applicationContext)
            view.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )

            with(view.settings) {
                javaScriptEnabled = true
                domStorageEnabled = true
                databaseEnabled = true
                setGeolocationEnabled(true)
                cacheMode = WebSettings.LOAD_DEFAULT
                mediaPlaybackRequiresUserGesture = true
                userAgentString = "$userAgentString WoulweGrok/1.1"
                allowFileAccess = false
                allowContentAccess = true
                builtInZoomControls = false
                displayZoomControls = false
            }

            CookieManager.getInstance().setAcceptCookie(true)
            CookieManager.getInstance().setAcceptThirdPartyCookies(view, true)

            view.webViewClient = object : WebViewClient() {
                override fun onRenderProcessGone(
                    view: WebView,
                    detail: android.webkit.RenderProcessGoneDetail
                ): Boolean {
                    recreateWebView()
                    return true
                }
            }

            view.webChromeClient = object : WebChromeClient() {
                override fun onGeolocationPermissionsShowPrompt(
                    origin: String?,
                    callback: GeolocationPermissions.Callback?
                ) {
                    if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                    ) {
                        requestPermissions(
                            arrayOf(
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                            ),
                            LOCATION_REQUEST
                        )
                    }
                    callback?.invoke(origin, true, false)
                }
            }

            webView = view
            setContentView(view)

            if (savedInstanceState == null) {
                view.loadUrl(GROK_URL)
            } else {
                view.restoreState(savedInstanceState)
            }
        } catch (_: Throwable) {
            // If Android WebView is unavailable or fails to initialize,
            // open Grok in the installed browser instead of crashing.
            openInBrowser()
        }
    }

    private fun recreateWebView() {
        try {
            webView?.stopLoading()
            webView?.destroy()
        } catch (_: Throwable) {
        }
        webView = null
        openGrok(null)
    }

    private fun openInBrowser() {
        try {
            startActivity(
                Intent(Intent.ACTION_VIEW, Uri.parse(GROK_URL))
            )
        } catch (_: Throwable) {
        }
        finish()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        webView?.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    @Deprecated("Deprecated in Android API 33, retained for compatibility")
    override fun onBackPressed() {
        val view = webView
        if (view != null && view.canGoBack()) {
            view.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onDestroy() {
        try {
            webView?.stopLoading()
            webView?.destroy()
        } catch (_: Throwable) {
        }
        webView = null
        super.onDestroy()
    }

    companion object {
        private const val GROK_URL = "https://grok.com"
        private const val LOCATION_REQUEST = 1001
    }
}
