package com.faruqisan.coding.smsgateway2;

import org.apache.http.HttpException;
import org.apache.http.impl.DefaultConnectionReuseStrategy;
import org.apache.http.impl.DefaultHttpResponseFactory;
import org.apache.http.impl.DefaultHttpServerConnection;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.BasicHttpProcessor;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.protocol.HttpRequestHandlerRegistry;
import org.apache.http.protocol.HttpService;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * Created by Coding on 6/1/2015.
 */
public class SmsGatewayServer {

    private int port;
    private HttpService httpService;
    private ServerSocket serverSocket;
    private HttpContext httpContext;

    public SmsGatewayServer(int port){
        this.port=port;
        httpService=new HttpService(new BasicHttpProcessor(),new DefaultConnectionReuseStrategy(),new DefaultHttpResponseFactory());
        httpContext=new BasicHttpContext();

        HttpRequestHandlerRegistry registry = new HttpRequestHandlerRegistry();
        registry.register("*",new SmsGatewayHandler());
        httpService.setHandlerResolver(registry);
    }
    public void start()throws IOException,HttpException{
        serverSocket=new ServerSocket(port);
        while(true){
            Socket socket=serverSocket.accept();

            DefaultHttpServerConnection serverConnection=new DefaultHttpServerConnection();
            serverConnection.bind(socket,new BasicHttpParams());
            httpService.handleRequest(serverConnection, httpContext);

            socket.close();
        }
    }
    public void stop()throws IOException{
        serverSocket.close();
    }

}
