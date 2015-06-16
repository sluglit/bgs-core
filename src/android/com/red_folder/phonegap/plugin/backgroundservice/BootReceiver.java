package com.red_folder.phonegap.plugin.backgroundservice;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
    public static final String TAG = BootReceiver.class.getSimpleName();

    /*
     ************************************************************************************************
     * Overriden Methods
     ************************************************************************************************
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        String action = String.valueOf(intent.getAction());
        Log.d(TAG, "BootReceiver event " + action);

        // Get all the registered and loop through and start them
        String[] serviceList = PropertyHelper.getBootServices(context);

        if (serviceList != null) {
            for (String service : serviceList) {
                try {
                    Log.d(TAG, "Starting service " + service + " [Action:" + action + "]");
                    // Fix to https://github.com/Red-Folder/bgs-core/issues/18
                    // Gets the class from string
                    Class<?> serviceClass = ReflectionHelper.LoadClass(service);
                    Intent serviceIntent = new Intent(context, serviceClass);
                    serviceIntent.setAction(action);
                    context.startService(serviceIntent);
                } catch (Exception e) {
                    Log.e(TAG, "Error starting service" + service, e);
                }
            }
        }
    }

} 
