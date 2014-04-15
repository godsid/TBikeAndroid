package info.srihawong.tbike;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import uk.co.senab.actionbarpulltorefresh.extras.actionbarcompat.PullToRefreshLayout;
import uk.co.senab.actionbarpulltorefresh.library.ActionBarPullToRefresh;
import uk.co.senab.actionbarpulltorefresh.library.listeners.OnRefreshListener;


public class FavoritesActivity extends ActionBarActivity implements OnRefreshListener {

    FavoritesDB favoritesDB;
    ArrayList<TopicListItem> favoritesListItems;
    TopicListAdapter topicListAdapter;
    ListView favoritesListView;
    int chooseItem;
    private PullToRefreshLayout mPullToRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);
        mPullToRefreshLayout = (PullToRefreshLayout) findViewById(R.id.layout_favorite);
        ActionBarPullToRefresh.from(this)
                .allChildrenArePullable()
                .listener(this)
                .setup(mPullToRefreshLayout);

        favoritesListView = (ListView) findViewById(R.id.favoriteslistView);
        setTitle(R.string.title_activity_favorites);
        favoritesListItems = new ArrayList<TopicListItem>();
        favoritesDB = new FavoritesDB(getApplicationContext());
        topicListAdapter = new TopicListAdapter(getBaseContext(),favoritesListItems);
        favoritesListView.setAdapter(topicListAdapter);
        registerForContextMenu(favoritesListView);

        favoritesListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent openDetail = new Intent(getApplicationContext(),DetailActivity.class);
                openDetail.putExtra("topic_id", favoritesListItems.get(i).getTopicId());
                openDetail.putExtra("title",favoritesListItems.get(i).getTitle());
                startActivity(openDetail);
                overridePendingTransition(R.layout.transition_fromright, R.layout.transition_toleft);
            }
        });
        favoritesListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                chooseItem = i;
                return false;
            }
        });
        getFavorites();
    }

    private void getFavorites(){
        ArrayList<FaviriteData> faviriteDatas =  favoritesDB.getLimit(10,0);
        favoritesListItems.clear();
        for (int i=0,j = faviriteDatas.size();i < j;i++ ){
            favoritesListItems.add(new TopicListItem(
                    Integer.valueOf(faviriteDatas.get(i).getForum_id()),
                    Integer.valueOf(faviriteDatas.get(i).getTopic_id()),
                    Integer.valueOf(faviriteDatas.get(i).getUser_id()),
                    faviriteDatas.get(i).getUsername(),
                    faviriteDatas.get(i).getTitle(),
                    faviriteDatas.get(i).getTopic_create_time(),
                    Integer.valueOf(faviriteDatas.get(i).getSticky())
            ));
        }
        topicListAdapter.notifyDataSetChanged();
        if(mPullToRefreshLayout.isRefreshing()){
            mPullToRefreshLayout.setRefreshComplete();
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.favorites, menu);
        //getMenuInflater().inflate(R.menu.detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        //super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.options, menu);
        menu.findItem(R.id.option_favorite).setTitle(R.string.option_remove_favorites);
        menu.setHeaderTitle(R.string.option_title);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // Remove from Favorites
        if(item.getTitle().equals(getString(R.string.option_remove_favorites))){
            favoritesDB.delete(favoritesListItems.get(chooseItem).getTopicId());
            favoritesListItems.remove(chooseItem);
            topicListAdapter.notifyDataSetChanged();
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.option_remove_favorites), Toast.LENGTH_LONG).show();
        // Open On ThaiMTB
        }else if(item.getTitle().equals(getString(R.string.option_openthaimtb))){
            Intent openDetail = new Intent(getApplicationContext(),DetailActivity.class);
            openDetail.putExtra("topic_id",Integer.valueOf(favoritesListItems.get(chooseItem).getTopicId()));
            openDetail.putExtra("original",true);
            startActivity(openDetail);
            overridePendingTransition(R.layout.transition_fromright,R.layout.transition_toleft);

        }
        return super.onContextItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.layout.transition_fromleft, R.layout.transition_toright);
    }

    @Override
    public void onRefreshStarted(View view) {
        getFavorites();
    }
}
