package info.srihawong.tbike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Banpot.S on 10/1/2557.
 */
public class DetailActivity extends Activity{
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);


        ProgressDialog progressDialog = new ProgressDialog(DetailActivity.this,R.style.TransparentProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setIndeterminateDrawable(R.style.TransparentProgressDialog);

        progressDialog.show();


        WebView webView = (WebView) findViewById(R.id.webView);
        //WebSettings webSettings = webView.getSettings();


        WebViewClient webViewClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.d("tui",url.toString());
            }
        };

        webView.setWebViewClient(webViewClient);

        Integer topicId = getIntent().getIntExtra("topic_id",0);

        if(topicId!=0){
            String apiUrl = getResources().getString(R.string.api_topic)+"?t="+String.valueOf(topicId);
            Log.d("tui", String.valueOf(apiUrl));
            webView.loadUrl(apiUrl);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.layout.transition_fromleft, R.layout.transition_toright);
    }
}
