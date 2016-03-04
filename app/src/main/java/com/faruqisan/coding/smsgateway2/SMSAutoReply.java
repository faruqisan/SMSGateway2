package com.faruqisan.coding.smsgateway2;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class SMSAutoReply extends Activity implements DialogAutoRepSettings.DialogAutoRepSettingsListener{

    Button setting;
    Button start;
    Button stop;
    public static boolean autoRepStat=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsauto_reply);

        setting=(Button)findViewById(R.id.buttonAutoRepSettings);
        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogAutoRepSettings dialogAutoRepSettings = new DialogAutoRepSettings();
                dialogAutoRepSettings.setRetainInstance(true);
                dialogAutoRepSettings.show(getFragmentManager(),"tes");
            }
        });

        start=(Button)findViewById(R.id.buttonAutoRepStart);
        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRepStat=true;
                Toast.makeText(SMSAutoReply.this,"SMS Auto Reply Started",Toast.LENGTH_LONG).show();
            }
        });

        stop=(Button)findViewById(R.id.buttonAutoRepStop);
        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                autoRepStat=false;
            }
        });


    }

    private void writeToFile(String data) {
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(openFileOutput("config.txt", Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            Log.e("Exception", "File write failed: " + e.toString());
        }
    }

    private String read() {
        String ret = "";

        try {
            InputStream inputStream = openFileInput("config.txt");

            if (inputStream != null) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ((receiveString = bufferedReader.readLine()) != null) {
                    stringBuilder.append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();
            }
        } catch (FileNotFoundException e) {
            Log.e("login activity", "File not found: " + e.toString());
        } catch (IOException e) {
            Log.e("login activity", "Can not read file: " + e.toString());
        }

        return ret;
    }


    @Override
    public void onDialogPositiveClick(DialogFragment dialog,String message) {
        writeToFile(message);
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }
}
