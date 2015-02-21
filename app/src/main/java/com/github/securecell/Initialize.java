package com.github.securecell;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class Initialize extends Application
{
    public static String SERVER_DOMAIN = "151.80.131.143";
    public static boolean SSL = true;
    public ServerSocket Sock;

    @Override
    public void onCreate()
    {
        super.onCreate();

        Thread SockThread = new Thread(new ServerThread());
        SockThread.start();
    }

    public class ServerThread implements Runnable
    {
        @Override
        public void run()
        {
            Socket socket = null;
            try
            {
                Sock = new ServerSocket(9090);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            while (!Thread.currentThread().isInterrupted())
            {
                try
                {
                    socket = Sock.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                    String Request = "", line;
                    while ((line = in.readLine()) != null)
                    {
                        Request += line + "\r\n";
                        if (line.isEmpty())
                        {
                            break;
                        }
                    }

                    String Response = ReplayRequest(Request);
                    out.write(Response);
                    
                    /*out.write("HTTP/1.1 200 OK\r\n");
                    out.write("Server: Squid\r\n");
                    out.write("Via: 1.0 perdu.com\r\n");
                    out.write("X-Forwarded-For: 89.45.123.255\r\n");
                    out.write("Content-Type: text/html\r\n");
                    out.write("Content-Length: 2\r\n");
                    out.write("\r\n");
                    out.write("ok");*/

                    out.close();
                    in.close();

                    /*PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    //Toast.makeText(getApplicationContext(), in.readLine(), Toast.LENGTH_LONG).show();
                    Log.i("ds", in.readLine());
                    out.write("HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: 13\r\nServer: Squid 1.3\r\nConnection: close\r\n\r\nhello world");
                    out.close();*/
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public String ReplayRequest(String request)
        {
            String mRTS = "", line;
            try
            {
                Socket socket = new Socket(InetAddress.getByName("perdu.com"), 80);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                out.print(request);
                /*out.println("GET / HTTP/1.1");
                out.println("Host: " + host);
                out.println("Connection: close");
                out.println("");*/
                out.flush();
                while((line = in.readLine()) != null)
                {
                    mRTS += line + "\r\n";
                }
                in.close();
                out.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
                Log.e("error", e.getLocalizedMessage());
            }
            return mRTS;
        }
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }
}
