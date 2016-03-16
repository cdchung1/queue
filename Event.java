class Event {
  double arrivaltime;
  double servicestarttime;
  double servicetime;
  double serviceendtime;
  double wait;
  
  //Constructor for the Event Class
  public Event(double x,double y,double z) {
    arrivaltime = x;
    servicestarttime = y;
    servicetime = z;
    serviceendtime = y + z;
    wait = y-x;
  }
}