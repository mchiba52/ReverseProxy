/**
 * Auto Generated Java Class.
 */
public class ReverseProxyTransformT1 implements Transform {
    private static final String[] T1TAGS = {"<AAA>", "</AAA>"};
    private static final String T1Repl = "YYY";
  
    /* ADD YOUR CODE HERE */
    public byte[] transform(byte[] in) throws Exception {
        String inData = new String(in, 0, in.length);
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
                return outData.toString().getBytes();
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
            return outData.toString().getBytes();
        }
        // No instance of T1 tag return inData intact
        return(in);
    }
}
