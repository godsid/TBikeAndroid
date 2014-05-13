package info.srihawong.tbike;


import android.app.Activity;
import android.content.Intent;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;

import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.androidquery.AQuery;
import com.androidquery.callback.AjaxCallback;
import com.androidquery.callback.AjaxStatus;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class MainActivity extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;
    private PlaceholderFragment placeholderFragment;

    String[] sectionArray;
    private Integer sectionSelectIndex = 0;
    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    private Long doubleBackToExitPressedOnce = (long)0;

    public MyGoogleAnalytics googleAnalytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Google Analytic Tracking
        googleAnalytics = new MyGoogleAnalytics(this);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);


        sectionArray =  getResources().getStringArray(R.array.title_section);

        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));


        if (checkPlayServices()) {

        }
    }

    @Override
    protected void onResume() {
        Log.d("tui","OnResume");
        super.onResume();
    }

    @Override
    protected void onResumeFragments() {
        Log.d("tui","OnResumeFragments");
        super.onResumeFragments();
        restoreActionBar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        sectionSelectIndex = position;
        position = position + 1;
        // update the main content by replacing fragments

        if(position==3){ //ซอยวัชรพล
            Intent openDetail = new Intent(getApplicationContext(),DetailActivity.class);
            openDetail.putExtra("topic_id",474812);
            startActivity(openDetail);

        }else if(position==5){ //Favorite
            this.openFavoritesActivity();
        }else{
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, placeholderFragment.newInstance(position))
                    .commit();
        }
    }

    public void onSectionAttached(int number) {
        mTitle = sectionArray[number-1];
        googleAnalytics.trackPage(mTitle.toString());
    }

    public void restoreActionBar() {

        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    private boolean checkPlayServices(){
        //int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        /*if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i("tui", "This device is not supported.");

                //finish();
            }
            return false;
        }*/
        return true;
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
        }else if(id == R.id.action_about){
            Intent aboutIntent = new Intent(getApplicationContext(),AboutActivity.class);
            startActivity(aboutIntent);
            return true;
        }else if(id == R.id.action_refresh || id == R.id.action_navigation_refresh){
            onNavigationDrawerItemSelected(sectionSelectIndex);
        }


        return super.onOptionsItemSelected(item);
    }


    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment implements OnRefreshListener{
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";
        private static final String ARG_SECTION_FORUM_ID = "forum_id";
        private static final String ARG_SECTION_TOPIC_ID = "topic_id";
        private static final String ARG_SECTION_STICKY = "sticky";
        private ListView topicListView;
        private ProgressBar progressBar;
        private EditText searchText;
        private ArrayList<TopicListItem> topicListItems = new ArrayList<TopicListItem>();
        private TopicListAdapter topicListAdapter;
        private TopicListItem chooseTopicItem;

        private PullToRefreshLayout mPullToRefreshLayout;

        private String forumID ;
        private String topicID ;
        private Boolean isSticky;
        private FavoritesDB favoritesDB;

        private static int cacheTime;

        private JSONArray resultTopicArray;
        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            String forumID;
            Boolean isSticky = false;
            switch(sectionNumber){
                case 1:
                    forumID = "153";
                    break;
                case 4:
                    forumID = "153";
                    isSticky = true;
                    break;
                case 2:
                default:
                    forumID = "3";
                    break;
            }
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            args.putString(ARG_SECTION_FORUM_ID, forumID);
            args.putBoolean(ARG_SECTION_STICKY,isSticky);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public void onViewCreated(View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);

            ViewGroup viewGroup = (ViewGroup) view;
            mPullToRefreshLayout = new PullToRefreshLayout(viewGroup.getContext());

            ActionBarPullToRefresh.from(getActivity())
                    .insertLayoutInto(viewGroup)
                    .theseChildrenArePullable(R.id.progressBar, R.id.topicListView, android.R.id.empty)
                    .listener(this)
                    .setup(mPullToRefreshLayout);
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {

            /*
            open detail from share url */
            Intent intent = getActivity().getIntent();
            String action = intent.getAction();
            if(Intent.ACTION_VIEW.equals(action)) {
                Log.d("tui", intent.getDataString());
                int topicId = Integer.valueOf(intent.getData().getQueryParameter("t"));
                Intent openDetail = new Intent(getActivity().getApplicationContext(),DetailActivity.class);
                openDetail.putExtra("topic_id",topicId);
                startActivity(openDetail);
                getActivity().overridePendingTransition(R.layout.transition_fromright,R.layout.transition_toleft);
            }
            cacheTime = getResources().getInteger(R.integer.cache_time);
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            topicListView = (ListView) rootView.findViewById(R.id.topicListView);
            progressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
            searchText = (EditText) rootView.findViewById(R.id.listview_search);
            forumID = getArguments().getString(ARG_SECTION_FORUM_ID);
            topicID = getArguments().getString(ARG_SECTION_TOPIC_ID,null);
            isSticky = getArguments().getBoolean(ARG_SECTION_STICKY, false);
            favoritesDB = new FavoritesDB(rootView.getContext());
            loadData();

            searchText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {

                }
                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
                   searchData();
                }
                @Override
                public void afterTextChanged(Editable editable) {
                }
            });
            return rootView;
        }

        private void searchData(){
            ArrayList<TopicListItem> searchTopicItems = new ArrayList<TopicListItem>();
            for(int j=0,k=topicListItems.size();j<k;j++){
                if(topicListItems.get(j).getTitle().contains(searchText.getText().toString().trim())){
                    searchTopicItems.add(topicListItems.get(j));
                }
            }
            topicListAdapter = new TopicListAdapter(getActivity().getBaseContext(),searchTopicItems);
            topicListView.setAdapter(topicListAdapter);
            topicListAdapter.notifyDataSetChanged();
        }
        private void loadData(){
            loadData(false);
        }
        private void loadData(Boolean refresh){
            AQuery aq = new AQuery(getActivity());
            String apiUrl = getResources().getString(R.string.api_forum);
            long cache = cacheTime*1000;
            if(refresh){
                cache = -1;
            }
            apiUrl+= "?f="+forumID;
            if(isSticky){
                apiUrl+="&s=1";
            }
            Log.d("tui","GET:"+(apiUrl));
            progressBar.setVisibility(View.VISIBLE);
            //resultTopicArray = new ApiJsonData(apiUrl).getJsonObject();

            aq.ajax(apiUrl,JSONObject.class,cache, new AjaxCallback<JSONObject>() {
                @Override
                public void callback(String url, JSONObject resp, AjaxStatus status) {
                    try {
                        //Log.d("tui",String.valueOf(resp.getJSONArray("result").length()));
                        resultTopicArray = resp.getJSONArray("result");
                        if (resultTopicArray.length()>0) {
                            topicListItems.clear();

                            for(int i=0,j=resultTopicArray.length();i<j;i++){
                                if(!isSticky && resultTopicArray.getJSONObject(i).getInt("sticky")==1){
                                    continue;
                                }

                                topicListItems.add(new TopicListItem(
                                        resultTopicArray.getJSONObject(i).getInt("f_id"),
                                        resultTopicArray.getJSONObject(i).getInt("topic_id"),
                                        resultTopicArray.getJSONObject(i).getInt("user_id"),
                                        resultTopicArray.getJSONObject(i).getString("user_name"),
                                        resultTopicArray.getJSONObject(i).getString("title"),
                                        resultTopicArray.getJSONObject(i).getString("createdate"),
                                        resultTopicArray.getJSONObject(i).getInt("sticky")
                                ));
                            }
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    topicListAdapter = new TopicListAdapter(getActivity().getBaseContext(),topicListItems);
                    topicListView.setAdapter(topicListAdapter);
                    registerForContextMenu(topicListView);
                    //getActivity().setContentView(topicListView);

                    topicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
                        @Override
                        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                            chooseTopicItem = topicListItems.get(position);
                            return false;
                        }
                    });

                    topicListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                            chooseTopicItem  = topicListItems.get(position);
                            Intent openDetail = new Intent(getActivity().getApplicationContext(),DetailActivity.class);
                            openDetail.putExtra("topic_id",chooseTopicItem.getTopicId());
                            openDetail.putExtra("title",chooseTopicItem.getTitle());
                            startActivity(openDetail);
                            getActivity().overridePendingTransition(R.layout.transition_fromright,R.layout.transition_toleft);
                        }
                    });
                    progressBar.setVisibility(View.INVISIBLE);
                    //progressDialog.dismiss();
                    if(mPullToRefreshLayout.isRefreshing()){
                        mPullToRefreshLayout.setRefreshComplete();
                    }
                }
            });
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            getActivity().getMenuInflater().inflate(R.menu.options,menu);
            if(favoritesDB.isFavorite(String.valueOf(chooseTopicItem.getTopicId()))) {
                menu.findItem(R.id.option_favorite).setTitle(R.string.option_remove_favorites);
            }else{
                menu.findItem(R.id.option_favorite).setTitle(R.string.option_add_favorites);
            }
            menu.setHeaderTitle(R.string.option_title);
        }

        //Context Menu
        @Override
        public boolean onContextItemSelected(MenuItem item) {
            String itemTitle = item.getTitle().toString();
            int id = item.getItemId();

            if(itemTitle.equals(getResources().getString(R.string.option_add_favorites))){
                favoritesDB.add(chooseTopicItem.getForumId(), chooseTopicItem.getTopicId(), chooseTopicItem.getUserId(), chooseTopicItem.getUsername(), chooseTopicItem.getTitle(), chooseTopicItem.getSticky(), chooseTopicItem.getCreateDate());
                Toast.makeText(getView().getContext(),getResources().getString(R.string.option_add_favorites),Toast.LENGTH_LONG).show();
            }else if(itemTitle.equals(getResources().getString(R.string.option_remove_favorites))){
                favoritesDB.delete(chooseTopicItem.getTopicId());
                Toast.makeText(getView().getContext(),getResources().getString(R.string.option_remove_favorites),Toast.LENGTH_LONG).show();
            }else if(id == R.id.action_openthaimtb){
                openDetailOnThaiMTB();
            }else if(id == R.id.action_social_share){
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "TBike Share");
                sendIntent.putExtra(Intent.EXTRA_TEXT,chooseTopicItem.getTitle()+"\n"+ getString(R.string.api_topic_original)+"?t="+String.valueOf(chooseTopicItem.getTopicId()));
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, getResources().getString(R.string.share_title)));
            }
            return super.onContextItemSelected(item);
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            try {
                ((MainActivity) activity).onSectionAttached(
                        getArguments().getInt(ARG_SECTION_NUMBER));
            }catch (Exception e){

            }
        }

        @Override
        public void onRefreshStarted(View view) {
            loadData(true);
        }

        public void success(String result){
            Log.d("tui",result);
        }

        private void openDetailOnThaiMTB(){
            Intent openDetail = new Intent(getActivity().getApplicationContext(),DetailActivity.class);
            openDetail.putExtra("topic_id",Integer.valueOf(chooseTopicItem.getTopicId()));
            openDetail.putExtra("title",chooseTopicItem.getTitle());
            openDetail.putExtra("original",true);
            startActivity(openDetail);
            getActivity().overridePendingTransition(R.layout.transition_fromright,R.layout.transition_toleft);
        }
    }
    // End PlaceholderFragment class

    private void  openFavoritesActivity(){
        Intent openFavorites = new Intent(getApplicationContext(),FavoritesActivity.class);
        startActivity(openFavorites);
        overridePendingTransition(R.layout.transition_fromright,R.layout.transition_toleft);
    }

    @Override
    public void onBackPressed() {
        if(doubleBackToExitPressedOnce > System.currentTimeMillis()){
            super.onBackPressed();
            return;
        }else{
            doubleBackToExitPressedOnce = System.currentTimeMillis()+ Util.TOAST_LENGTH_LONG_TIME;
            Toast.makeText(this,R.string.toast_back_to_close,Toast.LENGTH_LONG).show();
        }

    }
}
// End MainActivity class
