package Process;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.RandomAccessFile;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import javax.crypto.AEADBadTagException;

public class FileInformation {

    private URL _URL;
    private String link;
    public String fileName;
    public static String fileExtention;
    public static int fileSize;
    private int sizeOfThread;
    public static int countFinished = 0;
    public static String dateTCFormat;
    public static LocalDateTime myDateTimeStart;
    
    public FileInformation(String link) {
        this.link = link;
    }
    
    public void Information(){
        try {
            
            _URL = new URL(link);
            HttpURLConnection connection = (HttpURLConnection) _URL.openConnection();
            connection.connect();
            
            fileName = link.substring(link.lastIndexOf("/") + 1, link.length());
            fileName = fileName.replace("+", " ");
            fileName = fileName.replace("%", " ");
            fileExtention = link.substring(link.lastIndexOf(".") + 1, link.length());
            
            fileSize = connection.getContentLength();
            sizeOfThread = fileSize / 10;
            System.out.println("---------------------------------------------------------------------------------");
            System.out.println("------------------------File Information-----------------------");
            System.out.println("Filename : " + fileName);
            System.out.println("Type of file : " + fileExtention);
            System.out.println("Size : " + fileSize/1000000 +" MB");
            System.out.println("---------------------------------------------------------------");
            
            ExecutorService thread_pool = Executors.newFixedThreadPool(10);

            int starting_Point = 0, end_Point = sizeOfThread;
            System.out.println("Thread Size : " + sizeOfThread);  
            System.out.println("<<<...Downloading Thread...>>>");
            
            myDateTimeStart = LocalDateTime.now();
            DateTimeFormatter myFormatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            dateTCFormat = myDateTimeStart.format(myFormatDateTime);
            
            for (int threadNumber = 1; threadNumber <= 10; threadNumber++) {
                if (threadNumber < 10) {
                    thread_pool.submit(new DownloadThread(threadNumber, _URL, starting_Point, end_Point, sizeOfThread, fileName));
                    
                } else {
                    thread_pool.submit(new DownloadThread(threadNumber, _URL, starting_Point, fileSize, sizeOfThread, fileName));
                    
                }
                starting_Point = end_Point + 1;
                end_Point += sizeOfThread;
            }
            thread_pool.shutdown();
            
        } catch (MalformedURLException ex) {
            System.out.println("\n!!!!Please enter the link again.!!!!");
            Scanner input = new Scanner(System.in);
            String newlink = input.nextLine();
            System.out.printf("-> Link : " + newlink + "\n");
            FileInformation info = new FileInformation(newlink);
            info.Information();
        } catch (IOException ex) {
            System.err.println(ex);
        }
        
        
    }
}