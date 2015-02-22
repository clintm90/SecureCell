package com.github.securecell;

import android.app.Application;

import com.github.securecell.proxy.Request;
import com.github.securecell.proxy.Response;

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
                    
                    ClientThread clientThread = new ClientThread(socket);
                    clientThread.start();
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            }
        }

        public String ReplayRequest(String request)
        {
            String mRTS = "", domain = null, line;
            Response ResultResponse = null;
            
            Request ResultRequest = Request.Parse(request);
            ResultRequest.Fields.put("X-Forwarded-For", "192.168.1.1");

            domain = ResultRequest.Fields.get("Host");
            
            try
            {
                Socket socket = new Socket(InetAddress.getByName(domain), 80);
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                PrintWriter out = new PrintWriter(socket.getOutputStream());
                /*out.print("GET http://www.monip.org/ HTTP/1.1\r\n" +
                        "    Accept-Language: fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4\r\n" +
                        "    Host: www.monip.org\r\n" +
                        "    Accept-Encoding: gzip, deflate, sdch\r\n" +
                        "    X-Forwarded-For: 192.168.1.1\r\n" +
                        "    User-Agent: Mozilla/5.0 (Linux; Android 4.2.2; OZZY Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.93 Mobile Safari/537.36\\n" +
                        "    Accept: text/html,application/xhtml+xml,application/xml;q=0.9,image/webp\r\n" +
                        "    Cache-Control: max-age=0\r\n" +
                        "    Proxy-Connection: keep-alive\r\n\r\n");*/
                //out.print("GET / HTTP/1.1\r\nHost: " + domain + "\r\nX-Forwarded-For: 205.48.32.165\r\nConnection: close\r\n\r\n");
                out.print(Request.Compile(ResultRequest));
                out.flush();
                while((line = in.readLine()) != null)
                {
                    mRTS += line + "\r\n";
                }
                ResultResponse = Response.Parse(mRTS);
                ResultResponse.Fields.put("Via", "1.1 perdu.com");
                in.close();
                out.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            return Response.Compile(ResultResponse);
        }

        public class ClientThread extends Thread
        {
            Socket Sock;
            
            public ClientThread(Socket socket)
            {
                Sock = socket;
                setName(socket.getRemoteSocketAddress().toString());
            }
            
            @Override
            public void run()
            {
                try
                {
                    BufferedReader in = new BufferedReader(new InputStreamReader(Sock.getInputStream()));
                    BufferedWriter out = new BufferedWriter(new OutputStreamWriter(Sock.getOutputStream()));

                    //Lecture de la reqûete
                    String Request = "", line;
                    while ((line = in.readLine()) != null)
                    {
                        Request += line + "\r\n";
                        if (line.isEmpty())
                        {
                            break;
                        }
                    }

                    //Envoi de la réponse par proxy
                    out.write(ReplayRequest(Request));
                    
                    out.close();
                    in.close();
                }
                catch(Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
//                        Sock.close();
                    }
                    catch(Exception e)
                    {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onLowMemory()
    {
        super.onLowMemory();
    }
}
