package io.github.profvanselow;

//Step 11
//    Create a class called MoviePlayer that extends Product and implements MultimediaControl.
public class MoviePlayer extends Product implements MultimediaControl {

  private Screen screen;
  private MonitorType monitorType;

  public Screen getScreen() {
    return screen;
  }

  public MonitorType getMonitorType() {
    return monitorType;
  }

  MoviePlayer(String n, String man, Screen s, MonitorType mon) {
    super(n, man, ItemType.VISUAL);
    screen = s;
    monitorType = mon;
  }

  @Override
  public void play() {
    System.out.println("Playing movie");
  }

  @Override
  public void stop() {
    System.out.println("Stopping movie");
  }

  @Override
  public void previous() {
    System.out.println("Previous movie");
  }

  @Override
  public void next() {
    System.out.println("Next movie");
  }

  @Override
  public String toString() {
    return super.toString() + "\r\n"
        + "Screen: \n" + screen + "\r\n"
        + "Monitor Type: " + monitorType;
  }
}
