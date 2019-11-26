### Java 与 Javascript 通信

1. Js 方法
```
function callByAndroid(name){
      console.log("callByAndroid:"+name)
  }
```

2.Android 端
```
//第一个参数为方法名, 第二个参数为Js方法的入参
mAgentWeb.getJsEntraceAccess().quickCallJs("callByAndroid","AgentWeb");
```

结果
consoleMessage:callByAndroid:AgentWeb

### WebSettings 自定义设置
```
 mWebSettings = webView.getSettings();
        mWebSettings.setJavaScriptEnabled(true);
        mWebSettings.setSupportZoom(true);
        mWebSettings.setBuiltInZoomControls(false);
        mWebSettings.setSavePassword(false);
        if (AgentWebUtils.checkNetwork(webView.getContext())) {
            //根据cache-control获取数据。
            mWebSettings.setCacheMode(android.webkit.WebSettings.LOAD_DEFAULT);
        } else {
            //没网，则从本地获取，即离线加载
            mWebSettings.setCacheMode(android.webkit.WebSettings.LOAD_CACHE_ELSE_NETWORK);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            //适配5.0不允许http和https混合使用情况
            mWebSettings.setMixedContentMode(android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT >= 19) {
            webView.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        } else if (Build.VERSION.SDK_INT < 19) {
            webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        }

//        mWebSettings.setRenderPriority(android.webkit.AgentWebSettings.RenderPriority.HIGH);
        mWebSettings.setTextZoom(100);
        mWebSettings.setDatabaseEnabled(true);
        mWebSettings.setAppCacheEnabled(true);
        mWebSettings.setLoadsImagesAutomatically(true);
        mWebSettings.setSupportMultipleWindows(false);
        mWebSettings.setBlockNetworkImage(false);//是否阻塞加载网络图片  协议http or https
        mWebSettings.setAllowFileAccess(true); //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
        mWebSettings.setAllowFileAccessFromFileURLs(false); //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
        mWebSettings.setAllowUniversalAccessFromFileURLs(false);//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        mWebSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        if (Build.VERSION.SDK_INT >=19)
            mWebSettings.setLayoutAlgorithm(android.webkit.WebSettings.LayoutAlgorithm.SINGLE_COLUMN);
        else
            mWebSettings.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NORMAL);
        mWebSettings.setLoadWithOverviewMode(true);
        mWebSettings.setUseWideViewPort(true);
        mWebSettings.setDomStorageEnabled(true);
        mWebSettings.setNeedInitialFocus(true);
        mWebSettings.setDefaultTextEncodingName("utf-8");//设置编码格式
        mWebSettings.setDefaultFontSize(16);
        mWebSettings.setMinimumFontSize(12);//设置 WebView 支持的最小字体大小，默认为 8
        mWebSettings.setGeolocationEnabled(true);

        //
        String dir = AgentWebConfig.getCachePath(webView.getContext());

        LogUtils.i(TAG, "dir:" + dir + "   appcache:" + AgentWebConfig.getCachePath(webView.getContext()));
        //设置数据库路径  api19 已经废弃,这里只针对 webkit 起作用
        mWebSettings.setGeolocationDatabasePath(dir);
        mWebSettings.setDatabasePath(dir);
        mWebSettings.setAppCachePath(dir);

        //缓存文件最大值
        mWebSettings.setAppCacheMaxSize(Long.MAX_VALUE);
```

这里提供两种方式修改 Websettings
1.  获取Websettings
```
mAgentWeb.getAgentWebSettings().getWebSettings().setUserAgentString("agentweb");
```

2.  自定义重写toSetting方法
```
public class CustomSettings extends WebDefaultSettingsManager {
    protected CustomSettings() {
        super();
    }


    @Override
    public AgentWebSettings toSetting(WebView webView) {
        super.toSetting(webView);
        getWebSettings().setBlockNetworkImage(false);//是否阻塞加载网络图片  协议http or https
        getWebSettings().setAllowFileAccess(false); //允许加载本地文件html  file协议, 这可能会造成不安全 , 建议重写关闭
        getWebSettings().setAllowFileAccessFromFileURLs(false); //通过 file url 加载的 Javascript 读取其他的本地文件 .建议关闭
        getWebSettings().setAllowUniversalAccessFromFileURLs(false);//允许通过 file url 加载的 Javascript 可以访问其他的源，包括其他的文件和 http，https 等其他的源
        return this;
    }
}

mAgentWeb.setAgentWebWebSettings(传入CustomSettings)
```

### 文件下载
```
protected DownLoadResultListener mDownLoadResultListener = new DownLoadResultListener() {
        @Override
        public void success(String path) {
            //do you work
        }

        @Override
        public void error(String path, String resUrl, String cause, Throwable e) {
            //do you work
        }
    };

mAgentWeb.addDownLoadResultListener(mDownLoadResultListener)
```

mDownLoadResultListener监听不是每次用户触发下载文件,都会回调此方法的,
如果用户触发下载文件,而该文件又存在,那么 AgentWeb 会默认打开该文件,
如果文件不存在,AgentWeb 会创建一个下载任务,进行下载,下载结果会回调此接口。