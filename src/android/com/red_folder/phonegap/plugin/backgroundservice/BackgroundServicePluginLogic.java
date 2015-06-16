package com.red_folder.phonegap.plugin.backgroundservice;

import android.content.Context;
import android.util.Log;
import org.json.JSONArray;

import java.util.Hashtable;
import java.util.Map;

public class BackgroundServicePluginLogic {

    /*
     ************************************************************************************************
     * Static values
     ************************************************************************************************
     */
    public static final String TAG = BackgroundServicePluginLogic.class.getSimpleName();

    /*
     ************************************************************************************************
     * Keys
     ************************************************************************************************
     */
    public static final String ACTION_START_SERVICE = "startService";
    public static final String ACTION_STOP_SERVICE = "stopService";

    public static final String ACTION_ENABLE_TIMER = "enableTimer";
    public static final String ACTION_DISABLE_TIMER = "disableTimer";

    public static final String ACTION_SET_CONFIGURATION = "setConfiguration";

    public static final String ACTION_REGISTER_FOR_BOOTSTART = "registerForBootStart";
    public static final String ACTION_DEREGISTER_FOR_BOOTSTART = "deregisterForBootStart";

    public static final String ACTION_GET_STATUS = "getStatus";

    public static final String ACTION_RUN_ONCE = "runOnce";

    public static final String ACTION_REGISTER_FOR_UPDATES = "registerForUpdates";
    public static final String ACTION_DEREGISTER_FOR_UPDATES = "deregisterForUpdates";

    public static final String[] ACTIONS = {
            ACTION_START_SERVICE,
            ACTION_STOP_SERVICE,
            ACTION_ENABLE_TIMER,
            ACTION_DISABLE_TIMER,
            ACTION_SET_CONFIGURATION,
            ACTION_REGISTER_FOR_BOOTSTART,
            ACTION_DEREGISTER_FOR_BOOTSTART,
            ACTION_GET_STATUS,
            ACTION_RUN_ONCE,
            ACTION_REGISTER_FOR_UPDATES,
            ACTION_DEREGISTER_FOR_UPDATES
    };


    /*
     ************************************************************************************************
     * Fields
     ************************************************************************************************
     */
    private Context mContext;
    private final Hashtable<String, ServiceDetails> mServices = new Hashtable<String, ServiceDetails>();

	/*
     ************************************************************************************************
	 * Constructors 
	 ************************************************************************************************
	 */
    // Part fix for https://github.com/Red-Folder/Cordova-Plugin-BackgroundService/issues/19
    //public BackgroundServicePluginLogic() {
    //}

    public BackgroundServicePluginLogic(Context pContext) {
        this.mContext = pContext;
    }

	/*
     ************************************************************************************************
	 * Public Methods 
	 ************************************************************************************************
	 */

    // Part fix for https://github.com/Red-Folder/Cordova-Plugin-BackgroundService/issues/19
    //public void initialize(Context pContext) {
    //	this.mContext = pContext;
    //}

    //public boolean isInitialized() {
    //	if (this.mContext == null)
    //		return false;
    //	else
    //		return true;
    //}

    public boolean isActionValid(String action) {
        for (String tmp : ACTIONS) {
            if (tmp.equals(action)) {
                return true;
            }
        }
        return false;
    }

    public ExecuteResult execute(String action, JSONArray data) {
        return execute(action, data, null, null);
    }

    public ExecuteResult execute(String action, JSONArray data, IUpdateListener listener, Object[] listenerExtras) {
        ExecuteResult result = null;

        Log.d(TAG, "Start of Execute");
        try {
            Log.d(TAG, "Withing try block");
            if ((data != null) &&
                    (!data.isNull(0)) &&
                    (data.get(0) instanceof String) &&
                    (data.getString(0).length() > 0)) {

                String serviceName = data.getString(0);

                Log.d(TAG, "Finding servicename " + serviceName);

                ServiceDetails service = null;

                Log.d(TAG, "Services contain " + this.mServices.size() + " records");

                synchronized (mServices) {
                    service = mServices.get(serviceName);
                    if (service != null) {
                        Log.d(TAG, "Found existing Service Details");
                    } else {
                        Log.d(TAG, "Creating new Service Details");
                        service = new ServiceDetails(this.mContext, serviceName);
                        this.mServices.put(serviceName, service);
                    }
                }

                Log.d(TAG, "Action = " + action);


                if (!service.isInitialised())
                    service.initialise();

                if (ACTION_GET_STATUS.equals(action)) {
                    result = service.getStatus();
                } else if (ACTION_START_SERVICE.equals(action)) {
                    result = service.startService();
                } else if (ACTION_REGISTER_FOR_BOOTSTART.equals(action)) {
                    result = service.registerForBootStart();
                } else if (ACTION_DEREGISTER_FOR_BOOTSTART.equals(action)) {
                    result = service.deregisterForBootStart();
                } else if (ACTION_REGISTER_FOR_UPDATES.equals(action)) {
                    result = service.registerForUpdates(listener, listenerExtras);
                } else if (ACTION_DEREGISTER_FOR_UPDATES.equals(action)) {
                    result = service.deregisterForUpdates();
                } else {
                    Log.d(TAG, "Check if the service is running?");

                    if (service != null && service.isServiceRunning()) {
                        Log.d(TAG, "Service is running?");

                        if (ACTION_STOP_SERVICE.equals(action)) result = service.stopService();

                        if (ACTION_ENABLE_TIMER.equals(action)) result = service.enableTimer(data);
                        if (ACTION_DISABLE_TIMER.equals(action)) result = service.disableTimer();

                        if (ACTION_SET_CONFIGURATION.equals(action)) result = service.setConfiguration(data);

                        if (ACTION_RUN_ONCE.equals(action)) result = service.runOnce();

                    }
                }

            } else {
                result = new ExecuteResult(ExecuteStatus.ERROR);
                Log.w(TAG, "ERROR - no servicename");
            }
        } catch (Exception ex) {
            result = new ExecuteResult(ExecuteStatus.ERROR);
            Log.e(TAG, "Exception - " + ex.getMessage(), ex);
        }

        if (result == null)
            result = new ExecuteResult(ExecuteStatus.INVALID_ACTION);

        return result;
    }

    public void onDestroy() {

        Log.d(TAG, "On Destroy Start");
        try {
            Log.d(TAG, "Checking for services");
            for (Map.Entry<String, ServiceDetails> entry : mServices.entrySet()) {
                try {
                    Log.d(TAG, "Calling service.close()");
                    ServiceDetails service = entry.getValue();
                    service.close();
                } catch (Exception e) {
                    Log.e(TAG, "Error closing service " + entry.getKey(), e);
                }
            }
        } catch (Throwable t) {
            // catch any issues, typical for destroy routines
            // even if we failed to destroy something, we need to continue destroying
            Log.e(TAG, "Error has occurred while trying to close services", t);
        } finally {
            this.mServices.clear();
            Log.d(TAG, "On Destroy Finish");
        }
    }
}
