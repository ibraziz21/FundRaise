package com.redbravo.fundraise.ui.dashboard;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.redbravo.fundraise.R;

import java.io.ByteArrayOutputStream;

public class DashboardFragment extends Fragment  implements View.OnClickListener{
    Bitmap bitmap;
ImageView upload;
EditText campaign;
EditText shortdesc;
EditText Amount;
EditText longdesc;
Button button;
    private DashboardViewModel dashboardViewModel;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);


        upload = view.findViewById(R.id.imageupload);
        campaign = view.findViewById(R.id.campaign);
        button = view.findViewById(R.id.addCamp);
        shortdesc= view.findViewById(R.id.shortdesc);
        longdesc = view.findViewById(R.id.LongDesc);
        Amount =view.findViewById(R.id.amt);

        upload.setOnClickListener(DashboardFragment.this);
        button.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        switch(view.getId()){

            case R.id.imageupload:
                //Getting permission to read the external storage from user
                if (android.os.Build.VERSION.SDK_INT >= 19 &&
                        ActivityCompat.checkSelfPermission(getContext(),
                                Manifest.permission.READ_EXTERNAL_STORAGE)
                                != PackageManager.PERMISSION_GRANTED) {
                    requestPermissions(new String[]
                                    {Manifest.permission.READ_EXTERNAL_STORAGE},
                            1000);
                }else{
                    getChosenImage();
                }

                break;

            case R.id.addCamp:

                if (bitmap != null){

                 String projectname = campaign.getText().toString();
                 String shortd = shortdesc.getText().toString();
                 String amts = Amount.getText().toString();
                 int amt = Integer.parseInt(amts);
                 String longd = longdesc.getText().toString();

                        //Convert the image to an array of bytes (easier to compress and upload it)
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);

                        byte[] bytes = byteArrayOutputStream.toByteArray();
                        ParseFile parseFile = new ParseFile("pic.png",bytes);
                        ParseObject parseObject = new ParseObject("campaigndata");
                        parseObject.put("campaignName", projectname);
                        parseObject.put("shortDescription",shortd);
                        parseObject.put("amountRequired",amt);
                        parseObject.put("longDescription",longd);
                        parseObject.put("username", ParseUser.getCurrentUser().getUsername());
                        parseObject.put("amountRaised",0);
                        parseObject.put("Image",parseFile);

                        final ProgressDialog dialog = new ProgressDialog(getContext());
                        dialog.setMessage("Posting...");
                        dialog.show();

                        parseObject.saveInBackground(new SaveCallback() {
                            @Override
                            public void done(ParseException e) {
                                if (e==null){
                                    Toast.makeText(getContext(),"Image Has Been Published !", Toast.LENGTH_LONG).show();
                                }else{
                                    Toast.makeText(getContext(),"Error , please try again later.", Toast.LENGTH_SHORT).show();
                                }
                                dialog.dismiss();
                            }
                        });


                } else{
                    Toast.makeText(getContext(), "You Must Choose A Picture", Toast.LENGTH_SHORT).show();
                }
                break;
        }


    }

    private void getChosenImage() {

        Toast.makeText(getContext(), "Choose A Picture", Toast.LENGTH_SHORT).show();
        Uri imageUri= ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,0);
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, 2000);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1000){
            if (grantResults.length > 0 && grantResults[0] ==
                    PackageManager.PERMISSION_GRANTED){
                getChosenImage();
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2000){

            if (resultCode == Activity.RESULT_OK) {

                //Do Something with your chosen image
                try{
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getActivity().getContentResolver().query(selectedImage,
                            filePathColumn, null, null, null);

                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    bitmap = BitmapFactory.decodeFile(picturePath);
                    upload.setImageBitmap(bitmap);

                }catch (Exception e){
                    e.printStackTrace();
                }
            }

        } else {
            Toast.makeText(getActivity(), "Try Again!!", Toast.LENGTH_SHORT)
                    .show();
        }

    }
}
