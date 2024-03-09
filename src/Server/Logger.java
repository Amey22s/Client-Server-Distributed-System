package Server;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * A logger class which contains all the logging functions which are used by other programs for creating logs
 * used for troubleshooting.
 */

public class Logger {

    /**
     * Method to print Request messages.
     *
     * @param str  message string to be printed.
     * @param ip   IP address of client.
     * @param port port number of client.
     */
    void requestLogger(String str, String ip, String port) {
        System.out.println(getTimeStamp() + "\n<< Request from :-> " + ip + ":" + port + " :-> " + str + ">>");
    }


    /**
     * Method to print Response messages.
     *
     * @param str message string to be printed
     */
    void responseLogger(String str) {
        System.out.println(getTimeStamp() + "\n<< Response :-> " + str + ">>");
    }

    /**
     * Method to print Error messages.
     *
     * @param err error message string to be printed
     */
    void errorLogger(String err) {
        System.out.println(getTimeStamp() + "\n<< Error :-> " + err + ">>");
    }


    /**
     * Method which returns the current timestamp.
     *
     * @return the current timestamp in string format.
     */
    String getTimeStamp() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm:ss.SSS");
        return "<< Time :-> " + simpleDateFormat.format(new Date()) + ">>";
    }

    
}
