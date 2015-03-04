/*
 * Copyright (c) 1995, 2013, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */ 

import java.net.*;
import java.io.*;

public class RPMultiProxyThread extends Thread {
    private Socket socket = null;
    private static final int portNumber = 4446;

    public RPMultiProxyThread(Socket socket) {
        super("RPMultiServerThread");
        this.socket = socket;
    }
    
    /*
     * This function forwards the (un)modified data on to the server
     */
    private static void RPMultiProxyForward(byte[] outBuff) {
        String inLine;
        try (
             Socket fwdSoc = new Socket("localhost", portNumber);
             PrintWriter out = new PrintWriter(fwdSoc.getOutputStream(), true);
             BufferedReader in = new BufferedReader(
                new InputStreamReader(fwdSoc.getInputStream()));
            ) {
              String inData = new String(outBuff, 0, outBuff.length);
              out.println(inData);
              //We only expect to get back a 'Bye.' message
              while ((inLine = in.readLine()) != null) {
                  break;
              }
              fwdSoc.close();
            } catch (IOException e) {
            } 
    }
    
    public void run() {

        try (
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
         ) {
            byte[] inBuff = new byte[1024]; //storage for 1KB message exchange
            String outputLine = null;
            //Initialize dialog with client
            out.println(outputLine);
            int k = -1;
            ReverseProxyTransformT1 t1 = new ReverseProxyTransformT1();
            ReverseProxyTransformT2 t2 = new ReverseProxyTransformT2();
            
            while ((k = socket.getInputStream().read(inBuff, 0 , inBuff.length)) > -1) {
                String inputStream = new String(inBuff, 0, k-2);
                if (inputStream.equals("Bye.")) break;
                try {
                    RPMultiProxyForward(t2.transform(t1.transform(inBuff)));
                    //close client connection
                    out.println("Bye.");
                } catch (Throwable e) {
                }
            }
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
