 /*This is a simulation program written in Java.  Each day the news boy must order the papers for the next day.    
  He buys the papers for 10 cents and sells them for 20 cents.  The simulation runs for 100 days and generates the paper demand (from 10 to 15 per day)
  randomly for an observed distribution.  Over the 1000 day period, the simulation collects profit per day, average profit per day and the average papers 
  demanded per day.     
  */
  import java.io.*;
  import java.io.IOException;
  import javax.swing.*;
  import java.util.*;
  import java.io.File;
  import java.io.FileNotFoundException;
  import java.lang.*;
  import java.util.NoSuchElementException;
  import java.math.*;
 
 
  public class NewsBoySim3 {
      public static void main(String[] args) throws Exception {
 
          PrintWriter outpt;
          // now equate the internal name to an external file through the PrintWriter
 
          outpt = new PrintWriter(new File("NewsBoyOut.txt"));
          int i, day;
          // Inventory pans=new Inventory(15.75, 10);
 
          cstats statics = new cstats(); //Initiate statistics
          newsboy joe = new newsboy(); // initiate Joe
          dmdproc wantpaper = new dmdproc(); //initiate wantpaper process
          int dmd;
          double sales, money;
          //now start a loop to test joe's behavior for 5 days.  Have demand constant at 11
          for (day = 1; day <= 1000; day++) { // get the demand for today
              dmd = wantpaper.dmdtoday();
              //give Joe the demand for today
              joe.setdemand(dmd);
              // record the statistics for this day
              statics.setprofit(joe.getprofit());
              // order papers for tomorrow
              joe.order();
              if (day >= 500 && day <= 505) {
                  System.out.println("for day " + day + " demand " + dmd + " sold " + joe.getsold());
                  System.out.println(" profit " + joe.getprofit() + " ordered " + joe.getordered());
                  outpt.println("for day " + day + " demand " + dmd + " sold " + joe.getsold());
                  outpt.println(" profit " + joe.getprofit() + " ordered " + joe.getordered());
              }
 
          }; //end of timing loop
          System.out.println("sold " + joe.getsold() + " ordered " + joe.getordered());
          System.out.println("profit " + joe.getprofit());
          System.out.println("**************Statics for 1000 Days of Sales***************");
 
          System.out.println("average profit " + statics.getaverage());
          System.out.println("variance " + statics.getvar() + " st dev " + statics.getstdev());
          System.out.println("count " + statics.getcount());
          System.out.println("average sold: " + statics.getAvgSold());
          System.out.println("average demanded: " + statics.getAvgDmd());
          System.out.println("average profit per day: " + statics.getAvgProf());
          System.out.println("variance for sold papers: " + statics.getVarSold());
          System.out.println("variance for demanded papers: " + statics.getVarDmd());
          System.out.println("varince for avg daily profit: " + statics.getVarProf());
          // a test of the Math.random function
          
          /*
          int x;
          for (i = 1; i <= 30; i++) {
              x = (int)(Math.random() * 100);
 
              System.out.println("Math.random test: " + x);
          }
          */

      } // end of main
  } //end of newsboysim
 
  class newsboy {
      public static int demand; // this is the demand for the day
      private int ordered; // this is the amount ordered for today
      private int bought; //this is the amount bought for today
      public static int sold; // this is the amount sold for today
      private double profit; //
      private int overflow;
      // now for the behaviors of the newsboy
      public newsboy() { // this is the constructor for the newsboy
          // set all values to 0 and start him with 10 papers
          demand = 0;
          ordered = 10;
          bought = 0;
          sold = 0;
          profit = 0.0;
      } //end of newsboy constructor
      public int order() { // this is a private policy function for how many the newsboy will order daily
          int x;
          x = demand - 1; // order 12 papers daily
          ordered = x;
          return x;
      }
      private void behavior() { // this is the behavior of the newsboy.
          // recieve the papers ordered yesterday
          bought = ordered;
          //  System.out.println ("nboybehavior bought "+bought+" demand "+demand);
          // calculate the papers sold
          if (demand >= bought) sold = bought;
          else
              sold = demand;
          // calculate profit
          if (sold == ordered){
              profit = 1 * sold - .35 * bought;
          }
          else if (sold < ordered){
              overflow = ordered - sold;
              profit = 1 * sold - .35 * bought + .05 * overflow;
          }
          
          // order for tomorrow
          ordered = order();
          // System.out.println(" bought "+bought+" ordered "+ordered+" demand "+demand+" sold "+sold);
      } // this is the end of the behavior of the newsboy
      public void setdemand(int x) { // we will give the newsboy a demand and then let him behave as appropriate
          demand = x;
          //given the demand for the day, activate the the behavior of the newsboy object
          behavior();
      } //end of setdemand
 
 
      //********************************Now Create the Utility functions to Interrograte the News Boy Objecct
      public double getprofit() {
          return profit;
      }
      public int getsold() {
          return sold;
      }
      public int getordered() {
          return ordered;
      }
  } // end of newsboy class
  //**********************Now  Setup the Calculator Class*********************************
  class cstats {
      private double profit; //profit for today
      private double psum; //sum of profit for all days
      private double psum2; //sum squaared of profit
      private double average; //average profit
      private double stdev; //standard deviation
      private double variance; // variance
      private int count; //
      private double avgSold; 
      private double avgDmd;
      private double varSold;
      private double varDmd;
      private double avgProf;
      private double varProf;
      private int totSold;
      private int totDmd;
      public cstats() { //constructor for cstats
          profit = psum = psum2 = average = stdev = variance = 0;
          count = 0;
      }
      public void setprofit(double x) { // this function  sets profit and calculates the stats for the day
          totSold += newsboy.sold;
          totDmd += newsboy.demand;
          profit = x;
          psum += profit;
          psum2 += profit * profit;
          count++;
          average = psum / count;
          variance = psum2 / count - average * average;
          stdev = Math.sqrt(variance);
          avgSold = totSold / count;
          avgDmd = totDmd / count;
          varSold = (totSold * totSold) / count - totSold * totSold;
          varDmd = (totDmd * totDmd) / count - totDmd * totDmd;
          avgProf = psum / count;
          varProf = (avgProf * avgProf) / count - avgProf * avgProf;
          return; 
      } // end of setprofit
      //  Utility functions to return values from cstats
      public double getprofit() {
          return profit;
      }
      public double getaverage() {
          return average;
      }
      public double getvar() {
          return variance;
      }
      public double getstdev() {
          return stdev;
      }
      public int getcount() {
          return count;
      }
      public double getAvgSold(){
          return avgSold;
      }
      public double getAvgDmd(){
          return avgDmd;
      }
      public double getVarSold(){
          return varSold;
      }
      public double getVarDmd(){
          return varDmd;
      }
      public double getAvgProf(){
          return avgProf;
      }
      public double getVarProf(){
          return varProf;
      }
  } // end of class cstats
  class dmdproc { //this is the process generator for the demand
      private int demand;
      public dmdproc() { // this is the conctructor for dmdproc
          demand = 0;
      }
      public int dmdtoday() { 
          
        //this is the process generator for the demand today
        //the demand for papers considered with percents on a daily basis is
        // Freq. is out of 12. 100/12 = 8.3 so the given range is as close as integer 
        //division will get to that frequency
          int x; // this is the random variante U(0-100)
 
          x = (int)(Math.random() * 100);
          if (x <= 8) demand = 15;
          else
          if (x <= 16) demand = 16;
          else
          if (x <= 58) demand = 17;
          else
          if (x <= 75) demand = 18;
          else
          if (x <= 92) demand = 19;
          else
              demand = 20;
          //System.out.println(" x and demand"+x+"  "+demand);
          return demand;
      } // end of dmdtoday
  } //end of class dmdproc