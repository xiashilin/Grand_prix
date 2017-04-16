package edu.niit.ydkf.grand_prix.common.adapter;

import android.animation.Animator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.animators.internal.ViewHelper;

import java.util.List;

import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.common.ChatObject;


/**
 * Created by liuhaitian on 16/2/26.
 */
public class ChatAnimationAdapter extends UltimateViewAdapter<RecyclerView.ViewHolder> {
    List<ChatObject> lists;

    public ChatAnimationAdapter(List<ChatObject> lists, Context context) {
        this.context = context;
        this.lists = lists;
    }

    public void setData(List<ChatObject> lists) {
        this.lists = lists;
    }

    private int mDuration = 300;
    private Interpolator mInterpolator = new LinearInterpolator();
    private int mLastPosition = 5;

    private boolean isFirstOnly = true;
    private Context context;


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (position < getItemCount() && (customHeaderView != null ? position <= lists.size() :
                position < lists.size()) && (customHeaderView != null ? position > 0 : true)) {
            ChatObject chatObject = lists.get(customHeaderView != null ? position -
                    1 :
                    position);
            ((ViewHolder) holder).tv_item_im_username.setText(chatObject.getName());
//            Uri uri = Uri.parse(avObject.getString("ThumbnailUrl"));
//            ((ViewHolder) holder).my_image_view.setImageURI(uri);
            //((ViewHolder) holder).textViewSample.setText(lists.get(customHeaderView != null ? position - 1 : position));
            // ((ViewHolder) holder).itemView.setActivated(selectedItems.get(position, false));
        }
        if (!isFirstOnly || position > mLastPosition) {
            for (Animator anim : getAdapterAnimations(holder.itemView, AdapterAnimationType.ScaleIn)) {
                anim.setDuration(mDuration).start();
                anim.setInterpolator(mInterpolator);
            }
            mLastPosition = position;
        } else {
            ViewHelper.clear(holder.itemView);
        }
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(View view) {
        return new UltimateRecyclerviewViewHolder(view);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_listview, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void toggleSelection(int pos) {
        super.toggleSelection(pos);
    }

    @Override
    public void setSelected(int pos) {
        super.setSelected(pos);
    }

    @Override
    public void clearSelection(int pos) {
        super.clearSelection(pos);
    }

    @Override
    public int getAdapterItemCount() {
        return lists.size();
    }

    public void insert(ChatObject chatObject, int position) {
        insertInternal(lists, chatObject, position);
    }

    public void remove(int position) {
        removeInternal(lists, position);
    }

    @Override
    public long generateHeaderId(int position) {
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent) {
        return null;
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    class ViewHolder extends UltimateRecyclerviewViewHolder {

        TextView tv_item_im_username;

        public ViewHolder(View itemView) {
            super(itemView);

//            tv_recyclerview_renshu = (TextView) itemView.findViewById(R.id.tv_recyclerview_renshu);
            tv_item_im_username = (TextView) itemView.findViewById(R.id
                    .tv_item_im_username);
//            itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(context, EventDetailedActivity.class);
//                    intent.putExtra("id", lists.get(getAdapterPosition()).getObjectId());
//                    intent.putExtra("number", tv_recyclerview_renshu.getText());
//                    context.startActivity(intent);
//                }
//            });

        }
    }

    @Override
    public int getItemCount() {
        return getAdapterItemCount();
    }

    public ChatObject getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < lists.size())
            return lists.get(position);
        else return null;
    }
}
