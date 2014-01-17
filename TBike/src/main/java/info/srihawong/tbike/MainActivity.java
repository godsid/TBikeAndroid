package info.srihawong.tbike;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private PlaceholderFragment placeholderFragment;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(getApplicationContext(),"onResume",5000);
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        Toast.makeText(getApplicationContext(),"Fragement Resume",5000);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, placeholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }else if(id== R.id.action_about){
            Intent aboutIntent = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(aboutIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_FORUM_ID = "forum_id";
        private ListView topicListView;
        private ProgressBar progressBar;
        private ArrayList<TopicListItem> topicListItems = new ArrayList<TopicListItem>();
        private TopicListAdapter topicListAdapter;

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            String forumID;
            switch(sectionNumber){
                case 1:
                    forumID = "153";
                    break;
                case 2:
                default:
                    forumID = "3";
                    break;
            }
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_FORUM_ID,forumID);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            topicListView = (ListView) rootView.findViewById(R.id.topicListView);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);



            class TopicsJson extends AsyncTask<String, Integer, Long>{
                private JSONObject resultTopicJSON;
                //private ProgressDialog progressDialog = new ProgressDialog();
                @Override
                protected void onPreExecute() {
                    progressBar.setVisibility(View.VISIBLE);

                    //MainActivity.this
                    //ProgressDialog.show(get, "title", "message");
                   /* progressDialog.setMax(100);
                    progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
                    progressDialog.setTitle("Loading...");
                    progressDialog.setMessage("กำลังโหดอยู่รอซักครู่");
                              */
                    //super.onPreExecute();


                }



                @Override
                protected Long doInBackground(String... strings) {
                    String forumID = getArguments().getString(ARG_SECTION_FORUM_ID);
                    String apiUrl = getResources().getString(R.string.api_forum);
                    Log.d("tui","GET:"+(apiUrl+"?f="+forumID));
                    resultTopicJSON = new ApiJsonData((apiUrl+"?f="+forumID)).getJsonObject();
                    /*
                    try {
                        resultTopicJSON = new JSONObject("{\"status\":\"OK\",\"result\":[{\"createdate\":\"13 ก.ย. 2012 23:13\",\"f_id\":\"14\",\"topic_id\":\"596584\",\"title\":\"ชวนลงขัน/ตั้งค่าหัว  &quot;กองทุนให้รางวัลนำจับโจรทำร้ายและปล้นชาวจักรยาน&quot; ..... เปิดบัญชีรับโอนเงินแล้วครับ // update สมุด 16/10/55\",\"user_id\":\"57\",\"user_name\":\"nbt\"},{\"createdate\":\"29 ก.ย. 2010 11:21\",\"f_id\":\"3\",\"topic_id\":\"245525\",\"title\":\"== ผิดกติกาขาย แบนเตือน7 วัน ผู้ซื้อโปรดระวังจะติดต่อไม่ได้=\",\"user_id\":\"57\",\"user_name\":\"nbt\"},{\"createdate\":\"28 ก.ย. 2010 01:32\",\"f_id\":\"3\",\"topic_id\":\"245089\",\"title\":\"แนวทางในการดำเนินการเมื่อเกิดเหตุโดนฉ้อโกง\",\"user_id\":\"57\",\"user_name\":\"nbt\"}]}");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    */
                    return null;
                }
                @Override
                public void onPostExecute(Long aLong) {
                    super.onPostExecute(aLong);
                    Log.d("tui", "Receive data");

                    try {
                        JSONArray resultTopicArray = resultTopicJSON.getJSONArray("result");
                        if (resultTopicArray.length()>0) {
                            topicListItems.clear();
                            for(Integer i=0,j=resultTopicArray.length();i<j;i++){
                                if(resultTopicArray.getJSONObject(i).getInt("sticky")==1){
                                    continue;
                                }
                                topicListItems.add(new TopicListItem(
                                        resultTopicArray.getJSONObject(i).getInt("topic_id"),
                                        resultTopicArray.getJSONObject(i).getString("user_name"),
                                        resultTopicArray.getJSONObject(i).getString("title"),
                                        resultTopicArray.getJSONObject(i).getString("createdate")));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    topicListAdapter = new TopicListAdapter(getActivity().getBaseContext(),topicListItems);
                    topicListView.setAdapter(topicListAdapter);
                    topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            Intent openDetail = new Intent(getActivity().getApplicationContext(),DetailActivity.class);
                            openDetail.putExtra("topic_id",topicListItems.get(position).getTopicId());
                            openDetail.putExtra("title",topicListItems.get(position).getTitle());
                            startActivity(openDetail);
                            getActivity().overridePendingTransition(R.layout.transition_fromright,R.layout.transition_toleft);
                        }
                    });
                    progressBar.setVisibility(View.INVISIBLE);
                    //progressDialog.dismiss();

                }
            }

            new TopicsJson().execute();
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        public void success(String result){
            Log.d("tui",result);
        }
    }

}
