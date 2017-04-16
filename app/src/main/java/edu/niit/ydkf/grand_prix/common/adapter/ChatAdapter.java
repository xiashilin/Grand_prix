package edu.niit.ydkf.grand_prix.common.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.ChatObject;

/**
 * Created by liuhaitian on 16/3/22.
 */
public class ChatAdapter extends BaseAdapter {
    private List<ChatObject> list;
    private Context context;
    private String hint;

    public ChatAdapter(Context context, List<ChatObject> list) {
        this.context = context;
        this.list = list;
    }

    public ChatAdapter(Context context) {
        list = new ArrayList<>();
        this.context = context;
    }

    public ChatAdapter(Context context, String hint) {
        list = new ArrayList<>();
        this.context = context;
        this.hint = hint;
    }

    public void changeData(List<ChatObject> list) {
        this.list = list;
        this.notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_listview, null);
            viewHolder = new ViewHolder();
            viewHolder.tv_name = (TextView) convertView.findViewById(R.id.tv_item_im_username);
            viewHolder.sdv_icon = (SimpleDraweeView) convertView.findViewById(R.id.sdv_icon);
            viewHolder.tv_item_hint = (TextView) convertView.findViewById(R.id.tv_item_hint);
            convertView.setTag(viewHolder);
        } else viewHolder = (ViewHolder) convertView.getTag();
        viewHolder.tv_name.setText(list.get(position).getName());
        Uri uri = Uri.parse(list.get(position).getUser().getString("iconurl"));
        viewHolder.sdv_icon.setImageURI(uri);
        viewHolder.tv_item_hint.setText(hint);
        return convertView;
    }

    private class ViewHolder {
        TextView tv_name, tv_item_hint;
        SimpleDraweeView sdv_icon;
    }
}
