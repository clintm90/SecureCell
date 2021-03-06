package com.securecell.core;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Proxy;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.util.Log;
import android.webkit.WebView;

import com.securecell.core.service.ProxyServerService;

import org.acra.annotation.ReportsCrashes;
import org.apache.http.HttpHost;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.Security;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;

@ReportsCrashes(
		formUri = "http://securecellhost.sourceforge.net/Backend.php")

public class Initialize extends Application
{
	public static String PACKAGE = "com.securecell.core";
	public static String VPS_DOMAIN = "151.80.131.143";
	public static String SERVER = "securecellhost.sourceforge.net";
	public static int PROXY_PORT = 9090;
	public static boolean SSL = true;
	public static boolean ServerRunning = false;

	public static void LaunchProxyServer(Context context)
	{
		Intent proxyServerService = new Intent(context, ProxyServerService.class);

		if (!ServerRunning)
		{
			ShowDebug("Starting Proxy Service...");
			context.startService(proxyServerService);
		}
	}

	/**
	 * Print Log.i if debug mode enable
	 * @param value Value to print
	 */
	public static void ShowDebug(String value)
	{
		Log.i("SecureCell", value);
	}

	@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
	public static void setMockLocation(Context context, double latitude, double longitude, float accuracy)
	{
		LocationManager lm = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

		lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener()
		{
			@Override
			public void onStatusChanged(String provider, int status, Bundle extras)
			{
			}

			@Override
			public void onProviderEnabled(String provider)
			{
			}

			@Override
			public void onProviderDisabled(String provider)
			{
			}

			@Override
			public void onLocationChanged(Location location)
			{
			}
		});

		lm.addTestProvider(LocationManager.GPS_PROVIDER, "requiresNetwork" == "", "requiresSatellite" == "", "requiresCell" == "", "hasMonetaryCost" == "", "supportsAltitude" == "", "supportsSpeed" == "", "supportsBearing" == "", android.location.Criteria.POWER_LOW, android.location.Criteria.ACCURACY_FINE);

		Location newLocation = new Location(LocationManager.GPS_PROVIDER);

		newLocation.setLatitude(latitude);
		newLocation.setLongitude(longitude);
		newLocation.setAccuracy(accuracy);
		newLocation.setTime(5);
		newLocation.setElapsedRealtimeNanos(5);

		lm.setTestProviderEnabled(LocationManager.GPS_PROVIDER, true);

		lm.setTestProviderStatus(LocationManager.GPS_PROVIDER, LocationProvider.AVAILABLE, null, System.currentTimeMillis());

