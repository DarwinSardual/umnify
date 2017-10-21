package com.example.darwin.umnify.feed.notifications.view_holder;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 9/16/17.
 */

public class NotificationViewHolder extends RecyclerView.ViewHolder {

    private ImageView notificationImageView;
    private TextView notificationTitleView;
    private TextView notificationContentView;
    private TextView notificationDateView;
    private View notificationContainer;

    public NotificationViewHolder(LayoutInflater inflater, ViewGroup parent, int layoutId){
        super(inflater.inflate(layoutId, parent, false));

        notificationImageView = (ImageView) itemView.findViewById(R.id.author_image);
        notificationTitleView = (TextView) itemView.findViewById(R.id.title);
        notificationContentView = (TextView) itemView.findViewById(R.id.content);
        notificationDateView = (TextView) itemView.findViewById(R.id.date);
        notificationContainer = (View) itemView.findViewById(R.id.container);

    }

    public ImageView getNotificationImageView() {
        return notificationImageView;
    }

    public TextView getNotificationTitleView() {
        return notificationTitleView;
    }

    public TextView getNotificationContentView() {
        return notificationContentView;
    }

    public TextView getNotificationDateView() {
        return notificationDateView;
    }

    public View getNotificationContainer() {
        return notificationContainer;
    }
}
