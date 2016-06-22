package com.example.garytan.downloadimage;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.gcm.GcmPubSub;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

/**
 * Created by garytan on 16/6/19.
 */
public class RegistrationIntentService extends IntentService{
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    private static final String TAG = "RegIntentService";
    private static final String[] TOPICS = {"global"};
    public RegistrationIntentService() {
        super(TAG);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            synchronized (TAG) {

                //向GCM註冊(產生Token)
                InstanceID instanceID = InstanceID.getInstance(this);
                String token = instanceID.getToken("1059331836522",GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                Log.i(TAG, "GCM Registration Token: " + token);

                //App Server註冊(回傳Token)
                sendRegistrationToServer(token);
                //訂閱發怖主題
                subscribeTopics(token);
            }
        } catch (Exception e) {
            Log.d(TAG, "Failed to complete token refresh", e);
        }

    }

    private void sendRegistrationToServer(String token) {

    }
    private void subscribeTopics(String token) throws IOException {
        GcmPubSub pubSub = GcmPubSub.getInstance(this);
        for (String topic : TOPICS) {
            pubSub.subscribe(token, "/topics/" + topic, null);
        }
    }
}
