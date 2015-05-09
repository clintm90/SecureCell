package com.securecell.core.proxy;

import android.util.Log;
import android.widget.Toast;

import com.securecell.core.Initialize;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.util.StringTokenizer;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

/**
 * Proxy Server Instance
 */
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
			Initialize.ShowDebug("Launching Proxy Server");
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

	/**
	 * Replay sended request to server
	 * @param request HTTP request
	 * @return HTTP response
	 */
	public String ReplayRequest(String request)
	{
		String mRTS = "", domain = null, line;
		Response ResultResponse = null;

		Request ResultRequest = Request.Parse(request);
		//ResultRequest.Fields.put("X-Forwarded-For", "192.168.1.1");

		domain = ResultRequest.Fields.get("Host");

		try
		{
			/*SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
			SSLSocket socket = (SSLSocket) factory.createSocket("ssl.com", 443);

			socket.startHandshake();*/

			Socket socket = new Socket(InetAddress.getByName(domain), 80);

			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream(), "UTF8"));
			PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), "UTF8")));

			out.println("GET " + ResultRequest.Path + " HTTP/1.1");
			out.println("Host: " + ResultRequest.Fields.get("Host"));
			out.println("Connection: close");
			out.println();
			out.flush();

			while ((line = in.readLine()) != null)
			{
				mRTS += line + "\r\n";
				Log.e("Result", "---" + line);
			}

			//ResultResponse = Response.Parse(mRTS);
			//ResultResponse.Fields.put("Via", "1.1 perdu.com");

			in.close();
			out.close();

			/*DefaultHttpClient httpclient = new DefaultHttpClient();
			HttpGet method = new HttpGet(new URI(ResultRequest.Path));
			HttpResponse response = httpclient.execute(method);
			InputStream data = response.getEntity().getContent();

			BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(data));
			while((line = bufferedReader.readLine()) != null)
				mRTS += line + "\r\n";*/
		}
		catch (Exception e)
		{
			e.printStackTrace();
			Log.e("error", e.getLocalizedMessage());
		}
		return mRTS; //Response.Compile(ResultResponse);
	}

	/**
	 * Socket.accept() thread
	 */
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