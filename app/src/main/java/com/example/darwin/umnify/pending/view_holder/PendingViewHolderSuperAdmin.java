package com.example.darwin.umnify.pending.view_holder;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.darwin.umnify.R;

/**
 * Created by darwin on 9/21/17.
 */

public class PendingViewHolderSuperAdmin extends RecyclerView.ViewHolder {

    private TextView pendingContentView;
    private TextView pendingAuthorView;
    private TextView pendingDateView;
    private Button pendingAllowButton;
    private CardView pendingContainer;

    public PendingViewHolderSuperAdmin(LayoutInflater inflater, ViewGroup parent, int layoutId){
        super(inflater.inflate(layoutId, parent, false));

        pendingContentView = (TextView) itemView.findViewById(R.id.content);
        pendingAuthorView = (TextView) itemView.findViewById(R.id.author);
        pendingAllowButton = (Button) itemView.findViewById(R.id.allow);
        pendingDateView = (TextView) itemView.findViewById(R.id.date);
        pendingContainer = (CardView) itemView.findViewById(R.id.card_container);

    }

    public TextView getPendingContentView() {
        return pendingContentView;
    }

    public TextView getPendingAuthorView() {
        return pendingAuthorView;
    }

    public Button getPendingAllowButton() {
        return pendingAllowButton;
    }

    public TextView getPendingDateView() {
        return pendingDateView;
    }

    public CardView getPendingContainer() {
        return pendingContainer;
    }
}
