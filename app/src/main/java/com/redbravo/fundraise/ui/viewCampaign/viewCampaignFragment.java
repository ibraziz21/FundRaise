package com.redbravo.fundraise.ui.viewCampaign;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.redbravo.fundraise.R;
import com.redbravo.fundraise.ui.notifications.NotificationsViewModel;

public class viewCampaignFragment {
    private NotificationsViewModel viewCampaignViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.viewcampaign_fragment, container, false);


        return root;
    }
}
