package com.life.me.adapter;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.BounceInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.life.me.R;
import com.life.me.entity.resultentity.Contains_keyWord_bean;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cuiyang on 15/12/10.
 */
public class PopView_Adapter extends RecyclerView.Adapter<PopView_Adapter.ViewHolder> {

    private List<Contains_keyWord_bean.ResListEntity> stringList;
    private Context mContext;
    private ClickListener clickListener;

    public PopView_Adapter(Context mContext, List<Contains_keyWord_bean.ResListEntity> stringList) {
        this.stringList = stringList;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(stringList.get(position).getResName() + "  " + stringList.get(position).getSinger());
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return stringList == null ? 0 : stringList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            title = (TextView) itemView.findViewById(R.id.item_txt);
        }

        @Override
        public void onClick(View v) {
            clickListener.onItemClick(getAdapterPosition(), v);
        }
    }

    public void setOnItemClickListener(ClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface ClickListener {
        void onItemClick(int position, View v);
    }
}
