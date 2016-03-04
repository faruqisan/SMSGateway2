package com.faruqisan.coding.smsgateway2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by Coding on 6/5/2015.
 */
public class SmsResponder extends BroadcastReceiver {

    private Context context;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context=context;

        if(SMSAutoReply.autoRepStat==true){

            if(intent.getAction().endsWith("android.provider.Telephony.SMS_RECEIVED")){
                Bundle bundle=intent.getExtras();
                android.telephony.SmsMessage[]msgs=null;
                String msg_from;
                String message=read(context);
                boolean verifyResult=false;

                if(bundle!=null){
                    try{
                        Object[]pdus=(Object[])bundle.get("pdus");
                        msgs=new android.telephony.SmsMessage[pdus.length];
                        for(int i=0;i<msgs.length;i++){
                            msgs[i]= android.telephony.SmsMessage.createFromPdu((byte[])pdus[i]);
                            msg_from=msgs[i].getOriginatingAddress();
                            Toast.makeText(context, "Get SMS From"+msg_from, Toast.LENGTH_SHORT).show();

                            verifyResult=verifySms(msgs[i].getMessageBody());
                            if(verifyResult==true) {
                                SmsManager.getDefault().sendTextMessage(msg_from, null, message, null, null);
                            }else{
                                SmsManager.getDefault().sendTextMessage(msg_from, null, "Format Salah", null, null);
                            }
                        }
                    }catch (Exception e ){

                    }
                }

            }
        }else{
            Toast.makeText(context, "SMS Received but nothing happen except this toast", Toast.LENGTH_SHORT).show();
        }
    }
    private String read(Context context) {
        String ret = "";

        try {
            InputStream inputStream = context.openFileInput("config.txt");

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

    private boolean verifySms(String message){
        boolean result = false;
        try {
            String splitter[] = message.split(" ");

            if (splitter[0].equalsIgnoreCase("info") && !splitter[1].isEmpty()) {
                result = true;
            }
        }catch(ArrayIndexOutOfBoundsException e){
            result=false;
        }
        return result;
    }


}
