// ===========================================================================
// Created by : Joshua Challenger
// Date : Wednesday January 8, 2018
// AudioThread is a running thread in the program used soley for audio.
// The thread keeps running as long as BattleShipGame::allowMusic is true or terminate is called.
// ===========================================================================

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class AudioThread implements Runnable {
	
	public static volatile String currentSong = "";
	public static volatile String currentEffect = "";
	public static int soundIndex = 1;
	public static boolean MainGameSound = false;
	public static Thread music;
	public static Sound sound;
	// Function runs a new thread
	public static void initSound() {
		sound = new Sound();
		music = new Thread(new AudioThread());
		music.start(); 
	}	
	
	@Override
	public void run() {
		loop:
		while (true) {
			// wait .3 seconds before checking if sound is needed to be changed each time
			if (Thread.currentThread().isInterrupted())
				break loop;
			try {
				Thread.sleep(300);
			} catch (Exception e) { break; }
			
			if (!BattleShipGame.allowMusic) break;
			String song = readSong();
			String effect = readEffect();
			
			if (MainGameSound) {
				// stop the current song if the thread is not playing a game song
				boolean isPlayingGameS = song.substring(0, 4).equalsIgnoreCase("game");
				if (!isPlayingGameS) {
					sound.stop();
					try {
						Thread.sleep(300);
					} catch(Exception e) {break;}
				}
				// otherwise play the next song if not playing
				if (!sound.isPlaying()) {
					String gameSound = getSound();
					sound.play(gameSound);
					setSong(gameSound);
				}
			}
			else {
				if ( !song.isEmpty() &&
						(song != sound.currentSong() || !sound.isPlaying()) ) {
					sound.play(song);
				}
			}
			
			if (!effect.isEmpty()) {
				// play the effect and remove the effect buffer
				sound.playSoundEffect(effect);
				setEffect("");
			}
			
		}
		if (sound.isPlaying())
			sound.stop();
	}
	
	public String readEffect() {
		String eff = "";
		synchronized(this) {
			eff = currentEffect;
		}
		return eff;
	}
	
	public String readSong() {
		String song = "";
		synchronized(this) {
			song = currentSong;
		}
		return song;
	}
	
	public static void setSong( final String song) {
		Lock lock = new ReentrantLock();
		try {
			lock.lock();
			currentSong = song;
		} 
		catch (Exception e) {
			System.out.println("Error enabling music, disabling music...");
			BattleShipGame.allowMusic = false;
		}
		finally {
			lock.unlock();
		}
	}
	
	public static void setEffect(final String eff) {
		Lock lock = new ReentrantLock();
		try {
			lock.lock();
			currentEffect = eff;
		} 
		catch (Exception e) {
			System.out.println("Error enabling music, disabling music...");
			BattleShipGame.allowMusic = false;
		}
		finally {
			lock.unlock();
		}
	}
	
	public static void disableMainSound() {
		Lock lock = new ReentrantLock();
		try {
			lock.lock();
			MainGameSound = false;
		} 
		catch (Exception e) {
			System.out.println("Error enabling music, disabling music...");
			BattleShipGame.allowMusic = false;
		}
		finally {
			lock.unlock();
		}
	}
	
	public static void enableMainSound() {
		Lock lock = new ReentrantLock();
		try {
			lock.lock();
			MainGameSound = true;
		} 
		catch (Exception e) {
			System.out.println("Error enabling music, disabling music...");
			BattleShipGame.allowMusic = false;
		}
		finally {
			lock.unlock();
		}
	}
	
	
	public static String getSound() {
		if (soundIndex == 6)
			soundIndex = 1;
		return "Game " + soundIndex++ + ".wav";
	}
	
	public static void setHitEffect(boolean didHit) {
		if (didHit)
			AudioThread.setEffect("Explosion.wav");
		else
			AudioThread.setEffect("Splash.wav");
	}
	
	public static void terminate() {
		try {
			music.interrupt();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
}
