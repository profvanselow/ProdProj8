package io.github.profvanselow;

// Step 2
// Create an enum called ItemType.
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