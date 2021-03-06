package com.redbravo.fundraise.ui.home;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import com.parse.ParseFile;
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
        final View root = inflater.inflate(R.layout.fragment_home, container, false);
        //Recyclerview
       listView=root.findViewById(R.id.list);

        //layoutmanager
       listView.setLayoutManager(new LinearLayoutManager(getActivity()));
        final ArrayList<String> campaign = new ArrayList<>();
        final ArrayList<String>desc = new ArrayList<>();
        final ArrayList<Bitmap>image = new ArrayList<>();
    ParseQuery<ParseObject> query = ParseQuery.getQuery("campaigndata");


        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    if (objects.size() > 0) {
                        for (final ParseObject campaigndata : objects) {
                            try {
                                ParseFile parseFile = campaigndata.getParseFile("Image");
                                byte[] data = parseFile.getData();
                                Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
                                image.add(bitmap);
                            } catch (Exception i) {
                                i.printStackTrace();
                            }


                           campaign.add(String.valueOf(campaigndata.get("campaignName")));
                            desc.add(campaigndata.get("shortDescription").toString());

                        }
                        adapter=new MyAdapter(getContext(),campaign,desc,image);

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