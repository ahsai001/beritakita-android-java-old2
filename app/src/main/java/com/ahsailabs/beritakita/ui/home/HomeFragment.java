package com.ahsailabs.beritakita.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ahsailabs.beritakita.R;
import com.ahsailabs.beritakita.configs.Config;
import com.ahsailabs.beritakita.ui.home.adapters.NewsAdapter;
import com.ahsailabs.beritakita.ui.home.models.News;
import com.ahsailabs.beritakita.ui.home.models.NewsListResponse;
import com.ahsailabs.beritakita.utils.HttpUtil;
import com.ahsailabs.beritakita.utils.InfoUtil;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvList;
    private ArrayList<News> newsList;
    private NewsAdapter newsAdapter;

    private ProgressBar pbLoadingIndicator;
    private LinearLayout llLoadingPanel;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        loadViews(root);

        return root;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setupViews();
        setupListener();
        loadData();
    }

    private void loadData() {
        showLoading();
        AndroidNetworking.post(Config.getNewsListUrl())
                .setOkHttpClient(HttpUtil.getCLient(getActivity()))
                .addBodyParameter("groupcode", Config.GROUP_CODE)
                .addBodyParameter("")
                .setTag("newslist")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(NewsListResponse.class, new ParsedRequestListener<NewsListResponse>() {
                    @Override
                    public void onResponse(NewsListResponse response) {
                        if(response.getStatus() == 1){
                            List<News> resultList = response.getData();
                            //TODO: show listview dengan data resultList
                            newsList.addAll(resultList);
                            newsAdapter.notifyDataSetChanged();
                        } else {
                            //TODO: show info error
                            InfoUtil.showToast(getActivity(), response.getMessage());
                        }
                        hideLoading();
                    }

                    @Override
                    public void onError(ANError anError) {
                        //TODO: show info error
                        InfoUtil.showToast(getActivity(), anError.getMessage());
                        hideLoading();
                    }
                });


    }

    private void setupListener() {

    }

    private void setupViews() {
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        rvList.setAdapter(newsAdapter);
    }

    private void loadViews(View root) {
        rvList = root.findViewById(R.id.rvList);
        pbLoadingIndicator = root.findViewById(R.id.pbLoadingIndicator);
        llLoadingPanel = root.findViewById(R.id.llLoadingPanel);
    }

    private void showLoading(){
        rvList.setVisibility(View.GONE);
        llLoadingPanel.setVisibility(View.VISIBLE);
        pbLoadingIndicator.setProgress(50);
    }

    private void hideLoading(){
        rvList.setVisibility(View.VISIBLE);
        llLoadingPanel.setVisibility(View.GONE);
        pbLoadingIndicator.setProgress(0);
    }
}