package com.red_folder.phonegap.plugin.backgroundservice;

import org.json.JSONObject;

public class ExecuteResult {

        /*
         ************************************************************************************************
         * Fields
         ************************************************************************************************
         */
        private ExecuteStatus mStatus;
        private JSONObject mData;
        private boolean mFinished = true;

        public ExecuteStatus getStatus() {
            return this.mStatus;
        }

        public void setStatus(ExecuteStatus pStatus) {
            this.mStatus = pStatus;
        }

        public JSONObject getData() {
            return this.mData;
        }

        public void setData(JSONObject pData) {
            this.mData = pData;
        }

        public boolean isFinished() {
            return this.mFinished;
        }

        public void setFinished(boolean pFinished) {
            this.mFinished = pFinished;
        }

        /*
         ************************************************************************************************
         * Constructors
         ************************************************************************************************
         */
        public ExecuteResult(ExecuteStatus pStatus) {
            this.mStatus = pStatus;
        }

        public ExecuteResult(ExecuteStatus pStatus, JSONObject pData) {
            this.mStatus = pStatus;
            this.mData = pData;
        }

        public ExecuteResult(ExecuteStatus pStatus, JSONObject pData, boolean pFinished) {
            this.mStatus = pStatus;
            this.mData = pData;
            this.mFinished = pFinished;
        }

    }