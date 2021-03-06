package dabarun.remotefarm_admin.chatting;


//import android.R;



import com.google.android.gms.gcm.GoogleCloudMessaging;

import dabarun.remotefarm_admin.R;


import Variable.GlobalVariable;
import android.app.IntentService;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

public class MSGService extends IntentService {

    SharedPreferences prefs;
    NotificationCompat.Builder notification;
    NotificationManager manager;


    public MSGService() {
        super("MSGService");
    }



    @Override
    protected void onHandleIntent(Intent intent) {
    	Log.d("test", "onHandleIntent in MSGService");
        Bundle extras = intent.getExtras();
        GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(this);

        String messageType = gcm.getMessageType(intent);
        prefs = getSharedPreferences(GlobalVariable.DABARUNFARMER, 0);

        
        if (!extras.isEmpty()) {

            if (GoogleCloudMessaging.
                    MESSAGE_TYPE_SEND_ERROR.equals(messageType)) {
                Log.e("TAG","Error");

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_DELETED.equals(messageType)) {
                Log.e("TAG","Error");

            } else if (GoogleCloudMessaging.MESSAGE_TYPE_MESSAGE.equals(messageType)) {

                if(!prefs.getString("CURRENT_ACTIVE","").equals(extras.getString("fromu"))) {
                    sendNotification(extras.getString("msg"), extras.getString("fromu"), extras.getString("name"));
                    Log.i("TAG", "CURRENT_ACTIVE not same");
                }
                Log.i("TAG", "Received: " + extras.getString("msg"));
            }
        }
        MSGReceiver.completeWakefulIntent(intent);
    }




    private void sendNotification(String msg,String mobno,String name) {

        Bundle args = new Bundle();
        args.putString("mobno", mobno);
        args.putString("name", name);
        args.putString("msg", msg);
        Intent chat = new Intent(this, ChatActivity.class);
        chat.putExtra("INFO", args);
        notification = new NotificationCompat.Builder(this);
        notification.setContentTitle(name);
        notification.setContentText(msg);
        notification.setTicker("New Message !");
        Log.d("test", "New Message!");
        notification.setSmallIcon(R.drawable.ic_launcher);

        PendingIntent contentIntent = PendingIntent.getActivity(this, 1000, chat, PendingIntent.FLAG_CANCEL_CURRENT);
        notification.setContentIntent(contentIntent);
        notification.setAutoCancel(true);
        manager =(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        manager.notify(0, notification.build());
    }


}