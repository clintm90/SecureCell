package com.securecell.core.service;

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
                    int timer = 0;
                    // We keep forwarding packets till something goes wrong.
                    while (true)
                    {
                        // Assume that we did not make any progress in this iteration.
                        boolean idle = true;
                        // Read the outgoing packet from the input stream.
                        int length = in.read(packet.array());
                        if (length > 0)
                        {
                            // Write the outgoing packet to the tunnel.
                            packet.limit(length);
                            tunnel.write(packet);
                            packet.clear();
                            // There might be more outgoing packets.
                            idle = false;
                            // If we were receiving, switch to sending.
                            if (timer < 1)
                            {
                                timer = 1;
                            }
                        }
                        // Read the incoming packet from the tunnel.
                        length = tunnel.read(packet);
                        if (length > 0)
                        {
                            // Ignore control messages, which start with zero.
                            if (packet.get(0) != 0)
                            {
                                // Write the incoming packet to the output stream.
                                out.write(packet.array(), 0, length);
                            }
                            packet.clear();
                            // There might be more incoming packets.
                            idle = false;
                            // If we were sending, switch to receiving.
                            if (timer > 0)
                            {
                                timer = 0;
                            }
                        }
                        // If we are idle or waiting for the network, sleep for a
                        // fraction of time to avoid busy looping.
                        if (idle)
                        {
                            Thread.sleep(100);
                            // Increase the timer. This is inaccurate but good enough,
                            // since everything is operated in non-blocking mode.
                            timer += (timer > 0) ? 100 : -100;
                            // We are receiving for a long time but not sending.
                            if (timer < -15000)
                            {
                                // Send empty control messages.
                                packet.put((byte) 0).limit(1);
                                for (int i = 0; i < 3; ++i)
                                {
                                    packet.position(0);
                                    tunnel.write(packet);
                                }
                                packet.clear();
                                // Switch to sending.
                                timer = 1;
                            }
                            // We are sending for a long time but not receiving.
                            if (timer > 20000)
                            {
                                throw new IllegalStateException("Timed out");
                            }
                        }
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