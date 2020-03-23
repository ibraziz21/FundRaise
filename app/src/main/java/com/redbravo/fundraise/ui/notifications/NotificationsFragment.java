package com.redbravo.fundraise.ui.notifications;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.redbravo.fundraise.MainActivity;
import com.redbravo.fundraise.R;

public class NotificationsFragment extends Fragment {
Button logout;
TextView userEmail;
TextView username;
    private NotificationsViewModel notificationsViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notificationsViewModel =
                ViewModelProviders.of(this).get(NotificationsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifications, container, false);
       userEmail=root.findViewById(R.id.userEmail);
       username=root.findViewById(R.id.userNme);

        ParseUser user=ParseUser.getCurrentUser();
        ParseQuery<ParseUser> query=ParseQuery.getQuery("User");
        query.whereEqualTo("user",user);

       String mail=user.getEmail();
       String usern=user.getUsername();
       userEmail.setText(mail, TextView.BufferType.NORMAL);
       username.setText(usern, TextView.BufferType.NORMAL);

        logout=root.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // ParseUser.logOut();
                Intent intent=new Intent(getContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        return root;
    }
}