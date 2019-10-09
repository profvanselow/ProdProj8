package io.github.profvanselow;

// Step 5
// Create class AudioPlayer that is a subclass of Product and implements MultimediaControl interface
public class AudioPlayer extends Product implements MultimediaControl {

  private final String supportedAudioFormats;
  private final String supportedPlaylistFormats;

  AudioPlayer(String n, String m, String saf, String spf) {
    super(n, m, ItemType.AUDIO);
    this.supportedAudioFormats = saf;
    this.supportedPlaylistFormats = spf;

  }

  @Override
  public String toString() {
    return super.toString() + "\r\n" +
        "Supported Audio Formats: " + supportedAudioFormats + "\r\n" +
        "Supported Playlist Formats: " + supportedPlaylistFormats;
  }

  @Override
  public void play() {
    System.out.println("Playing");
  }

  @Override
  public void stop() {
    System.out.println("Stopping");
  }

  @Override
  public void next() {
    System.out.println("Next");
  }

  @Override
  public void previous() {
    System.out.println("Previous");
  }
}
