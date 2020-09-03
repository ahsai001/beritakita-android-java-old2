package com.ahsailabs.beritakita.bases;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahsailabs.beritakita.R;

import java.util.List;

/**
 * Created by ahsai on 5/30/2018.
 */

public abstract class BaseRecyclerViewAdapter<DM, HV extends RecyclerView.ViewHolder> extends RecyclerView.Adapter {
    private final int VIEW_ITEM = 1;
    private final int VIEW_PROG = 0;
    private boolean showProgress = false;

    private List<DM> modelList;
    protected abstract int getLayout();
    protected abstract HV getViewHolder(View rootView);
    protected abstract void doSettingViewWithModel(HV holder, DM dataModel, int position);

    public BaseRecyclerViewAdapter(List<DM> modelList) {
        this.modelList = modelList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_ITEM) {
            View rootView = LayoutInflater.from(parent.getContext()).inflate(getLayout(), parent, false);
            return getViewHolder(rootView);
        } else if(viewType == VIEW_PROG){
            View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.progressbar_vertical_center, parent, false);
            return new ProgressViewHolder(rootView);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if(position < modelList.size()) {
            doSettingViewWithModel((HV)holder, modelList.get(position), position);
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position < modelList.size())?VIEW_ITEM:VIEW_PROG;
    }

    @Override
    public int getItemCount() {
        return getAllItemCount();
    }

    private int getAllItemCount(){
        return modelList.size()+(showProgress?1:0);
    }

    public void showLoadMoreProgress(){
        showProgress = true;
        notifyDataSetChanged();
    }

    public void hideLoadMoreProgress(){
        showProgress = false;
        notifyDataSetChanged();
    }

    private static class ProgressViewHolder extends RecyclerView.ViewHolder {
        TextView descView;
        private ProgressViewHolder(View view) {
            super(view);
            descView = (TextView) view.findViewById(R.id.tvLoading);
        }
    }

    protected void setViewClickable(final HV viewHolder, View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onChildViewClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    onChildViewClickListener.onClick(view, (DM)modelList.get(position), position);
                }
            }
        });
    }

    protected void setViewLongClickable(final HV viewHolder, View view){
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onChildViewClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    onChildViewClickListener.onLongClick(view, (DM)modelList.get(position), position);
                    return true;
                }
                return false;
            }
        });
    }




    private OnChildViewClickListener<DM> onChildViewClickListener;

    public void setOnChildViewClickListener(OnChildViewClickListener<DM> onChildViewClickListener){
        this.onChildViewClickListener = onChildViewClickListener;
    }

    public interface OnChildViewClickListener<DM> {
        void onClick(View view, DM dataModel, int position);
        void onLongClick(View view, DM dataModel, int position);
    }
}
