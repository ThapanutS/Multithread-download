package Process;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter; 

public class DownloadThread implements Runnable{

    private int threadNumber;
    private URL downloadFromURL;
    private int starting_Point;
    private int end_Point;
    private byte[] buffer;
    private HttpURLConnection connection;
    private int sideThread;
    private InputStream inputdata;
    private String fileName;
    private RandomAccessFile fileOUTPUT;

    DownloadThread(int threadNumber, URL _URL, int starting_Point, int end_Point,int sideThread,String fileName) {
        this.threadNumber = threadNumber;
        this.downloadFromURL = _URL;
        this.starting_Point = starting_Point;
        this.end_Point = end_Point;
        this.sideThread = sideThread;
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            connection = (HttpURLConnection) downloadFromURL.openConnection();
            System.out.printf("Processing Thread %d ...\n",threadNumber);
            connection.setRequestProperty("Range", "Bytes=" + starting_Point + "-" + end_Point);
            connection.connect();
            
            inputdata = connection.getInputStream();
            buffer = new byte[sideThread];
            fileOUTPUT = new RandomAccessFile(fileName, "rw");
            fileOUTPUT.seek(starting_Point);
            
            
            int read_1 = inputdata.read(buffer, 0, buffer.length);
            int byteForRead = read_1;
            while (byteForRead < sideThread) {
                read_1 = inputdata.read(buffer, byteForRead, (buffer.length - byteForRead));
                if (read_1 != -1) {
                    byteForRead += read_1;
                    System.err.printf("Thread %d :: %d // %d\n",threadNumber,byteForRead,(end_Point - starting_Point));
                }
            }
            
            fileOUTPUT.write(buffer, 0, byteForRead);
            System.out.printf("Suscessfully Downloaded Thread[%d]\n",threadNumber);
            FileInformation.countFinished++;
            
            if (FileInformation.countFinished == 10) {
                
                LocalDateTime myDateTimeModified = LocalDateTime.now();
                int before = FileInformation.myDateTimeStart.getMinute(),after = myDateTimeModified.getMinute();
                DateTimeFormatter myFormatDateTime = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
                String dateTMFormat = myDateTimeModified.format(myFormatDateTime);
                int totaltime = after - before;
                
                System.out.println("<<<...Finished Thread...>>>");
                System.out.println("<<<...Download Finished...>>>");
                System.out.printf("Total time of download : %d Minute\n",totaltime);
                
                System.out.println("------------------------------------- File -------------------------------------");
                System.out.println("Filename : " + fileName);
                System.out.println("Type of file : " + FileInformation.fileExtention);
                System.out.println("Size : " + FileInformation.fileSize / 1000000 + " MB");
                System.out.println("Date created : " + FileInformation.dateTCFormat);
                System.out.println("Date modified : " + dateTMFormat);
                System.out.println("--------------------------------------------------------------------------------");
            }
            inputdata.close();
            fileOUTPUT.close();
            
        } catch (IOException ex) {
            System.err.println(ex);
        }  
    }
}
