package edu.niit.ydkf.grand_prix.common.adapter;

import android.animation.Animator;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.TextView;

import com.avos.avoscloud.AVObject;
import com.facebook.drawee.view.SimpleDraweeView;
import com.marshalchen.ultimaterecyclerview.UltimateRecyclerviewViewHolder;
import com.marshalchen.ultimaterecyclerview.UltimateViewAdapter;
import com.marshalchen.ultimaterecyclerview.animators.internal.ViewHelper;

import java.util.List;

import edu.niit.ydkf.grand_prix.R;
import edu.niit.ydkf.grand_prix.module.index.EventDetailedActivity;


/**
 * Created by liuhaitian on 16/2/26.
 */
public class SimpleAnimationAdapter extends UltimateViewAdapter<RecyclerView.ViewHolder> {
    List<AVObject> lists;

    public SimpleAnimationAdapter(List<AVObject> lists, Context context) {
        this.context = context;
        this.lists = lists;
    }

    public void setData(List<AVObject> lists) {
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
            AVObject avObject = lists.get(customHeaderView != null ? position - 1 : position);
            ((ViewHolder) holder).tv_recyclerview_title.setText(avObject.getString("eventname"));
            ((ViewHolder) holder).tv_recyclerview_location.setText(avObject.getString("locantion"));
            ((ViewHolder) holder).tv_recyclerview_time.setText(avObject.getString("starttime"));
            ((ViewHolder) holder).tv_recyclerview_title.setText(avObject.getString("eventname"));
            ((ViewHolder) holder).tv_recyclerview_type.setText(avObject.getString("eventtype"));
            ((ViewHolder) holder).tv_recyclerview_renshu.setText(avObject.getString("number"));
//            ((ViewHolder) holder).tv_recyclerview_distance.setText(avObject.getString("distance"));
            Uri uri = Uri.parse(avObject.getString("ThumbnailUrl"));
            ((ViewHolder) holder).my_image_view.setImageURI(uri);
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
                .inflate(R.layout.recycler_view_adapter, parent, false);
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

    public void insert(AVObject avObject, int position) {
        insertInternal(lists, avObject, position);
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

        TextView tv_recyclerview_title;
        SimpleDraweeView my_image_view;
        TextView tv_recyclerview_location;
        TextView tv_recyclerview_time;
        TextView tv_recyclerview_type;
        TextView tv_recyclerview_distance;
        TextView tv_recyclerview_renshu;

        public ViewHolder(View itemView) {
            super(itemView);
//            itemView.setOnTouchListener(new SwipeDismissTouchListener(itemView, null, new SwipeDismissTouchListener.DismissCallbacks() {
//                @Override
//                public boolean canDismiss(Object token) {
//                    Logs.d("can dismiss");
//                    return true;
//                }
//
//                @Override
//                public void onDismiss(View view, Object token) {
//                   // Logs.d("dismiss");
//                    remove(getPosition());
//
//                }
//            }));
            tv_recyclerview_renshu = (TextView) itemView.findViewById(R.id.tv_recyclerview_renshu);
            tv_recyclerview_title = (TextView) itemView.findViewById(
                    R.id.tv_recyclerview_title);
            tv_recyclerview_location = (TextView) itemView.findViewById(
                    R.id.tv_recyclerview_location);
            tv_recyclerview_time = (TextView) itemView.findViewById(
                    R.id.tv_recyclerview_time);
            tv_recyclerview_type = (TextView) itemView.findViewById(
                    R.id.tv_recyclerview_type);
            tv_recyclerview_distance = (TextView) itemView.findViewById(
                    R.id.tv_recyclerview_distance);
            my_image_view = (SimpleDraweeView) itemView.findViewById(R.id.my_image_view);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, EventDetailedActivity.class);
                    intent.putExtra("id", lists.get(getAdapterPosition()).getObjectId());
                    intent.putExtra("number", tv_recyclerview_renshu.getText());
                    context.startActivity(intent);
                }
            });

        }
    }

    @Override
    public int getItemCount() {
        return getAdapterItemCount();
    }

    public AVObject getItem(int position) {
        if (customHeaderView != null)
            position--;
        if (position < lists.size())
            return lists.get(position);
        else return null;
    }
}
