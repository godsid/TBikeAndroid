package info.srihawong.tbike;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
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
import android.support.v7.app.ActionBarActivity;
import android.widget.Toast;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;

/**
 * Created by Banpot.S on 10/1/2557.
 */
public class DetailActivity extends ActionBarActivity implements OnRefreshListener{

    private PullToRefreshLayout mPullToRefreshLayout;

    WebView webView;
    ProgressDialog progressDialog;
    WebViewClient webViewClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.layout_detail);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        progressDialog = new ProgressDialog(DetailActivity.this,R.style.TransparentProgressDialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setMessage("Loading.....");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        //progressDialog.setIndeterminateDrawable(R.style.TransparentProgressDialog);

        progressDialog.show();

        webView = (WebView) findViewById(R.id.webView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setAppCacheEnabled(true);
        //webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);

        webViewClient = new WebViewClient(){
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressDialog.dismiss();

                // If the PullToRefreshAttacher is refreshing, make it as complete
                if (mPullToRefreshLayout.isRefreshing()) {
                    mPullToRefreshLayout.setRefreshComplete();
                    Toast.makeText(getApplicationContext(),R.string.refresh_completed,Toast.LENGTH_LONG);
                }
            }

            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {

                if(url.matches(".*&p=[0-9]+")){
                    progressDialog.show();
                }
                return super.shouldOverrideUrlLoading(view, url);
            }
        };


        webView.setWebViewClient(webViewClient);
        webView.canGoBack();
        webView.canGoForward();

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
    public void onRefreshStarted(View view) {
        progressDialog.show();
        webView.reload();
    }

    @Override
    public void onBackPressed() {
        try {
            if (webView.getUrl().matches(".*&p=[0-9]+")) {
                webView.goBack();
            } else {
                super.onBackPressed();
                overridePendingTransition(R.layout.transition_fromleft, R.layout.transition_toright);
            }
        }catch (Exception e){
            super.onBackPressed();
            overridePendingTransition(R.layout.transition_fromleft, R.layout.transition_toright);
        }

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
        }else if(id == R.id.action_social_share){
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_SUBJECT, "TBike Share");
            sendIntent.putExtra(Intent.EXTRA_TEXT,getIntent().getStringExtra("title")+" "+ getString(R.string.api_topic_original)+"?t="+String.valueOf(getIntent().getIntExtra("topic_id",0)));
            sendIntent.setType("text/plain");
            startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share_title)));

        }
        return super.onOptionsItemSelected(item);
    }

    private void showProgress(){

    }
    private void hideProgress(){

    }
}
