package Process;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("---------------------------------------------------------------------------------");
        System.out.println("Please enter the link");
        String link = input.nextLine();
        System.out.println("-> Link : " + link);
        FileInformation info = new FileInformation(link);
        info.Information();
    }
   
}
