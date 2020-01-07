//USEFUL LINKS
//Switch between fragments without recreating the fragments
//https://medium.com/@oluwabukunmi.aluko/bottom-navigation-view-with-fragments-a074bfd08711

//youtube api key
//AIzaSyC52Fs1AuUX6AntXNdkzHQKmZq5B5n9l5c


//the movie db api key
//c5f034d2365ff3aaf5bc40944d249acc

package com.example.mymovieapp.Activities;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.mymovieapp.Fragments.LoginFragment;
import com.example.mymovieapp.R;
import com.example.mymovieapp.Services.UpdateDatabaseJobService;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        LoginFragment startFragment = new LoginFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.fg_placeholder,startFragment);
        fragmentTransaction.commit();

        scheduleJob();
    }


    public void scheduleJob()
    {
        ComponentName componentName = new ComponentName(this, UpdateDatabaseJobService.class);
        JobInfo jobInfo = new JobInfo.Builder(1234, componentName)
                .setRequiresCharging(false)
                .setPersisted(true)
                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                .setPeriodic(15* 60 *1000)
                .build();


        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.schedule(jobInfo);

        int resultCode = scheduler.schedule(jobInfo);

        if(resultCode == JobScheduler.RESULT_SUCCESS)
        {
            //Log.d("debug", "Job scheduled");
        }
        else
        {
            //Log.d("debug", "Job schedule failed!");
        }
    }

    public void cancelJob(View view)
    {
        JobScheduler scheduler = (JobScheduler) getSystemService(JOB_SCHEDULER_SERVICE);
        scheduler.cancel(1234);
        //Log.d("debug", "Job cancelled");
    }

    @Override
    public void onBackPressed()
    {
        if (getSupportFragmentManager().findFragmentByTag("FragmentC") != null)
        {
            // I'm viewing Fragment C
            getSupportFragmentManager().popBackStack("back",
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
        }
        else
        {
            super.onBackPressed();
        }
    }
}
