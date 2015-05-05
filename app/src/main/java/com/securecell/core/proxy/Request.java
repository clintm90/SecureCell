package com.securecell.core.proxy;

import android.util.Log;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * GET / HTTP/1.1\r\n
 * Host: perdu.com\r\n
 * Connection: close\r\n
 * \r\n
 */
public class Request implements Serializable
{
    public String Method = null;
    public String Path = null;
    public String Version = null;
    public Map<String, String> Fields = new HashMap<String, String>();
    
    public static Request Parse(String value)
    {
        Request mRTS = new Request();
        String[] lines = value.split("\r\n");
        String[] first_line = lines[0].split(" ");

        mRTS.Method = first_line[0];
        mRTS.Path = first_line[1];
        mRTS.Version = first_line[2];
        
        for(String current_line : lines)
        {
            if(!current_line.equals(lines[0]))
            {
                String[] params = current_line.split(": ");
                mRTS.Fields.put(params[0], params[1]);
            }
        }

        Log.w("Request", value);

        return mRTS;
    }
    
    public static String Compile(Request request)
    {
        String mRTS = "";
        mRTS += request.Method;
        mRTS += " ";
        mRTS += request.Path;
        mRTS += " ";
        mRTS += request.Version;
        mRTS += "\r\n";

        for(Map.Entry<String, String> current_line : request.Fields.entrySet())
        {
            mRTS += current_line.getKey() + ": " + current_line.getValue() + "\r\n";
        }
        
        mRTS += "\r\n\r\n";
        
        return mRTS;
    }
}