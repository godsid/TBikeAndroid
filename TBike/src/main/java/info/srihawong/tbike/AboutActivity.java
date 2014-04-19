package info.srihawong.tbike;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

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
        TextView textVersion = (TextView)findViewById(R.id.text_version);
        textVersion.setText("Version: "+Util.getAppVersionName(this));

        setTitle(R.string.title_activity_about);

    }
}
