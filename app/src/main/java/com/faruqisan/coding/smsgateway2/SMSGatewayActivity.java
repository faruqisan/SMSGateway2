package com.faruqisan.coding.smsgateway2;

import android.app.Activity;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Formatter;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.apache.http.HttpException;

import java.io.IOException;


public class SMSGatewayActivity extends Activity {
    public static boolean autoRepStat;
    TextView ipAddress;
    TextView serverStatus;
    Button startButton;
    Button stopButton;
    private SmsGatewayServer smsGatewayServer=new SmsGatewayServer(8989);
    private boolean running;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_smsgateway);


        ipAddress=(TextView)findViewById(R.id.labelIpAddress);
        ipAddress.setText(setIpAddress());

        running=false;

        serverStatus=(TextView)findViewById(R.id.textViewServerStatus);
        serverStatus.setText("Server Status : Stopped");
        serverStatus.setTextColor(Color.RED);

        startButton=(Button)findViewById(R.id.buttonStartServer);
        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SMSGatewayActivity.this, "SMS Gateway Server Service Started", Toast.LENGTH_SHORT).show();
                running=true;
                serverStatus.setText("Server Status : Running");
                serverStatus.setTextColor(Color.GREEN);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            smsGatewayServer.start();
                        } catch (IOException e) {

                        } catch (HttpException e) {

                        }
                    }
                }).start();
            }
        });

        stopButton=(Button)findViewById(R.id.buttonStopServer);
        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(running==false){
                    Toast.makeText(SMSGatewayActivity.this, "SMS Gateway Server Service Already Stoped", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(SMSGatewayActivity.this, "SMS Gateway Server Service Stoped", Toast.LENGTH_SHORT).show();
                    running = false;
                    serverStatus.setText("Server Status : Stopped");
                    serverStatus.setTextColor(Color.RED);
                    try {
                        smsGatewayServer.stop();
                        System.out.println("stopped");
                    } catch (IOException e) {

                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        if(running==true){
            try{
                smsGatewayServer.stop();
            }catch (IOException e){
            }
        }else{
        }
        super.onDestroy();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_smsgateway, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private String getDeviceIpAddress(){

        WifiManager wifiMgr = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        int ip = wifiInfo.getIpAddress();
        String ipAddress = Formatter.formatIpAddress(ip);
        return ipAddress;

    }

    private String setIpAddress(){
        String ip=getDeviceIpAddress();
        String text="This Device IP Adress : ";
        String result="";

        if(ip.equalsIgnoreCase("0.0.0.0")){
            result="This Device didn't connect to local network";
        }else{
            result=text+ip;
        }
        return result;
    }

}
