package src;

import java.awt.FlowLayout;
import javax.swing.JFrame;
/**
   This program allows the user to view appointment frame
   Yuanbo Shi 500745024
*/
public class AppointmentViewer
{  
   public static void main(String[] args)
   {  
      JFrame frame = new AppointmentFrame();//set up new frame
      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//default close operation
      frame.setTitle("Appointments");//name my lovely frame
      frame.setVisible(true);//let users see it
   }
}