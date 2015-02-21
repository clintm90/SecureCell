package com.github.securecell.proxy;

import java.io.Serializable;
import java.util.List;

/**
 * HTTP/1.1 200 OK
 * Connection: close
 */
public class Response implements Serializable
{
    public String Version = "HTTP/1.1";
    public int StatusCode = 200;
    public String StatusMessage = "OK";
    public List<Field> Fields;

    public static Response Parse(String value)
    {
        Response mRTS = null;

        return mRTS;
    }

    public static String Compile(Response response)
    {
        String mRTS = null;
        return mRTS;
    }

    private static class Field
    {
        public String Name;
        public String Value;


        public Field(String name, String value)
        {
            Name = name;
            Value = value;
        }
    }
}
