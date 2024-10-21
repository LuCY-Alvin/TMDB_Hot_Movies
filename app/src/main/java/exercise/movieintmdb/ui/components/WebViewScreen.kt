package exercise.movieintmdb.ui.components

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
fun WebViewScreen(authUrl:String?, onRequestTokenReceived: (String) -> Unit) {
    val context = LocalContext.current
    val webView = remember { WebView(context) }

    LaunchedEffect(authUrl) {
        authUrl?.let { webView.loadUrl(it) }
    }

    DisposableEffect(Unit) {
        val webViewClient = object : WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                super.onPageFinished(view, url)
                if (url != null && url.startsWith("myapp://auth")) {
                    Log.d("WebViewScreen", "Received URL: $url")
                    val uri = Uri.parse(url)
                    val token = uri.getQueryParameter("request_token")
                    if (token != null) {
                        onRequestTokenReceived(token)
                        Log.d("WebViewScreen", "token is not null")
                    }
                }
            }
        }

        webView.webViewClient = webViewClient
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        onDispose {
            webView.destroy()
        }
    }

    AndroidView(factory = { webView })
}