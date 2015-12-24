package com.life.me.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.life.me.R;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by cuiyang on 15/12/10.
 */
public class PopView_Adapter extends BaseAdapter {

    private List<String> stringList;
    private Context mContext;

    public PopView_Adapter(Context mContext, List<String> stringList) {
        this.stringList = stringList;
        this.mContext = mContext;

    }

    @Override
    public int getCount() {
        return stringList == null ? 0 : stringList.size();
    }

    @Override
    public Object getItem(int position) {
        return stringList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_txt, parent, false);
            holder.title = (TextView) convertView.findViewById(R.id.item_txt);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.title.setText(stringList.get(position));
        return convertView;
    }

    public class ViewHolder {
        TextView title;
    }
}
