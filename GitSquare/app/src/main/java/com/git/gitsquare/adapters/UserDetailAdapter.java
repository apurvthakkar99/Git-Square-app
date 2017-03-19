package com.git.gitsquare.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.git.gitsquare.R;
import com.git.gitsquare.model.UserDetail;

import java.util.ArrayList;

/**
 * Created by DELL on 3/19/2017.
 */

public class UserDetailAdapter extends BaseAdapter {

    Context mContext;
    ArrayList<UserDetail> arrUserDetail;
    private static final int SAVE_SEARCH_LIST = 100;


    public UserDetailAdapter(Context context, ArrayList<UserDetail> array) {
        this.arrUserDetail = array;
        this.mContext = context;
    }

    @Override
    public int getCount() {
        return arrUserDetail.size();
    }

    @Override
    public Object getItem(int i) {
        return arrUserDetail.get(i);
    }

    @Override
    public long getItemId(int i) {
        return arrUserDetail.indexOf(getItem(i));
    }

    @Override
    public int getViewTypeCount() {
        return getCount();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {

        final LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final UserDetailAdapter.ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.row_user_detail, viewGroup, false);
            holder = new UserDetailAdapter.ViewHolder(view);
            view.setTag(holder);
        } else {
            holder = (UserDetailAdapter.ViewHolder) view.getTag();
        }

        final UserDetail userDetail = arrUserDetail.get(i);

        holder.tvLoginName.setText(userDetail.getLogin());
        holder.tvContributors.setText(String.valueOf(userDetail.getContributions()));

        Glide.with(mContext)
                .load(arrUserDetail.get(i).getAvatarUrl())
                .into(holder.ivAvatar);

        holder.tvReposUrl.setPaintFlags( holder.tvReposUrl.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        holder.tvReposUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(userDetail.getReposUrl()));
                mContext.startActivity(i);
            }
        });

        return view;
    }



    private class ViewHolder {

        private TextView tvLoginName;
        private ImageView ivAvatar;
        private TextView tvContributors;
        private TextView tvReposUrl;

         ViewHolder(View v) {

            tvLoginName = (TextView) v.findViewById(R.id.tvLoginName);
            ivAvatar = (ImageView) v.findViewById(R.id.ivAvatar);
            tvContributors = (TextView) v.findViewById(R.id.tvContributors);
            tvReposUrl = (TextView) v.findViewById(R.id.tvReposURL);
        }
    }

}
