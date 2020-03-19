package com.redbravo.fundraise.ui.dashboard;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;
import com.redbravo.fundraise.R;

public class DashboardFragment extends Fragment {
EditText campaign;
EditText shortdesc;
EditText Amount;
EditText longdesc;
Button button;
    private DashboardViewModel dashboardViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        dashboardViewModel =
                ViewModelProviders.of(this).get(DashboardViewModel.class);
        View root = inflater.inflate(R.layout.fragment_dashboard, container, false);
            campaign=root.findViewById(R.id.campaign);
            shortdesc=root.findViewById(R.id.shortdesc);
            Amount=root.findViewById(R.id.amt);
            longdesc=root.findViewById(R.id.LongDesc);
            button=root.findViewById(R.id.addCamp);

            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String Campaign=campaign.getText().toString();
                    String shortDesc=shortdesc.getText().toString();
                   String X=Amount.getText().toString();
                   int amount=Integer.parseInt(X);
                   String longDesc=longdesc.getText().toString();

                    ParseObject data = new ParseObject("campaigndata");
                    data.put("campaignName", Campaign);
                    data.put("shortDescription",shortDesc);
                    data.put("amountRequired",amount);
                    data.put("longDescription", longDesc);
                    data.put("amountRaised", 0);
                    data.saveInBackground(new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            if (e==null){
                                Toast.makeText(getContext(), "Campaign Started Successfully" , Toast.LENGTH_SHORT).show();

                            }else {
                                e.printStackTrace();
                            }
                        }
                    });

                }
            });

        return root;
    }
}