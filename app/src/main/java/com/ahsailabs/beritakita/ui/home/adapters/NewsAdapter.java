package com.ahsailabs.beritakita.ui.home.adapters;

import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ahsailabs.beritakita.R;
import com.ahsailabs.beritakita.bases.BaseRecyclerViewAdapter;
import com.ahsailabs.beritakita.ui.home.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ahmad s on 02/09/20.
 */
public class NewsAdapter extends BaseRecyclerViewAdapter<News, NewsAdapter.NewsViewHolder> {
    public NewsAdapter(List<News> modelList) {
        super(modelList);
    }

    @Override
    protected int getLayout() {
        return R.layout.news_itemview;
    }

    @Override
    protected NewsViewHolder getViewHolder(View rootView) {
        return new NewsViewHolder(rootView);
    }

    @Override
    protected void doSettingViewWithModel(NewsViewHolder holder, News dataModel, int position) {
        holder.tvTitle.setText(dataModel.getTitle());
        holder.tvSummary.setText(dataModel.getSummary());
        holder.tvDate.setText(dataModel.getCreatedAt());
        holder.tvUser.setText(dataModel.getCreatedBy());
        if(TextUtils.isEmpty(dataModel.getPhoto())){
            holder.ivPhoto.setVisibility(View.GONE);
        } else {
            Picasso.get().load(dataModel.getPhoto()).into(holder.ivPhoto);
            holder.ivPhoto.setVisibility(View.VISIBLE);
        }
        setViewClickable(holder, holder.itemView);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder{
        private TextView tvTitle;
        private TextView tvSummary;
        private TextView tvDate;
        private TextView tvUser;
        private ImageView ivPhoto;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvSummary = itemView.findViewById(R.id.tvSummary);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvUser = itemView.findViewById(R.id.tvUser);
            ivPhoto = itemView.findViewById(R.id.ivPhoto);
        }
    }
}
