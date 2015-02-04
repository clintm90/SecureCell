package com.github.securecell.service;

import android.content.Intent;
import android.net.VpnService;
import android.os.ParcelFileDescriptor;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.DatagramChannel;

public class VPNService extends VpnService
{
    private Thread mThread;
    private ParcelFileDescriptor mInterface;
    Builder builder = new Builder();

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        mThread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                try
                {
                    //a. Configure the TUN and get the interface.
                    mInterface = builder.setSession("SecureCell")
                            .addAddress("192.168.0.1", 24)
                            .addDnsServer("8.8.8.8")
                            .addRoute("0.0.0.0", 0).establish();
                    FileInputStream in = new FileInputStream(mInterface.getFileDescriptor());
                    //b. Packets received need to be written to this output stream.
                    FileOutputStream out = new FileOutputStream(mInterface.getFileDescriptor());
                    //c. The UDP channel can be used to pass/get ip package to/from server
                    DatagramChannel tunnel = DatagramChannel.open();
                    // Connect to the server, localhost is used for demonstration only.
                    tunnel.connect(new InetSocketAddress("127.0.0.1", 8087));
                    //d. Protect this socket, so package send by it will not be feedback to the vpn service.
                    protect(tunnel.socket());

                    ByteBuffer packet = ByteBuffer.allocate(32767);
                    //e. Use a loop to pass packets.
                    while(true)
                    {
                        
                        int length = in.read(packet.array());
                        packet.put(packet.array());
                        tunnel.write(packet);
                        packet.clear();
                            /*while (packet.hasRemaining())
                            {
                                Log.i("ok",""+packet.get());
                                //System.out.print((char) packet.get());
                            }*/
                            /*packet.limit(length);
                            tunnel.write(packet);
                            packet.clear();*/
                        
                        //get packet with in
                        //put packet to tunnel
                        //get packet form tunnel
                        //return packet with out
                        //sleep is a must
                        Thread.sleep(100);
                    }

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally
                {
                    try
                    {
                        if (mInterface != null)
                        {
                            mInterface.close();
                            mInterface = null;
                        }
                    }
                    catch (Exception e)
                    {

                    }
                }
            }

        }, "MyVpnRunnable");

        mThread.start();
        return START_STICKY;
    }

    @Override
    public void onDestroy()
    {
        if (mThread != null)
        {
            mThread.interrupt();
        }
        super.onDestroy();
    }
}