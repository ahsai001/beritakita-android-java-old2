package com.ahsailabs.beritakita.ui.home.adapters;

import android.graphics.Color;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.ahsailabs.beritakita.R;
import com.ahsailabs.beritakita.bases.BaseRecyclerViewAdapter;
import com.ahsailabs.beritakita.ui.home.models.News;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by ahmad s on 02/09/20.
 */
public class NewsAdapter2 extends BaseRecyclerViewAdapter<News, NewsAdapter2.NewsViewHolder> {
    public NewsAdapter2(List<News> modelList) {
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
}
