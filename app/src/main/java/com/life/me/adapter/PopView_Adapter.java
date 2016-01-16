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
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cuiyang on 15/12/10.
 */
public class PopView_Adapter extends RecyclerView.Adapter<PopView_Adapter.ViewHolder> {

    private List<String> stringList;
    private Context mContext;
    private ClickListener clickListener;
    private DecelerateInterpolator mDecelerateInterpolator = new DecelerateInterpolator(2f);
    private static final int PHOTO_ANIMATION_DELAY = 700;

    public PopView_Adapter(Context mContext, List<String> stringList) {
        this.stringList = stringList;
        this.mContext = mContext;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.title.setText(stringList.get(position));
        if (position <= 12) {
            animatePhoto(holder);
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return stringList == null ? 0 : stringList.size();
    }

    private void animatePhoto(ViewHolder holder) {
        long animationDelay = PHOTO_ANIMATION_DELAY + holder.getPosition() * 50;
        holder.title.animate()
                .scaleY(1)
                .setDuration(500)
                .setInterpolator(mDecelerateInterpolator)
                .setStartDelay(animationDelay)
                .start();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView title;

        public ViewHolder(View itemView) {
            super(itemView);
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
