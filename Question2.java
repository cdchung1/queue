import java.util.*;
/*Name: Charles Chung
 * This program simulates an M/M/1 Queue. We need the Event.java file and the Statistics.java file. 
 * Enter a lamda, Ts, and simulation time, and this program prints out a 95% percent confidence interval for
 * Tw, w, Tq, q, and rho.
 */

public class Question2{
  
  static double ExponentialDistribution(int lamda){
    Random roll = new Random();
    double number;
    number = roll.nextDouble();
    
    double returnvalue = Math.log(1-number);
    returnvalue = returnvalue *-1;
    returnvalue = returnvalue/lamda;
    return returnvalue; 
  }
  static double ExponentialDistribution(double lamda){
    Random roll = new Random();
    double number;
    number = roll.nextDouble();
    
    double returnvalue = Math.log(1-number);
    returnvalue = returnvalue *-1;
    returnvalue = returnvalue/lamda;
    return returnvalue; 
  }
  
  public static void main(String[] args){
    Scanner user_input = new Scanner(System.in);
    
    int lamda;
    System.out.println("Enter lamda (int): ");
    lamda = user_input.nextInt();
    
    double Ts;
    System.out.print("Enter Ts (double): ");
    Ts = user_input.nextDouble();
    
    int simulationtime;
    System.out.println("Enter simulation time (int): ");
    simulationtime = user_input.nextInt();
    user_input.close();
    
    double averageRho = 0;
    double averageTw = 0;
    double averagew = 0;
    double averageTq = 0;
    double averageq = 0;
    
    for(int i = 0; i < 100; i++){   //The number of loops is arbitrary, enough to find a 'sample mean'
      Statistics A = Simulation(lamda, Ts, simulationtime);
      averageRho += A.rho;
      averageTw += A.Tw;
      averagew += A.w;
      averageTq += A.Tq;
      averageq += A.q;
    }
     averageRho = averageRho/100;
     averageTw = averageTw/100;
     averagew = averagew/100;
     averageTq = averageTq/100;
     averageq = averageq/100;
     
     ResultCalculator(averageRho, averageTw, averagew, averageTq, averageq,lamda, Ts, simulationtime);
  }
  
  static Statistics Simulation(int lamda, double Ts, int simulationtime){
    double t = 0;
    double nArrivalTime = 0;
    double nServiceStartTime = 0;
    double nServiceTime = 0;
    int numValues = 0;
    
    LinkedList<Event> Schedule = new LinkedList<Event>();//Create the schedule.
    
    while(t< simulationtime){
      
      if(numValues == 0){
        nArrivalTime = ExponentialDistribution(lamda);
        nServiceStartTime = nArrivalTime;
        numValues +=1;
//        System.out.println("First element of list created here");
      }
      else{
        nArrivalTime += ExponentialDistribution(lamda);
        nServiceStartTime = Math.max(nArrivalTime, Schedule.getLast().serviceendtime); //This covers empty/not empty queue
      }
      
      nServiceTime = ExponentialDistribution(1/Ts);
      
      
      
      Event A = new Event(nArrivalTime, nServiceStartTime, nServiceTime);          //creates Event class object and adds to schedule
      Schedule.add(A);
      t = nArrivalTime; //update time
    }
    
    //Statistics are calculated. We traverse the schedule linkedlist objects to get our values!
    //Tw and w are calculated
    Double totalwaittime = 0.0;
    Double counter = 0.0;
    
    for(int i = 0; i < Schedule.size(); i++){
//      totalwaittime += Schedule.get(i).servicestarttime - Schedule.get(i).arrivaltime;
      totalwaittime += Schedule.get(i).wait;
      counter+= 1.0;
    }
    
    Double Tw = totalwaittime/counter; //mean wait time
    Double w = Tw*lamda; //mean amount of 'customers' waiting
    
    //Tq and q are calculated
    Double totalTq = 0.0;
    counter = 0.0;
    
    for(int i = 0; i< Schedule.size(); i++){
      totalTq += Schedule.get(i).wait + Schedule.get(i).servicetime;
      counter+= 1.0;  
    }
    Double Tq = totalTq/counter; //mean Time in system
    Double q = Tq*lamda; //mean number of 'customers' in system
    Double rho = lamda*Ts;
    
//    System.out.println("Given Lamda is " + lamda);
//    System.out.println("Given Ts is "+ Ts);
//    System.out.println("Given simulation time is "+ simulationtime);
//    System.out.println("utilization (rho) is " + rho);
//    System.out.println("Tw is " + Tw);
//    System.out.println("w is "+ w);
//    System.out.println("Tq is " + Tq);
//    System.out.println("q is " + q);    //Uncomment to see the statistics of one simulation run. 
    
    Statistics A = new Statistics(rho, Tw, w, Tq, q);
    return A;
    
  }
  static void ResultCalculator(double averageRho,double averageTw,double averagew,double averageTq,double averageq, int lamda, double Ts, int simulationtime){
    double resultRho =0;
    double resultTw = 0;
    double resultw = 0;
    double resultTq = 0;
    double resultq = 0;
    
    for(int i = 0; i < 100; i++){   
      Statistics A = Simulation(lamda, Ts, simulationtime);
      resultRho += (A.rho - averageRho)*(A.rho - averageRho);
      resultTw += (A.Tw - averageTw)*(A.Tw - averageTw);
      resultw += (A.w - averagew)*(A.w - averagew);
      resultTq += (A.Tq - averageTq)*(A.Tq - averageTq);
      resultq += (A.q - averageq)*(A.q - averageq);
    }
    resultRho = resultRho/100;   //the 'means of squard differences from the sample mean' for each output. This is variance
    resultTw = resultTw/100;
    resultw = resultw/100;
    resultTq = resultTq/100;
    resultq = resultq/100;
    
    resultRho = Math.sqrt(resultRho); //These are the standard deviations for each output. 
    resultTw = Math.sqrt(resultTw);
    resultw = Math.sqrt(resultw);
    resultTq = Math.sqrt(resultTq);
    resultq = Math.sqrt(resultq);
    
    double eRho = (1.96 * (resultRho/(Math.sqrt(100))));
    double eTw = (1.96 * (resultTw/(Math.sqrt(100))));
    double ew = (1.96 * (resultw/(Math.sqrt(100))));
    double eTq = (1.96 * (resultTq/(Math.sqrt(100))));
    double eq = (1.96 * (resultq/(Math.sqrt(100))));
    
    System.out.println("At 95% confidence: ");
    System.out.println("Utilization (rho) = "+ averageRho + " +/- " + eRho);
    System.out.println("Tw = " + averageTw + " +/- " + eTw);
    System.out.println("w = " + averagew + " +/- " + ew);
    System.out.println("Tq = " + averageTq + " +/- " + eTq);
    System.out.println("q = " + averageq + " +/- " + eq);
  }
  
  
}



