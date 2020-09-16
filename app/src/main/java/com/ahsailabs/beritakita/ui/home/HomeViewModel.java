package com.ahsailabs.beritakita.ui.home;

import androidx.lifecycle.ViewModel;

import com.ahsailabs.beritakita.ui.home.models.News;

import java.util.ArrayList;

public class HomeViewModel extends ViewModel {
    public ArrayList<News> newsList;

    public HomeViewModel() {
        newsList = new ArrayList<>();
    }
}