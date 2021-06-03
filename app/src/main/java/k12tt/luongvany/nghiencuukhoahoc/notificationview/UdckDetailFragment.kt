package k12tt.luongvany.nghiencuukhoahoc.notificationview

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.webkit.*
import androidx.lifecycle.LifecycleObserver
import k12tt.luongvany.nghiencuukhoahoc.common.BaseFragment
import k12tt.luongvany.nghiencuukhoahoc.databinding.UdckDetailLayoutBinding
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.HIDE_TOP
import k12tt.luongvany.nghiencuukhoahoc.router.NotificationRouter.Companion.SHOW_ALL


class UdckDetailFragment: BaseFragment(), LifecycleObserver {
    private lateinit var dataBinding: UdckDetailLayoutBinding
    private lateinit var webView: WebView
    private var webViewBundle: Bundle? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        super.onCreate(savedInstanceState)
        dataBinding = UdckDetailLayoutBinding.inflate(inflater, container, false)
        webView = dataBinding.udckWebview
        router.hideAppBarAndBottom(HIDE_TOP)
        webView.setLayerType(View.LAYER_TYPE_HARDWARE, null)
        webView.apply {
            settings.javaScriptEnabled = true
            settings.loadWithOverviewMode = true
            settings.useWideViewPort = true
            settings.loadsImagesAutomatically
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView?, url: String?) {
                    super.onPageFinished(view, url)
                    dataBinding.progressBarUdck.visibility = View.GONE
                }
                override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
                    return false
                }
            }
            canGoBack()
            setDownloadListener { url, _, _, _, _ ->
                val i = Intent(Intent.ACTION_VIEW)
                i.data = Uri.parse(url)
                startActivity(i)
            }
            setOnKeyListener(object: View.OnKeyListener{
                override fun onKey(v: View?, keyCode: Int, event: KeyEvent?): Boolean {
                    if (event != null) {
                        if (keyCode == KeyEvent.KEYCODE_BACK && event.action == MotionEvent.ACTION_UP && webView.canGoBack()) {
                            webView.goBack()
                            return true;
                        }
                    }
                    return false;
                }
            })
        if(webViewBundle==null){
            webView.loadUrl("kontum.udn.vn")

        } else{
            webView.restoreState(webViewBundle!!)
        }
        return dataBinding.root
    }
    }

    override fun onPause() {
        super.onPause()
        webViewBundle = Bundle()
        webView.saveState(webViewBundle!!)
    }
    override fun onDetach() {
        super.onDetach()
        router.showAppBarAndBottom(SHOW_ALL)
    }
}