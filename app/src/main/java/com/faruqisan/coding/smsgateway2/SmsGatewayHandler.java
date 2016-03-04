package com.faruqisan.coding.smsgateway2;

import android.telephony.SmsManager;
import android.util.Log;

import org.apache.http.*;
import org.apache.http.entity.StringEntity;
import org.apache.http.protocol.*;
import org.apache.http.util.*;
import org.json.*;

import java.io.IOException;

/**
 * Created by Coding on 6/1/2015.
 */

public class SmsGatewayHandler implements HttpRequestHandler {

    @Override
    public void handle(HttpRequest httpRequest, HttpResponse httpResponse, HttpContext httpContext) throws HttpException, IOException {
        if (httpRequest instanceof HttpEntityEnclosingRequest) {

            try {
                HttpEntity entity = ((HttpEntityEnclosingRequest) httpRequest).getEntity();

                String body = EntityUtils.toString(entity);
                JSONObject object = new JSONObject(body);
                String no = object.getString("no");
                String pesan = object.getString("pesan");

                SmsManager.getDefault().sendTextMessage(no,null,pesan,null,null);
                httpResponse.setEntity(new StringEntity("Pengiriman Pesan Sukses"));

            } catch (Exception ex) {
                Log.e(SmsGatewayHandler.class.getName(), ex.getMessage(), ex);
            }
        }
    }
}
