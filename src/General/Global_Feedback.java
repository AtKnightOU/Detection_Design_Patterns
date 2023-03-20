package General;

import java.util.ArrayList;
import java.io.*;
import java.util.Date;

/**
 *
 * @author E.M. van Doorn
 */
public class Global_Feedback {

    private static Global_Feedback instance = null;
    private static ArrayList<String> feedbacks;
    private static BufferedWriter f_output;
    private static String fileName;
    private static int showUrgency;

    // private constructor to avoid client applications to use constructor
    private Global_Feedback() {
        feedbacks = null;
        f_output = null;
        fileName = "DetectionDP.log";
        showUrgency = 0;
        // Every message will be shown.
    }

    public static Global_Feedback getInstance() {
        if (instance == null) {
            instance = new Global_Feedback();
            feedbacks = new ArrayList();

            f_output = openLogFile();
            instance.writeDateTime();
        }

        return instance;
    }
    
    private void writeDateTime() {
        try {
            Date now = new Date();
            
            f_output.write(now.toString() + "\n");
        }
        catch(IOException e) {
            System.out.println("Problem writing Date and time to " + fileName);
            System.exit(1);
        }
    }

    public static void setFeedbackModus(String arg) {
        getInstance();
            // only to create this object 
            
        try {
            if (arg.startsWith("-f")) {
                showUrgency = Integer.parseInt(arg.substring(2));
            }
        } catch (Exception e) {
            Global_Feedback.showOrWite(0, "flag feedback could not be processed, Flag = " + arg);
            System.exit(1);
        }
    }

    private static BufferedWriter openLogFile() {
        try {
            return new BufferedWriter(new FileWriter(fileName));

        } catch (IOException e) {
            System.out.println("Problem openening " + fileName);
            System.exit(1);
        }

        return null;
    }

    public static void showOrWite(int urgency, String msg) {
        getInstance();
                
        if (urgency <= showUrgency) {
            System.out.println(msg);
        }

        try {
            f_output.write(msg + "\n");
        } catch (IOException e) {
            System.out.println("Problem writing to " + fileName);
            System.exit(1);
        }
    }

    public static void addFeedback(String comment) {
        getInstance();
        
        feedbacks.add(comment);
    }

    public  static ArrayList<String> allFeedback() {
        ArrayList<String> result;

        result = new ArrayList<>();
        result.addAll(feedbacks);

        return result;
    }

    private static void closeLogFile() {
        instance.writeDateTime();
        
        try {
            f_output.close();
        } catch (IOException e) {
            System.out.println(fileName + " could not be closed.");
            System.exit(1);
        }
    }

    public static void close() {
        getInstance();
        
        closeLogFile();
        instance = null;
        feedbacks = null;   
    }
}
