package com.example.mymovieapp.Fragments;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterUsers;
import com.example.mymovieapp.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;



public class ProfileFragment extends Fragment implements EditPasswordDialogFragment.EditPasswordDialogListener
{
    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";

    private String mUsername;
    private String mPassword;
    TextView tv_username;
    ImageView iv_profilePicture;
    private static final String IMAGE_DIRECTORY = "/demonuts";
    private int GALLERY = 1, CAMERA = 2;

    public ProfileFragment()
    {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance(String username, String password)
    {
        ProfileFragment fragment = new ProfileFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mUsername = getArguments().getString(ARG_USERNAME);
            mPassword = getArguments().getString(ARG_PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView = inflater.inflate(R.layout.fragment_profile, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Profile"); //does not work
        initView(retView);


        return  retView;
    }

    public void initView(View view)
    {
        tv_username = view.findViewById(R.id.tv_theUsername);
        iv_profilePicture = view.findViewById(R.id.iv_profilePicture);


        //TODO
        //get picture from db
        DBAdapterUsers db = new DBAdapterUsers(getActivity());
        Cursor cursor = db.getDataUsersByName(mUsername); //mUsername
        cursor.moveToFirst();
        byte[] profilePicture = cursor.getBlob(cursor.getColumnIndex("picture"));
        cursor.close();
        db.close();
        //if there is a picture already saved
        if (profilePicture.length>1)
        {
            iv_profilePicture.setImageBitmap(BitmapFactory.decodeByteArray(profilePicture,0,profilePicture.length));
        }

        //showing the password changing dialog fragment
        Button button = view.findViewById(R.id.btn);
        button.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                showEditPasswordDialogFragment();
            }
        });


        //set username
        tv_username.setText(mUsername);
        requestMultiplePermissions();
        iv_profilePicture.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                AlertDialog.Builder actionDialog = new AlertDialog.Builder(getContext());
                actionDialog.setTitle("Select Action");
                //actionDialog.setCancelable(false);
                String[] pictureDialogItems = {"Set Profile Picture", "Remove Profile Picture" };
                actionDialog.setItems(pictureDialogItems,
                        new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                //Log.d("DIALOG","Select action is activated");
                                switch (which)
                                {
                                    case 0:
                                        //Log.d("DIALOG","Set Porf Pic chosen");
                                        showPictureDialog();
                                        break;
                                    case 1:
                                        //Log.d("DIALOG","Remove Porf Pic chosen");
                                        iv_profilePicture.setImageResource(R.drawable.no_image);
                                        //TODO
                                        //get image from resource
                                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.no_image);
                                        //bitmap to byte[]
                                        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                                        bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                                        byte[] byteArray = outputStream.toByteArray();
                                        DBAdapterUsers db = new DBAdapterUsers(getActivity());
                                        db.updateUserProfilePicture(mUsername,byteArray);
                                        db.close();


                                        break;
                                }
                            }
                        });
                actionDialog.show();
            }
        });
    }
    //TODO
    //Show picture stuffs
    private void showPictureDialog()
    {
        AlertDialog.Builder sourceDialog = new AlertDialog.Builder(getContext());
        sourceDialog.setTitle("Select Source");
        String[] pictureDialogItems = {"Select photo from gallery", "Capture photo from camera" };
        sourceDialog.setItems(pictureDialogItems,
                new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        //Log.d("DIALOG","Select source activated");
                        switch (which)
                        {
                            case 0:
                                //Log.d("DIALOG","ChooseFromGallery chosen");
                                choosePhotoFromGallary();
                                break;
                            case 1:
                                //Log.d("DIALOG","takePhotoFromCamera chosen");
                                takePhotoFromCamera();
                                break;
                        }
                    }
                });
        sourceDialog.show();
    }

    public void choosePhotoFromGallary()
    {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        startActivityForResult(galleryIntent, GALLERY);
    }


    private void takePhotoFromCamera()
    {
        Intent intent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == getActivity().RESULT_CANCELED) {  return; }
        if (requestCode == GALLERY)
        {
            if (data != null)
            {
                Uri contentURI = data.getData();

                try
                {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), contentURI);
                    //TODO
                    //bitmap to byte[]
                    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.PNG,100,outputStream);
                    byte[] byteArray = outputStream.toByteArray();
                    DBAdapterUsers db = new DBAdapterUsers(getActivity());
                    db.updateUserProfilePicture(mUsername,byteArray);
                    db.close();

                    String path = saveImage(bitmap);
                    Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
                    iv_profilePicture.setImageBitmap(bitmap);

                }
                catch (IOException e)
                {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Failed!", Toast.LENGTH_SHORT).show();
                }
            }

        }
        else if (requestCode == CAMERA)
        {
            Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
            iv_profilePicture.setImageBitmap(thumbnail);
            saveImage(thumbnail);
            Toast.makeText(getContext(), "Image Saved!", Toast.LENGTH_SHORT).show();
        }
    }

    public String saveImage(Bitmap myBitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File wallpaperDirectory = new File(Environment.getDataDirectory()+ IMAGE_DIRECTORY);


        // have the object build the directory structure, if needed.
        if (!wallpaperDirectory.exists())
        {
            wallpaperDirectory.mkdirs();
        }

        try
        {
            File f = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".jpg");
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getContext(), new String[]{f.getPath()}, new String[]{"image/jpeg"}, null);
            fo.close();
            //Log.d("TAG", "File Saved::---&gt;" + f.getAbsolutePath());

            return f.getAbsolutePath();
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }

        return "";
    }

    private void  requestMultiplePermissions()
    {
        Dexter.withActivity(getActivity())
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report)
                    {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted())
                        {
                            Toast.makeText(getContext(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied())
                        {
                            // show alert dialog navigating to Settings
                            //openSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token)
                    {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getContext(), "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }


    void showEditPasswordDialogFragment()
    {
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        Fragment prev = getFragmentManager().findFragmentByTag("dialog");

        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        EditPasswordDialogFragment editPasswordDialogFragment = EditPasswordDialogFragment.newInstance("Some Title");
        editPasswordDialogFragment.setTargetFragment(this, 0);
        editPasswordDialogFragment.show(ft, "fragment_edit_name");
    }

    @Override
    public void methodToPassData(String newPassword)
    {
        DBAdapterUsers db = new DBAdapterUsers(getActivity());
        if(db.changePassword(mUsername,newPassword))
        {
            Cursor cursor = db.getPasswordByUsername(mUsername);

            if (!(cursor.moveToFirst()))
            {
                //Log.d("debug","cannot move to first");
            }
            else if(cursor.getCount() ==0)
            {
                //Log.d("debug","cursor is empty");
            }
            else
            {
                String str_password = cursor.getString(cursor.getColumnIndex("password"));
                //Log.d("debug","password : " + str_password);
                cursor.close();
                Toast.makeText(getContext(),"Password change successfull!", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(getContext(),"Password change failed, problem with database!", Toast.LENGTH_SHORT).show();
        }

    }
}
