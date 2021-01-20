package com.ahsailabs.beritakita.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.ahsailabs.beritakita.DetailActivity;
import com.ahsailabs.beritakita.R;
import com.ahsailabs.beritakita.configs.Config;
import com.ahsailabs.beritakita.ui.home.adapters.NewsAdapter;
import com.ahsailabs.beritakita.ui.home.models.News;
import com.ahsailabs.beritakita.ui.home.models.NewsListResponse;
import com.ahsailabs.beritakita.utils.HttpUtil;
import com.ahsailabs.beritakita.utils.InfoUtil;
import com.ahsailabs.beritakita.utils.SwipeRefreshLayoutUtil;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.ParsedRequestListener;
import com.facebook.shimmer.ShimmerFrameLayout;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    private RecyclerView rvList;
    private ArrayList<News> newsList;
    private NewsAdapter newsAdapter;

    private ProgressBar pbLoadingIndicator;
    private LinearLayout llLoadingPanel;

    private SwipeRefreshLayout swipeRefreshLayout;
    private SwipeRefreshLayoutUtil swipeRefreshLayoutUtil;

    private ShimmerFrameLayout sflLoading;

    private HomeViewModel homeViewModel;

    private String keyword = "";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadViews(view);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setupViews();
        setupListener();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        swipeRefreshLayoutUtil.refreshNow();
    }

    @Override
    public void onPause() {
        super.onPause();
    }


    @Override
    public void onStop() {
        super.onStop();
    }

    private void loadData() {
        showLoading();
        AndroidNetworking.post(Config.getNewsListUrl())
                .setOkHttpClient(HttpUtil.getCLient(getActivity()))
                .addBodyParameter("groupcode", Config.GROUP_CODE)
                .addBodyParameter("keyword", keyword)
                .setTag("newslist")
                .setPriority(Priority.HIGH)
                .build()
                .getAsObject(NewsListResponse.class, new ParsedRequestListener<NewsListResponse>() {
                    @Override
                    public void onResponse(NewsListResponse response) {
                        if(response.getStatus() == 1){
                            List<News> resultList = response.getData();
                            //TODO: show listview dengan data resultList
                            newsList.clear();
                            newsList.addAll(resultList);
                            newsAdapter.notifyDataSetChanged();
                        } else {
                            //TODO: show info error
                            InfoUtil.showToast(getActivity(), response.getMessage());
                        }
                        hideLoading();
                        swipeRefreshLayoutUtil.refreshDone();
                    }

                    @Override
                    public void onError(ANError anError) {
                        //TODO: show info error
                        InfoUtil.showToast(getActivity(), anError.getMessage());
                        hideLoading();
                        swipeRefreshLayoutUtil.refreshDone();
                    }
                });


    }

    private void setupListener() {
        //setup item click listener
        newsAdapter.setOnChildViewClickListener(new NewsAdapter.OnChildViewClickListener<News>() {
            @Override
            public void onClick(View view, News dataModel, int position) {
                DetailActivity.start(view.getContext(), dataModel.getId());
            }

            @Override
            public void onLongClick(View view, News dataModel, int position) {

            }
        });
    }

    private void setupViews() {
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));

        //rvList.setHasFixedSize(true);

        /*RecyclerView.ItemDecoration itemDecoration = new
                DividerItemDecoration(requireActivity(), DividerItemDecoration.VERTICAL);
        rvList.addItemDecoration(itemDecoration);*/

        //rvList.setItemAnimator(new SlideInUpAnimator());

        newsList = new ArrayList<>();
        newsAdapter = new NewsAdapter(newsList);
        rvList.setAdapter(newsAdapter);

        swipeRefreshLayoutUtil = SwipeRefreshLayoutUtil.init(swipeRefreshLayout, new Runnable() {
            @Override
            public void run() {
                loadData();
            }
        });
    }

    private void loadViews(View root) {
        rvList = root.findViewById(R.id.rvList);
        pbLoadingIndicator = root.findViewById(R.id.pbLoadingIndicator);
        llLoadingPanel = root.findViewById(R.id.llLoadingPanel);
        swipeRefreshLayout = root.findViewById(R.id.srlHome);
        sflLoading = root.findViewById(R.id.sflLoading);
    }

    private void showLoading(){
        rvList.setVisibility(View.GONE);
        //llLoadingPanel.setVisibility(View.VISIBLE);
        //pbLoadingIndicator.setProgress(50);

        sflLoading.startShimmer();
        sflLoading.setVisibility(View.VISIBLE);
    }

    private void hideLoading(){
        rvList.setVisibility(View.VISIBLE);
        //llLoadingPanel.setVisibility(View.GONE);
        //pbLoadingIndicator.setProgress(0);

        sflLoading.stopShimmer();
        sflLoading.setVisibility(View.GONE);
    }


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.home_menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.home_action_search).getActionView();
        searchView.setIconifiedByDefault(true);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                keyword = query;
                swipeRefreshLayoutUtil.refreshNow();
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                keyword = "";
                swipeRefreshLayoutUtil.refreshNow();
                return false;
            }
        });
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.home_action_refresh){
            swipeRefreshLayoutUtil.refreshNow();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroyView() {
        AndroidNetworking.cancel("newslist");
        super.onDestroyView();
    }
}