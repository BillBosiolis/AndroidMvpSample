package com.example.androidmvpsample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidmvpsample.R;
import com.example.androidmvpsample.domain.entities.Commit;

import java.util.List;

/**
 * Created by Bill on 19/10/2015.
 */
public class CommitsAdapter extends RecyclerView.Adapter<CommitsAdapter.CommitViewHolder> implements
        View.OnClickListener {

    private Context mContext;
    List<Commit> mCommits;

    public CommitsAdapter(Context context, List<Commit> commits) {
        mContext = context;
        mCommits = commits;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public CommitViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.list_item_repo, parent, false);
        view.setOnClickListener(this);

        return new CommitViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CommitViewHolder holder, int position) {
        Commit commit = mCommits.get(position);

        holder.sha.setText(commit.sha);
        holder.message.setText(commit.commitMessage);
    }

    @Override
    public int getItemCount() {
        return mCommits == null ? 0 : mCommits.size();
    }

    public class CommitViewHolder extends RecyclerView.ViewHolder {
        TextView sha;
        TextView message;

        public CommitViewHolder(View view) {
            super(view);
            sha = (TextView) view.findViewById(android.R.id.text1);
            message = (TextView) view.findViewById(android.R.id.text2);
        }
    }
}
