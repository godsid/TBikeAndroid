package info.srihawong.tbike;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Banpot.S on 10/1/2557.
 */
public class TopicListAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<TopicListItem> listData = new ArrayList<TopicListItem>();
    private LayoutInflater mInflater;

    public TopicListAdapter(Context context, ArrayList<TopicListItem> listData) {
        this.listData = listData;
        this.context = context;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public TopicListItem getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
           TopicListItemView topicListItemView;
           if(view==null){
               view = mInflater.inflate(R.layout.topics_adapter,null);
               topicListItemView = new TopicListItemView();
               topicListItemView.topicUsername = (TextView) view.findViewById(R.id.topicUsername);
               topicListItemView.topicTitle = (TextView) view.findViewById(R.id.topicTitle);
               topicListItemView.topicCreateDate = (TextView) view.findViewById(R.id.topicCreateDate);
               view.setTag(topicListItemView);
           }else{
               topicListItemView = (TopicListItemView) view.getTag();
           }
        topicListItemView.topicUsername.setText("โดย: "+listData.get(position).getUsername());
        topicListItemView.topicTitle.setText(listData.get(position).getTitle());
        topicListItemView.topicCreateDate.setText("เมื่อ: "+listData.get(position).getCreateDate());
        return view;
    }
}
