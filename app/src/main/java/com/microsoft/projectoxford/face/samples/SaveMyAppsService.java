package com.microsoft.projectoxford.face.samples;

import android.app.ActivityManager;
import android.content.Context;
import android.os.IBinder;
import android.content.Intent;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class SaveMyAppsService extends android.app.Service
{
    private static final String TAG="rohansmessage";
    String CURRENT_PACKAGE_NAME = "com.example.neel.service";
    String lastAppPN = "";
    boolean noDelay = false;
    public static SaveMyAppsService instance;

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        scheduleMethod();
        CURRENT_PACKAGE_NAME = getApplicationContext().getPackageName();
        Log.e("Current PN", "" + CURRENT_PACKAGE_NAME);

        instance = this;

        return START_STICKY;
    }

    private void scheduleMethod() {
        // TODO Auto-generated method stub
        Intent dialogIntent = new Intent(getApplicationContext(), DetectionActivity2.class);
        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(dialogIntent);
        Log.i(TAG,"Neel is Behen ka loda");
        ScheduledExecutorService scheduler = Executors
                .newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(new Runnable() {

            @Override
            public void run() {
                // TODO Auto-generated method stub

                // This method will check for the Running apps after every 100ms

                Log.i(TAG,"Neel is maha  chutiya");
                checkRunningApps();


            }
        }, 0, 1000, TimeUnit.MILLISECONDS);
    }

    public void checkRunningApps() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningTaskInfo> RunningTask = mActivityManager.getRunningTasks(1);

        ActivityManager.RunningTaskInfo ar = RunningTask.get(0);

        String activityOnTop = ar.baseActivity.getPackageName();

        Log.e("activity on TOp", "" + activityOnTop);



        Log.i(TAG,activityOnTop);
        // Provide the packagename(s) of apps here, you want to show password activity
        if (activityOnTop.contains(CURRENT_PACKAGE_NAME)){
            // DO nothing

        } else {
            Intent dialogIntent = new Intent(getApplicationContext(), DetectionActivity2.class);
            dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(dialogIntent);
            Log.i(TAG,"Neel is maha maha chutiya");
            stop();
        }
    }

    public static void stop() {
        if (instance != null) {
            instance.stopSelf();
        }
    }
}