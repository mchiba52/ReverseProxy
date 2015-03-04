/**
 * Auto Generated Java Class.
 */
public class ReverseProxyTransformT2 implements Transform {
    private static final String[] T2TAGS = {"<BBB>", "</BBB>"};
    
    /* ADD YOUR CODE HERE */
    public byte[] transform(byte[] in) throws Exception {
        String inData = new String(in, 0, in.length);
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
                return outData.toString().getBytes();
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
            return outData.toString().getBytes();
        }
        //else return the input string intact
        return(in);
    }
}
