package com.github.securecell.proxy;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * GET / HTTP/1.1\r\n
 * Host: perdu;com\r\n
 * Connection: close\r\n
 * \r\n
 */
public class Request implements Serializable
{
    public String Method = "GET";
    public String Path = "/";
    public String Version = "HTTP/1.1";
    public List<Field> Fields;
    
    public static Request Parse(String value)
    {
        Request mRTS = null;
        String[] lines = value.split("\r\n");
        String[] first_line = lines[0].split(" ");
        List<Field> fields = new ArrayList<>();
        
        mRTS.Method = first_line[0];
        mRTS.Path = first_line[1];
        mRTS.Version = first_line[2];
        mRTS.Fields = fields;
        
        mRTS.Fields.add(new Field("Host", "ds"));
        
        return mRTS;
    }
    
    public static String Compile(Request request)
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
