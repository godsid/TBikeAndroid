package info.srihawong.tbike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 * Created by Banpot.S on 10/1/2557.
 */
public class DetailActivity extends Activity{
    WebView webView;
    ProgressDialog progressDialog;
    WebViewClient webViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        progressDialog = new ProgressDialog(DetailActivity.this,R.style.TransparentProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setIndeterminateDrawable(R.style.TransparentProgressDialog);

        progressDialog.show();


        webView = (WebView) findViewById(R.id.webView);
        //WebSettings webSettings = webView.getSettings();

        webViewClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();
            }
        };

        webView.setWebViewClient(webViewClient);

        Integer topicId = getIntent().getIntExtra("topic_id",0);
        Boolean isOriginal = getIntent().getBooleanExtra("original",false);
        String apiUrl;
        if(topicId!=0){
            //Point screenPoint = new Point();
            DisplayMetrics metrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(metrics);

            if(isOriginal){
                apiUrl = getResources().getString(R.string.api_topic_original)+"?t="+String.valueOf(topicId);
            }else{
                apiUrl = getResources().getString(R.string.api_topic)+"?t="+String.valueOf(topicId)+"&w="+String.valueOf(metrics.widthPixels);
            }

            Log.d("tui", String.valueOf(apiUrl));
            webView.loadUrl(apiUrl);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        overridePendingTransition(R.layout.transition_fromleft, R.layout.transition_toright);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.action_refresh){
            progressDialog.show();
            webView.reload();
        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgress(){

    }
    private void hideProgress(){

    }
}
