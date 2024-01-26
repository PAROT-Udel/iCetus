package cetus.registration.controller;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * @author parinaz
 *The StreamGobbler class is a utility class used for handling the input stream (InputStream) from a subprocess 
 *(such as a command line process) in a separate thread. It is commonly used in Java programs that execute external 
 *processes to asynchronously capture and handle the output (both standard output and standard error) of those processes.  
 *The purpose of this class is to consume the output of an InputStream (which is typically the standard output or 
 *standard error of a subprocess) and print it to the console. By running this in a separate thread, it allows the 
 *capturing of the output to be asynchronous, preventing potential blocking issues when executing external processes. 
 *This is often used in conjunction with the ProcessBuilder class when starting external processes in Java.
 */
public class StreamGobbler extends Thread {
    private final InputStream is;
    private final String type;

    public StreamGobbler(InputStream is, String type) {
        this.is = is;
        this.type = type;
    }

    @Override
    public void run() {
        try {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(type + "> " + line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

