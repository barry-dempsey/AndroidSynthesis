package ie.dempsey.androidsynthesis;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;
import android.widget.ToggleButton;

public class PlaySound {
	private int duration;
	private final int sampleRate;
	private final int numOfSamples;
	//private final double sample[];
	private final double freq;
	private String selection;
	private ToggleButton harmonicBtn;
	private String harmonics;	
	private String numberOfChannels;
	private final byte generatedSnd[];
	private final int monoFormat = AudioFormat.CHANNEL_OUT_MONO;
	private final int stereoFormat = AudioFormat.CHANNEL_OUT_STEREO;
	
	public PlaySound(String selection, int length, double freq, 
			String numberOfChannels, ToggleButton harmonicBtn) {
		this.harmonicBtn = harmonicBtn;
		this.freq = freq;
		this.selection = selection;
		this.numberOfChannels = numberOfChannels;
		System.out.println(selection);
		duration = length;
		if(duration <1) duration = 1;
		sampleRate = 16000;
		numOfSamples = duration * sampleRate;
		//sample = new double[numOfSamples];
		generatedSnd = new byte[2 * numOfSamples];
		if(harmonicBtn.isChecked())
			new SynGen(freq, "yes").getSyntheticData(generatedSnd, selection);
		else
			new SynGen(freq, "no").getSyntheticData(generatedSnd, selection);
		new PlayThread().start();
	}
	
	private class PlayThread extends Thread{
		public void run() {
			if(numberOfChannels.equals("mono")) {
			final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
		            sampleRate, AudioFormat.CHANNEL_OUT_MONO,
		            AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
		            AudioTrack.MODE_STREAM);
		    audioTrack.write(generatedSnd, 0, generatedSnd.length);
		    audioTrack.play();	
		}else if(numberOfChannels.equals("stereo")) {
			final AudioTrack audioTrack = new AudioTrack(AudioManager.STREAM_MUSIC,
		            sampleRate, AudioFormat.CHANNEL_OUT_STEREO,
		            AudioFormat.ENCODING_PCM_16BIT, generatedSnd.length,
		            AudioTrack.MODE_STREAM);
		    audioTrack.write(generatedSnd, 0, generatedSnd.length);
		    audioTrack.play();	
			}		
		}
	};	    
}

