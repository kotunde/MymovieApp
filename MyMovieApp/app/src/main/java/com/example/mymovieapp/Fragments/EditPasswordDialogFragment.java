package com.example.mymovieapp.Fragments;


import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.mymovieapp.R;

// ...



public class EditPasswordDialogFragment extends DialogFragment
{

    private EditText mEditText;
    private EditPasswordDialogListener callback;


    public interface EditPasswordDialogListener
    {
        void methodToPassData(String inputText);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            callback = (EditPasswordDialogListener) getTargetFragment();
        }
        catch (Exception e)
        {
            throw new ClassCastException("Calling Fragment must implement OnAddFriendListener");
        }
    }



    public EditPasswordDialogFragment()
    {

        // Empty constructor is required for DialogFragment

        // Make sure not to add arguments to the constructor

        // Use `newInstance` instead as shown below
    }



    public static EditPasswordDialogFragment newInstance(String title)
    {

        EditPasswordDialogFragment frag = new EditPasswordDialogFragment();

        Bundle args = new Bundle();

        args.putString("title", title);

        frag.setArguments(args);

        return frag;

    }



    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,

                             Bundle savedInstanceState)
    {
        View view =  inflater.inflate(R.layout.fragment_edit_password_dialog, container);

        Button btn = view.findViewById(R.id.button2);

        final EditText et = view.findViewById(R.id.editText);
        final EditText et2 = view.findViewById(R.id.editText2);

        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                //Toast.makeText(getContext(),et.getText().toString() + " " + et2.getText().toString(), Toast.LENGTH_SHORT).show();

                if(et.getText().toString().equals(et2.getText().toString()))
                {
                    callback.methodToPassData(et.getText().toString());
                    getDialog().dismiss();
                }
                else
                {
                    Toast.makeText(getContext(),"Password change failed, bad input!", Toast.LENGTH_SHORT).show();
                    et.setError("Passwords do no match!");
                    et2.setError("Try it again!");
                }
            }
        });

        return view;
    }





    @Override

    public void onViewCreated(View view, @Nullable Bundle savedInstanceState)
    {

        super.onViewCreated(view, savedInstanceState);

        // Get field from view

        //mEditText = (EditText) view.findViewById(R.id.txt_your_name);

        // Fetch arguments from bundle and set title

        String title = getArguments().getString("title", "Enter Name");

        getDialog().setTitle(title);

        // Show soft keyboard automatically and request focus to field

        //mEditText.requestFocus();

        getDialog().getWindow().setSoftInputMode(

                WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
    }

}
