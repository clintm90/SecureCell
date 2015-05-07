package com.securecell.core.proxy;

import android.widget.Toast;

import com.securecell.core.Initialize;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLSocket;

public class ProxyServer implements Runnable
{
	public ServerSocket Sock;

	@Override
	public void run()
	{
		Initialize.ServerRunning = true;
		Socket socket = null;
		try
		{
			Sock = new ServerSocket(Initialize.PROXY_PORT);
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		while (!Thread.currentThread().isInterrupted())
		{
			try
			{
				if (Sock != null)
				{
					socket = Sock.accept();

					if (socket != null)
					{
						ClientThread clientThread = new ClientThread(socket);
						clientThread.start();
					}
				}
			}
			catch (Exception e)
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
		//ResultRequest.Fields.put("X-Forwarded-For", "192.168.1.1");

		domain = ResultRequest.Fields.get("Host");

		try
		{
			Socket socket = new Socket(InetAddress.getByName(domain), ResultRequest.Port);
			/*URL url = new URL("https://www.google.com");
			HttpsURLConnection con = (HttpsURLConnection) url.openConnection();
			con.getResponseCode();
			con.disconnect();
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			PrintWriter out = new PrintWriter(con.getOutputStream());*/
			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			PrintWriter out = new PrintWriter(socket.getOutputStream());

            /*Request toReplayRequest = new Request();
            toReplayRequest.Method = ResultRequest.Method;
            toReplayRequest.Path = ResultRequest.Path;
            toReplayRequest.Version = ResultRequest.Version;
            toReplayRequest.Fields.put("Host", "monip.org");
            toReplayRequest.Fields.put("Accept-Language", "fr-FR,fr;q=0.8,en-US;q=0.6,en;q=0.4");
            toReplayRequest.Fields.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp");
            toReplayRequest.Fields.put("User-Agent", "Mozilla/5.0 (Linux; Android 4.2.2; OZZY Build/JDQ39) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/39.0.2171.93 Mobile Safari/537.36");
            toReplayRequest.Fields.put("Connection", "close");
			out.print(Request.Compile(toReplayRequest));*/
			out.print("GET "+ResultRequest.Path+" HTTP/1.1\r\nHost: "+domain+"\r\nConnection: close\r\n\r\n");
			out.flush();

			while ((line = in.readLine()) != null)
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
		return mRTS; //Response.Compile(ResultResponse);
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
            catch (Exception e)
            {
                e.printStackTrace();
            }
            finally
            {
                try
                {
	                Sock.close();
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
		}
	}
}