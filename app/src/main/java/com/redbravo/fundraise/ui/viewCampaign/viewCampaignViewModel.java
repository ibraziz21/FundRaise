package com.redbravo.fundraise.ui.viewCampaign;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class viewCampaignViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public viewCampaignViewModel() {

    }

    public LiveData<String> getText() {
        return mText;
    }
}

