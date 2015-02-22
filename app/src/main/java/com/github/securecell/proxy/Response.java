package com.github.securecell.proxy;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * HTTP/1.1 200 OK
 * Connection: close
 */
public class Response implements Serializable
{
    public String Version = null;
    public int StatusCode = 0;
    public String StatusMessage = null;
    public Map<String, String> Fields = new HashMap<String, String>();
    public String Content = null;

    public static Response Parse(String value)
    {
        Response mRTS = new Response();
        String[] header = value.split("\r\n\r\n");
        String[] lines = header[0].split("\r\n");
        String[] first_line = lines[0].split(" ");

        mRTS.Version = first_line[0];
        mRTS.StatusCode = Integer.valueOf(first_line[1]);
        mRTS.StatusMessage = first_line[2];
        
        try
        {
            mRTS.Content = header[1];
        }
        catch(Exception e)
        {
            e.printStackTrace();
        }
        
        for(String current_line : lines)
        {
            if(!current_line.equals(lines[0]))
            {
                String[] params = current_line.split(": ");
                mRTS.Fields.put(params[0], params[1]);
            }
        }

        return mRTS;
    }

    public static String Compile(Response response)
    {
        String mRTS = "";
        mRTS += response.Version;
        mRTS += " ";
        mRTS += response.StatusCode;
        mRTS += " ";
        mRTS += response.StatusMessage;
        mRTS += "\r\n";
        
        for(Map.Entry<String, String> current_line : response.Fields.entrySet())
        {
            mRTS += current_line.getKey() + ": " + current_line.getValue() + "\r\n";
        }
        
        mRTS += "\r\n\r\n";
        
        mRTS += response.Content;
        
        return mRTS;
    }
}