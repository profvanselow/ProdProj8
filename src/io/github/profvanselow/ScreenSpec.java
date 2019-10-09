package io.github.profvanselow;

public interface ScreenSpec {

  String getResolution();

  int getRefreshRate();

  int getResponseTime();
  // modifier public is redundant for interface methods
}
