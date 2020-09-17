package com.ahsailabs.beritakita.ui.detail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.ahsailabs.beritakita.ui.detail.models.NewsDetail;

/**
 * Created by ahmad s on 16/09/20.
 */
public class DetailViewModel extends ViewModel {
    public MutableLiveData<NewsDetail> newsDetail;

    public DetailViewModel(){
        newsDetail = new MutableLiveData<>();
    }
}
