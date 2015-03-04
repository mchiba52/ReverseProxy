/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
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

public class ReverseProxyProtocol 
{
    private static final String[] T1TAGS = {"<AAA>", "</AAA>"};
    private static final String[] T2TAGS = {"<BBB>", "</BBB>"};
    private static final String T1Repl = "YYY";

    //Method to process the T1 type TAG instances
    private static String processTagT1(String inData) 
    {
        StringBuilder outData = new StringBuilder();
        int fromIndex = 0;
        int start = 0, end = 0;
        
        if (inData == null) return null;
        while ((end = inData.indexOf(T1TAGS[0], fromIndex)) != -1) {
            //Add data upto the first T1TAG
            outData.append(inData.substring(start,end));
            //Append the T1 start TAG 
            outData.append(T1TAGS[0]);
            //Skip over the start tag to search for end tag
            start = end + T1TAGS[0].length();
            //search for end tag
            if ((end = inData.indexOf(T1TAGS[1], start)) == -1) {
                //No end tag found
                //Check if there are other start tags
                if ((end = inData.indexOf(T1TAGS[0], start)) != -1) {
                    //update the place to resume processing
                    fromIndex = start;
                    //reached max data then break else continue processing
                    if (fromIndex > inData.length()) break;
                    continue;
                }
                System.err.println("T1 end tag not found; T1 operation skipped");
                //Add remainder of string after last T1 Tag
                outData.append(inData.substring(start, inData.length()));
                return outData.toString();
            }
            //Append replacement text
            outData.append(T1Repl);
            //Append T1 end TAG
            outData.append(T1TAGS[1]);
            fromIndex = start = end + T1TAGS[1].length();
            if (fromIndex > inData.length()) break;
        }
        
        //If we found atleast one tag then return outData 
        if ((end != 0) && (end < inData.length())) {
            outData.append(inData.substring(start, inData.length()));
            return outData.toString();
        }
        // No instance of T1 tag return inData intact
        return(inData);
    }
    
    //Method to process the T2 type TAG instance
    private static String processTagT2(String inData)
    {
        StringBuilder outData = new StringBuilder();
        int fromIndex = 0;
        int start = 0, end = 0;
        
        if (inData == null) return null;
        while ((end = inData.indexOf(T2TAGS[0], fromIndex)) != -1) {
            //Append initial data
            outData.append(inData.substring(start,end));
            //Append the start tag
            outData.append(T2TAGS[0]);
            //skip over the start tag
            start = end + T2TAGS[0].length();
            //Search for end tag
            if ((end = inData.indexOf(T2TAGS[1], start)) == -1) {
                //No end tag found
                //check if there is another start tag
                if ((end = inData.indexOf(T2TAGS[0], start)) != -1) {
                    //Found another start tag, update the place to resume from
                    fromIndex = start;
                    //reached max limit
                    if (fromIndex > inData.length()) break;
                    continue;
                }
                System.err.println("T2 end tag not found; T2 operation skipped");
                //Append remainder of string
                outData.append(inData.substring(start, inData.length()));
                return outData.toString();
            }
            //Append the lower case version of string
            outData.append((inData.substring(start, end)).toLowerCase());
            //Append end tag
            outData.append(T2TAGS[1]);
            //update the location to continue from
            fromIndex = start = end + T2TAGS[1].length();
            //reached max size
            if (fromIndex > inData.length()) break;
        }

        //If we found atleast one tag then return modified string
        if ((end != 0) && (end < inData.length())) {
            outData.append(inData.substring(start, inData.length()));
            return outData.toString();
        }
        //else return the input string intact
        return(inData);
    }
    public String processInput(String inData) 
    {
        return processTagT1(processTagT2(inData));
    }
}
