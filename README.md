# ReverseProxy
Proxy to modify input based on rules associated with tags

The proxy listens on a defined TCP port, parses input data, transforms it and passes to a defined server. 

Use standard/open-source applications as client and server.

The input data is always printable characters(PC) and is of the following format:

<Random sequence of PC><tag-1-start>Random sequence of PC<tag-1-end>Random sequence of PC<tag-2-start>Random sequence of PC<tag-2-end>Random sequence of PC<EOF>

Tags cannot be be interleaved, but there can be multiple occurrences of <tag-N-start> ...<tag-N-end>

When tags are identified, invoke class that implements the transformer interface:

interface Transformer {

 /**

 * takes an input byte array and returns an output byte array of transformed data.

 */

 byte[] transform(byte[] in) throws Exception;

}

Two tags are defined: T1 and T2. Write two corresponding transformation classes that:

When T1 is found, replaces the data between start and end with a pre-defined text.
When T2 is found, replaces all upper case letters with their lower case ones
