package com.example.eventappwear;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;

import com.google.android.gms.wearable.MessageClient;
import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.Wearable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.IOException;

public class MainActivity extends AppCompatActivity implements MessageClient.OnMessageReceivedListener {

    public static final String jsonURL= "https://hmin309-embedded-systems.herokuapp.com/message-exchange/messages/";
    RecyclerView rv;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        doTheAutoRefresh();

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isOnline()) {
                    new JSONDownloader(MainActivity.this,jsonURL, rv).execute();
                }
            }
        });
    }

    // ICMP
    public boolean isOnline() {
        Runtime runtime = Runtime.getRuntime();
        try {
            Process ipProcess = runtime.exec("/system/bin/ping -c 1 8.8.8.8");
            int     exitValue = ipProcess.waitFor();
            return (exitValue == 0);
        }
        catch (IOException e)          { e.printStackTrace(); }
        catch (InterruptedException e) { e.printStackTrace(); }

        return false;
    }

    private void doTheAutoRefresh() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Write code for your refresh logic
                if (isOnline()) {
                    new JSONDownloader(MainActivity.this,jsonURL, rv).execute();
                }
//                new JSONDownloader(MainActivity.this,jsonURL, rv).execute();
                doTheAutoRefresh();
            }
        }, 30000);
    }

    protected void onPostResume() {
        super.onPostResume();

        Wearable.getMessageClient(this).addListener(this);
    }


    @Override
    public void onMessageReceived(@NonNull MessageEvent messageEvent) {
//        Log.i("CIO", "Received message: ");
//        Log.i("CIO", "  - Path: " + messageEvent.getPath());
//        String message = new String(messageEvent.getData());
//        Log.i("CIO", "  - Content: " +  message);

//        AsyncHTTPGET taskGet = new AsyncHTTPGET(MainActivity.this);
//            AsyncHTTPPost taskPost = new AsyncHTTPPost(MainActivity.this);
//            taskPost.execute(url);
//        taskGet.execute(url);

        new JSONDownloader(MainActivity.this,jsonURL, rv).execute();
//        TextView tv = (TextView)findViewById(R.id.textView);
//        tv.setText(tv.getText() + "\n" + message);
    }
}
