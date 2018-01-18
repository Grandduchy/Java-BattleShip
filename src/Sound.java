// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// Sound class is the class responsible of playing the effects and songs
// This is done by playing .wav files in the project.
// ===========================================================================

import java.io.*;
import javax.sound.sampled.*;


public class Sound {
	
	public Sound() {
		playTrack("Nothing.wav");
	}
	
	public String currentSong() {
		return currentTrack;
	}
	// Function changes the current clip object to the resource.
	// If resource does not exist, all music is terminated
	private void playTrack(String resource) {
		boolean hasError = false;
		try {
			// get the file of the audio
			File fn = new File("");
			File mFile = new File(fn.getAbsoluteFile() + slash + "Music" + slash + resource);
			// use the file to create an audioStream
			AudioInputStream audioIn = AudioSystem.getAudioInputStream(mFile.getAbsoluteFile());
			DataLine.Info info = new DataLine.Info(Clip.class, audioIn.getFormat());
			// Get a sound clip resource.
			clip = (Clip) AudioSystem.getLine(info);
			// Open audio clip and load samples from the audio input stream.
			clip.open(audioIn);
			// change the volume to maxiumum
			FloatControl.Type type = isWindows ? FloatControl.Type.MASTER_GAIN : FloatControl.Type.VOLUME;
			FloatControl volume = (FloatControl) clip.getControl(type);
			volume.setValue(volume.getMaximum());
			currentTrack = resource;
		} catch (Exception e) {
			e.printStackTrace();
			hasError = true;
		} finally {
			if (hasError) {
				System.out.println("Error playing music, music/sound has been disabled.");
				BattleShipGame.allowMusic = false;
			}
		}
	}
	// plays a song
	public void play(String resource) {
		if (isPlaying())
			stop();
		playTrack(resource);
		clip.start();
	}
	
	public boolean isPlaying() {
	    return clip.isRunning();
	}
	// function returns if clip is playing the same as resource.
	public boolean isPlayingSame(String resource) {
		return currentTrack.equalsIgnoreCase(resource);
	}
	// Function plays a sound effect, this function must not use playTrack because
	// using the function stops the current clip, where we would want the clip to continue playing
	// instead, a new clip is made parallel to the original clip.
	public void playSoundEffect(String resource) {
		boolean hasError = false;
		try {
			File fn = new File("");
			File mFile = new File (fn.getAbsoluteFile() + slash + "Music" + slash + resource);
			
			
	        AudioInputStream audioIn = AudioSystem.getAudioInputStream(mFile.getAbsoluteFile());
	        DataLine.Info info = new DataLine.Info(Clip.class, audioIn.getFormat());
	        // Get a sound clip resource.
	        Clip effectClip = (Clip)AudioSystem.getLine(info);
	        // Open audio clip and load samples from the audio input stream.
	        effectClip.open(audioIn);
	        FloatControl.Type type = isWindows ? FloatControl.Type.MASTER_GAIN : FloatControl.Type.VOLUME;
	        FloatControl volume = (FloatControl)clip.getControl(type);
	        volume.setValue(volume.getMaximum());
	        effectClip.start();
		} catch(Exception e) {
			hasError = true;
			e.printStackTrace();
		}
		finally {
			if (hasError) {
				System.out.println("Error playing music, music/sound has been disabled.");
				BattleShipGame.allowMusic = false;
			}
		}
	}
	// stops the sound
	public void stop() {
		clip.stop();
		clip.flush();
		clip.drain();
		clip.close();
	}
	
	public static Clip clip;
	String currentTrack;
	
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static boolean isWindows = OS.indexOf("win") >= 0;
	private static String slash = isWindows ? "\\" : "/";
	
	
}
