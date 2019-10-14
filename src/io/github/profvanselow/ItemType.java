package io.github.profvanselow;

public enum ItemType {

  // ItemType AUDIO = new ItemType("AU");
  AUDIO("AU"),
  VISUAL("VI"),
  AUDIO_MOBILE("AM"),
  VISUAL_MOBILE("VM");

  public final String code;

  ItemType(String code) {
    this.code = code;

  }

}