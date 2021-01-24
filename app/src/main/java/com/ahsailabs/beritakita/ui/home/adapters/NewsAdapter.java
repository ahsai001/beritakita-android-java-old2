package com.ahsailabs.beritakita.ui.home.adapters;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ahsailabs.beritakita.R;
import com.ahsailabs.beritakita.ui.home.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ahmad s on 02/09/20.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private List<News> modelList;

    public NewsAdapter(List<News> modelList) {
        this.modelList = modelList;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View rootView = LayoutInflater.from(parent.getContext()).inflate(R.layout.news_itemview, parent, false);
        return new NewsViewHolder(rootView);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News dataModel = modelList.get(position);

        holder.tvTitle.setText(dataModel.getTitle());
        holder.tvSummary.setText(dataModel.getSummary());
        holder.tvDate.setText(dataModel.getCreatedAt());
        holder.tvUser.setText(dataModel.getCreatedBy());
        if(TextUtils.isEmpty(dataModel.getPhoto())){
            holder.ivPhoto.setVisibility(View.GONE);

            holder.llTextPanel.setBackground(null);
            holder.tvTitle.setTextColor(Color.BLACK);
            holder.tvUser.setTextColor(Color.BLACK);
            holder.tvDate.setTextColor(Color.BLACK);
            holder.tvSummary.setTextColor(Color.BLACK);
        } else {
            Picasso.get().load(dataModel.getPhoto()).into(holder.ivPhoto);
            holder.ivPhoto.setVisibility(View.VISIBLE);

            holder.llTextPanel.setBackgroundColor(ContextCompat.getColor(holder.llTextPanel.getContext(), R.color.news_item_overlay_color));
            holder.tvTitle.setTextColor(Color.WHITE);
            holder.tvUser.setTextColor(Color.WHITE);
            holder.tvDate.setTextColor(Color.WHITE);
            holder.tvSummary.setTextColor(Color.WHITE);
        }
        setViewClickable(holder, holder.itemView);
    }

    @Override
    public int getItemCount() {
        return modelList.size();
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvSummary;
        private TextView tvDate;
        private TextView tvUser;
        private ImageView ivPhoto;
        private LinearLayout llTextPanel;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSummary = itemView.findViewById(R.id.tvSummary);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvUser = itemView.findViewById(R.id.tvUser);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
            llTextPanel = itemView.findViewById(R.id.llTextPanel);
        }
    }


    //give this class new power : clickable itemview
    public interface OnChildViewClickListener<News> {
        void onClick(View view, News dataModel, int position);
        void onLongClick(View view, News dataModel, int position);
    }

    private OnChildViewClickListener<News> onChildViewClickListener;

    public void setOnChildViewClickListener(OnChildViewClickListener<News> onChildViewClickListener){
        this.onChildViewClickListener = onChildViewClickListener;
    }

    protected void setViewClickable(final NewsViewHolder viewHolder, View view){
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(onChildViewClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    onChildViewClickListener.onClick(view, modelList.get(position), position);
                }
            }
        });
    }

    protected void setViewLongClickable(final NewsViewHolder viewHolder, View view){
        view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if(onChildViewClickListener != null) {
                    int position = viewHolder.getAdapterPosition();
                    onChildViewClickListener.onLongClick(view, modelList.get(position), position);
                    return true;
                }
                return false;
            }
        });
    }
}
