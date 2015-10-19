package com.example.androidmvpsample.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.androidmvpsample.R;
import com.example.androidmvpsample.domain.entities.Repo;

import java.util.List;

/**
 * Created by Bill on 16/10/2015.
 */
public class RepositoriesAdapter extends RecyclerView.Adapter<RepositoriesAdapter.RepositoryViewHolder> {

    public interface Callbacks {
        void onRepoClicked(int position);
    }

    private Context mContext;
    List<Repo> mRepos;
    private Callbacks mCallbacks;

    public RepositoriesAdapter(Context context, List<Repo> repos) {
        mContext = context;
        mRepos = repos;
    }

    @Override
    public RepositoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(
                R.layout.list_item_repo, parent, false);

        return new RepositoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RepositoryViewHolder holder, int position) {
        Repo repo = mRepos.get(position);

        holder.name.setText(repo.name);
        holder.description.setText(repo.description);
    }

    @Override
    public int getItemCount() {
        return mRepos == null ? 0 : mRepos.size();
    }

    public void setCallbacks(Callbacks callbacks) {
        this.mCallbacks = callbacks;
    }

    public class RepositoryViewHolder extends RecyclerView.ViewHolder implements
            View.OnClickListener {

        TextView name;
        TextView description;

        public RepositoryViewHolder(View view) {
            super(view);

            view.setClickable(true);
            view.setOnClickListener(this);
            name = (TextView) view.findViewById(android.R.id.text1);
            description = (TextView) view.findViewById(android.R.id.text2);
        }


        @Override
        public void onClick(View v) {
            if(mCallbacks != null) {
                mCallbacks.onRepoClicked(getAdapterPosition());
            }
        }
    }
}