		lm.setTestProviderLocation(LocationManager.GPS_PROVIDER, newLocation);
	}

	public static boolean setProxyToWebView(WebView webview, String host, int port, String applicationClassName)
	{
		// 3.2 (HC) or lower
		if (Build.VERSION.SDK_INT <= 13)
		{
			return setProxyUpToHC(webview, host, port);
		}
		// ICS: 4.0
		else if (Build.VERSION.SDK_INT <= 15)
		{
			return setProxyICS(webview, host, port);
		}
		// 4.1-4.3 (JB)
		else if (Build.VERSION.SDK_INT <= 18)
		{
			return setProxyJB(webview, host, port);
		}
		// 4.4 (KK) & 5.0 (Lollipop)
		else
		{
			return setProxyKKPlus(webview, host, port, "com.github.securecell");
		}
	}

	@SuppressWarnings("all")
	private static boolean setProxyUpToHC(WebView webview, String host, int port)
	{
		Log.d("ds", "Setting proxy with <= 3.2 API.");

		HttpHost proxyServer = new HttpHost(host, port);
		// Getting network
		Class networkClass = null;
		Object network = null;
		try
		{
			networkClass = Class.forName("android.webkit.Network");
			if (networkClass == null)
			{
				Log.e("ds", "failed to get class for android.webkit.Network");
				return false;
			}
			Method getInstanceMethod = networkClass.getMethod("getInstance", Context.class);
			if (getInstanceMethod == null)
			{
				Log.e("ds", "failed to get getInstance method");
			}
			network = getInstanceMethod.invoke(networkClass, new Object[]{webview.getContext()});
		}
		catch (Exception ex)
		{
			Log.e("ds", "error getting network: " + ex);
			return false;
		}
		if (network == null)
		{
			Log.e("ds", "error getting network: network is null");
			return false;
		}
		Object requestQueue = null;
		try
		{
			Field requestQueueField = networkClass.getDeclaredField("mRequestQueue");
			requestQueue = getFieldValueSafely(requestQueueField, network);
		}
		catch (Exception ex)
		{
			Log.e("ds", "error getting field value");
			return false;
		}
		if (requestQueue == null)
		{
			Log.e("ds", "Request queue is null");
			return false;
		}
		Field proxyHostField = null;
		try
		{
			Class requestQueueClass = Class.forName("android.net.http.RequestQueue");
			proxyHostField = requestQueueClass.getDeclaredField("mProxyHost");
		}
		catch (Exception ex)
		{
			Log.e("ds", "error getting proxy host field");
			return false;
		}

		boolean temp = proxyHostField.isAccessible();
		try
		{
			proxyHostField.setAccessible(true);
			proxyHostField.set(requestQueue, proxyServer);
		}
		catch (Exception ex)
		{
			Log.e("ds", "error setting proxy host");
		}
		finally
		{
			proxyHostField.setAccessible(temp);
		}

		Log.d("ds", "Setting proxy with <= 3.2 API successful!");
		return true;
	}

	@SuppressWarnings("all")
	private static boolean setProxyICS(WebView webview, String host, int port)
	{
		try
		{
			Log.d("ds", "Setting proxy with 4.0 API.");

			Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
			Class params[] = new Class[1];
			params[0] = Class.forName("android.net.ProxyProperties");
			Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

			Class wv = Class.forName("android.webkit.WebView");
			Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
			Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webview);

			Class wvc = Class.forName("android.webkit.WebViewCore");
			Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
			Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

			Class bf = Class.forName("android.webkit.BrowserFrame");
			Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
			Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

			Class ppclass = Class.forName("android.net.ProxyProperties");
			Class pparams[] = new Class[3];
			pparams[0] = String.class;
			pparams[1] = int.class;
			pparams[2] = String.class;
			Constructor ppcont = ppclass.getConstructor(pparams);

			updateProxyInstance.invoke(sJavaBridge, ppcont.newInstance(host, port, null));

			Log.d("ds", "Setting proxy with 4.0 API successful!");
			return true;
		}
		catch (Exception ex)
		{
			Log.e("ds", "failed to set HTTP proxy: " + ex);
			return false;
		}
	}

	/**
	 * Set Proxy for Android 4.1 - 4.3.
	 */
	@SuppressWarnings("all")
	private static boolean setProxyJB(WebView webview, String host, int port)
	{
		Log.d("ds", "Setting proxy with 4.1 - 4.3 API.");

		try
		{
			Class wvcClass = Class.forName("android.webkit.WebViewClassic");
			Class wvParams[] = new Class[1];
			wvParams[0] = Class.forName("android.webkit.WebView");
			Method fromWebView = wvcClass.getDeclaredMethod("fromWebView", wvParams);
			Object webViewClassic = fromWebView.invoke(null, webview);

			Class wv = Class.forName("android.webkit.WebViewClassic");
			Field mWebViewCoreField = wv.getDeclaredField("mWebViewCore");
			Object mWebViewCoreFieldInstance = getFieldValueSafely(mWebViewCoreField, webViewClassic);

			Class wvc = Class.forName("android.webkit.WebViewCore");
			Field mBrowserFrameField = wvc.getDeclaredField("mBrowserFrame");
			Object mBrowserFrame = getFieldValueSafely(mBrowserFrameField, mWebViewCoreFieldInstance);

			Class bf = Class.forName("android.webkit.BrowserFrame");
			Field sJavaBridgeField = bf.getDeclaredField("sJavaBridge");
			Object sJavaBridge = getFieldValueSafely(sJavaBridgeField, mBrowserFrame);

			Class ppclass = Class.forName("android.net.ProxyProperties");
			Class pparams[] = new Class[3];
			pparams[0] = String.class;
			pparams[1] = int.class;
			pparams[2] = String.class;
			Constructor ppcont = ppclass.getConstructor(pparams);

			Class jwcjb = Class.forName("android.webkit.JWebCoreJavaBridge");
			Class params[] = new Class[1];
			params[0] = Class.forName("android.net.ProxyProperties");
			Method updateProxyInstance = jwcjb.getDeclaredMethod("updateProxy", params);

			updateProxyInstance.invoke(sJavaBridge, ppcont.newInstance(host, port, null));
		}
		catch (Exception ex)
		{
			Log.e("ds", "Setting proxy with >= 4.1 API failed with error: " + ex.getMessage());
			return false;
		}

		Log.d("ds", "Setting proxy with 4.1 - 4.3 API successful!");
		return true;
	}

	// from https://stackoverflow.com/questions/19979578/android-webview-set-proxy-programatically-kitkat
	@SuppressLint("NewApi")
	@SuppressWarnings("all")
	private static boolean setProxyKKPlus(WebView webView, String host, int port, String applicationClassName)
	{
		Log.d("ds", "Setting proxy with >= 4.4 API.");

		Context appContext = webView.getContext().getApplicationContext();
		System.setProperty("http.proxyHost", host);
		System.setProperty("http.proxyPort", port + "");
		System.setProperty("https.proxyHost", host);
		System.setProperty("https.proxyPort", port + "");
		try
		{
			Class applictionCls = Class.forName(applicationClassName);
			Field loadedApkField = applictionCls.getField("mLoadedApk");
			loadedApkField.setAccessible(true);
			Object loadedApk = loadedApkField.get(appContext);
			Class loadedApkCls = Class.forName("android.app.LoadedApk");
			Field receiversField = loadedApkCls.getDeclaredField("mReceivers");
			receiversField.setAccessible(true);
			ArrayMap receivers = (ArrayMap) receiversField.get(loadedApk);
			for (Object receiverMap : receivers.values())
			{
				for (Object rec : ((ArrayMap) receiverMap).keySet())
				{
					Class clazz = rec.getClass();
					if (clazz.getName().contains("ProxyChangeListener"))
					{
						Method onReceiveMethod = clazz.getDeclaredMethod("onReceive", Context.class, Intent.class);
						Intent intent = new Intent(Proxy.PROXY_CHANGE_ACTION);

						onReceiveMethod.invoke(rec, appContext, intent);
					}
				}
			}

			Log.d("ds", "Setting proxy with >= 4.4 API successful!");
			return true;
		}
		catch (ClassNotFoundException e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Log.v("ds", e.getMessage());
			Log.v("ds", exceptionAsString);
		}
		catch (NoSuchFieldException e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Log.v("ds", e.getMessage());
			Log.v("ds", exceptionAsString);
		}
		catch (IllegalAccessException e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Log.v("ds", e.getMessage());
			Log.v("ds", exceptionAsString);
		}
		catch (IllegalArgumentException e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Log.v("ds", e.getMessage());
			Log.v("ds", exceptionAsString);
		}
		catch (NoSuchMethodException e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Log.v("ds", e.getMessage());
			Log.v("ds", exceptionAsString);
		}
		catch (InvocationTargetException e)
		{
			StringWriter sw = new StringWriter();
			e.printStackTrace(new PrintWriter(sw));
			String exceptionAsString = sw.toString();
			Log.v("ds", e.getMessage());
			Log.v("ds", exceptionAsString);
		}
		return false;
	}

	private static Object getFieldValueSafely(Field field, Object classInstance) throws IllegalArgumentException, IllegalAccessException
	{
		boolean oldAccessibleValue = field.isAccessible();
		field.setAccessible(true);
		Object result = field.get(classInstance);
		field.setAccessible(oldAccessibleValue);
		return result;
	}

	@Override
	public void onCreate()
	{
		super.onCreate();

		AsyncTask<Void, Void, Void> Task = new AsyncTask<Void, Void, Void>()
		{
			@Override
			protected Void doInBackground(Void... params)
			{
				try
				{
					//Security.addProvider(new com.sun.net.ssl.internal.ssl.Provider());

					SSLSocketFactory factory = (SSLSocketFactory) SSLSocketFactory.getDefault();
					SSLSocket socket = (SSLSocket) factory.createSocket("google.com", 443);

					socket.startHandshake();

					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					PrintWriter out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())));

					out.println("GET https://www.google.fr/?gfe_rd=cr&amp;ei=rO1MVeORJLKx8weB44CYBg HTTP/1.1");
					out.println();
					out.flush();

					if (out.checkError())
					{
						Log.e("result", "SSLSocketClient:  java.io.PrintWriter error");
					}

					String inputLine;
					while ((inputLine = in.readLine()) != null)
					{
						Log.e("result", inputLine);
					}

					in.close();
					out.close();
					socket.close();

				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
				return null;
			}
		};//.execute();

		//ACRA.init(this);

		LaunchProxyServer(this);
	}

	@Override
	public void onLowMemory()
	{
		super.onLowMemory();
	}
}
