package com.example.mymovieapp.Fragments;

import android.database.Cursor;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymovieapp.Helpers.DBAdapters.DBAdapterUsers;
import com.example.mymovieapp.R;


public class LoginFragment extends Fragment
{

    boolean inputError;
    EditText et_userName;
    EditText et_password;
    String str_username;
    String str_password;
    NavigationFragment navigationFragment;
    //int loginFailChance = 3;

    public LoginFragment()
    {
        // Required empty public constructor
    }

    public static LoginFragment newInstance(String param1, String param2)
    {
        LoginFragment fragment = new LoginFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        if (getArguments() != null)
        { }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        View retView= inflater.inflate(R.layout.fragment_login, container, false);
        et_userName = retView.findViewById(R.id.et_userName);
        et_password = retView.findViewById(R.id.et_password);
        Button btn_login = retView.findViewById(R.id.btn_login);

        //handle login button
        btn_login.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                String str_username = et_userName.getText().toString();
                String str_password = et_password.getText().toString();
                //check input
                inputError = false;
                if (TextUtils.isEmpty(str_username))
                {
                    et_userName.setError("Username required");
                    inputError = true;
                }
                if(TextUtils.isEmpty(str_password))
                {
                    et_password.setError("Passwords required");
                    inputError = true;
                }
                if(!inputError)
                {
                    AttemptLogin(str_username,str_password);
                }
            }
        });
        return retView;
    }

    //check if the username is already registered, if it is, check the password
    //if is not, will be registered now
    void AttemptLogin(String pUsername, String pPassword)
    {
        DBAdapterUsers db = new DBAdapterUsers(getActivity());
        //first time nobody registered //??/
        Cursor cursor = db.getDataUsers();
        boolean userAlreadyExists=false;
        try
        {
            while (cursor.moveToNext())
            {
                str_username = cursor.getString(cursor.getColumnIndex("username"));
                str_password = cursor.getString(cursor.getColumnIndex("password"));
                //if the user is already registered, we check the password
                if (str_username.equals(pUsername))
                {
                    userAlreadyExists = true;
                    if (str_password.equals(pPassword))
                    {
                        Toast.makeText(getActivity(), "Log in successfully",Toast.LENGTH_SHORT).show();
                        navigationFragment = new NavigationFragment();
                        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fg_placeholder,navigationFragment.newInstance(str_username,str_password));
                        fragmentTransaction.addToBackStack("navigation");
                        fragmentTransaction.commit();

                    }
                    else
                    {
                        et_password.setError("Incorrect password");
                        cursor.close();
                        break;
                    }
                }
                else
                {

                }
            }
        }finally
        {
            if(!userAlreadyExists)
            {
                //insert login data into DB
                db.insertUser(pUsername,pPassword);
                Toast.makeText(getActivity(), "Registered successfully",Toast.LENGTH_SHORT).show();
                FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.fg_placeholder,navigationFragment.newInstance(str_username,str_password));
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
            cursor.close();
            db.close();
        }
    }
}
