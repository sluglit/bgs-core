package com.red_folder.phonegap.plugin.backgroundservice;

/**
 * Created by Gareth on 2015-06-16.
 */
public interface IUpdateListener {
    void handleUpdate(ExecuteResult logicResult, Object[] listenerExtras);

    void closeListener(ExecuteResult logicResult, Object[] listenerExtras);
}
