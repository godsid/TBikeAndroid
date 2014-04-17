package info.srihawong.tbike;

import android.app.Activity;
import android.os.Bundle;

/**
 * Created by Demo on 1/14/14 AD.
 */
public class AboutActivity extends Activity {

    MyGoogleAnalytics googleAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        googleAnalytics = new MyGoogleAnalytics(this);
        googleAnalytics.trackPage(getResources().getString(R.string.title_activity_about));
        setContentView(R.layout.activity_about);
        setTitle(R.string.title_activity_about);

    }
}
