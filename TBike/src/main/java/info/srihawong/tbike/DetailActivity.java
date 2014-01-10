package info.srihawong.tbike;

import android.app.Activity;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.TextView;


/**
 * Created by Demo on 1/9/14 AD.
 */
public class DetailActivity extends Activity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        TextView textview = (TextView)findViewById(R.id.textView);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.layout.open_left,R.layout.close_left);

    }
}
