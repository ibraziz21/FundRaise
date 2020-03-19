package com.redbravo.fundraise.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.redbravo.fundraise.MyAdapter;
import com.redbravo.fundraise.R;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {
    RecyclerView listView;
    MyAdapter adapter;
    MyAdapter myAdapter;
    private HomeViewModel homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Recyclerview
       listView=root.findViewById(R.id.list);

        //layoutmanager
       listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ArrayList<String> campaign = new ArrayList<>();
        final ArrayList<String>desc = new ArrayList<>();

    ParseQuery<ParseObject> query = ParseQuery.getQuery("campaigndata");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (ParseObject campaigndata : objects) {

                           campaign.add(String.valueOf(campaigndata.get("campaignName")));
                            desc.add(campaigndata.get("shortDescription").toString());

                        }
                        adapter=new MyAdapter(getContext(),campaign,desc);

                        listView.setAdapter(adapter);



                    }
                } else {
                    e.printStackTrace();
                }
            }
        });



        return root;
    }
}