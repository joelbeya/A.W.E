package com.example.awe;

import androidx.core.app.RemoteInput;
import androidx.core.app.NotificationCompat;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationManagerCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.app.PendingIntent;
import android.app.Notification;
import android.view.View;
import android.widget.TextView;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import static com.example.awe.DemandIntentReceiver.ACTION_DEMAND;
import static com.example.awe.DemandIntentReceiver.EXTRA_MESSAGE;
import static com.example.awe.DemandIntentReceiver.EXTRA_VOICE_REPLY;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create an Intent for the wearable demand
        Intent demandIntent = new Intent(
                this,
                DemandIntentReceiver.class).putExtra(EXTRA_MESSAGE,
                "Reply icon selected.").setAction(ACTION_DEMAND);

        // Create a pending intent for the wearable demand to include in the notification
        PendingIntent demandPendingIntent =
                PendingIntent.getBroadcast(this, 0, demandIntent, 0);

        // Create RemoteInput object for a voice reply (demand)
        String replyLabel = getResources().getString(R.string.app_name);
        RemoteInput remoteInput = new RemoteInput.Builder(EXTRA_VOICE_REPLY)
                .setLabel(replyLabel)
                .build();

        // Create a wearable action
        NotificationCompat.Action replyAction = new NotificationCompat.Action.Builder(
                R.drawable.ic_reply_icon,
                getString(R.string.reply_label), demandPendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        // Create a WearableExtender for a notification and add the wearable action
        NotificationCompat.WearableExtender wearableExtender =
                new NotificationCompat.WearableExtender()
                        .addAction(replyAction);


        // Notification that includes a reply action (demand)
        Notification notification =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.ic_launcher)
                        .setContentTitle("Jesus King")
                        .setContentText("Joel is Son")
                        .extend(wearableExtender)
                        .build();

        // Get an instance of the NotificationManagerCompat
        NotificationManagerCompat notificationManager =
                NotificationManagerCompat.from(this);

        // Dispatch the extendable notification
        int notificationId = 1;
        notificationManager.notify(notificationId, notification);

        // Register a message receiver for the users demand.
        IntentFilter messageFilter = new IntentFilter(Intent.ACTION_SEND);
        MessageReceiver messageReceiver = new MessageReceiver();
        LocalBroadcastManager.getInstance(this).
                registerReceiver(messageReceiver, messageFilter);
    }

    public void sendMessage(View view) {
        Intent intent = new Intent(this, MessageActivity.class);
        startActivity(intent);
    }


    // Class to receive demand text from the wearable demand receiver
    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            // Display the received demand
            TextView demandView = findViewById(R.id.demand_text);
            String demand = demandView.getText() + "\nDemand from wearable is: " + intent.getStringExtra("reply");
            demandView.setText(demand);
        }
    }
}
