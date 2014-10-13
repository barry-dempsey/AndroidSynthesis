package ie.dempsey.androidsynthesis;

import android.app.Activity;
import myAnalogMeter.*;
import android.content.Context;
import android.graphics.Color;
import android.media.AudioManager;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;
import android.widget.ToggleButton;

public class MainActivity extends Activity implements OnSeekBarChangeListener {
	private Button c, d, e, f, g, a, b;
	private RadioButton squareBtn;
	private RadioButton waWaBtn;
	private RadioButton fmBtn;
	private RadioButton decayPulseBtn;
	private RadioButton mixedSineBtn;
	private ToggleButton harmonicBtn;
	private ToggleButton octaveBtn;
	private ToggleButton stereoBtn;
	private TextView soundLabel;
	private String selection;
	private SeekBar duration;
	private SeekBar volSeekBar;
	private SeekBar freqSlider;
	private int length = 2;
	private double freq;
	private PlaySound playSound;
	AudioManager audioManager;
	private AnalogMeter meter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        harmonicBtn = (ToggleButton) findViewById(R.id.toggleButton1);
        harmonicBtn.setOnClickListener(new ButtonListener());
        octaveBtn = (ToggleButton) findViewById(R.id.toggleButton2);
        octaveBtn.setOnClickListener(new ButtonListener());
        duration = (SeekBar) findViewById(R.id.seekBar1);
        duration.setOnSeekBarChangeListener(this);
        duration.setMax(5);
        duration.setProgress(2);
        freqSlider = (SeekBar) findViewById(R.id.seekBar3);
        freqSlider.setMax(10);
        volSeekBar = (SeekBar) findViewById(R.id.seekBar2);
        squareBtn = (RadioButton) findViewById(R.id.radio0);
        squareBtn.setSelected(true);
        //selection = "square";
        waWaBtn = (RadioButton) findViewById(R.id.radio1);
        fmBtn = (RadioButton) findViewById(R.id.radio2);
        decayPulseBtn = (RadioButton) findViewById(R.id.radio3);
        mixedSineBtn = (RadioButton) findViewById(R.id.radio4);
        stereoBtn = (ToggleButton) findViewById(R.id.toggleButton3);
        soundLabel = (TextView)findViewById(R.id.soundLabel);
        soundLabel.setMovementMethod(new ScrollingMovementMethod());
        soundLabel.setSelected(true);
        soundLabel.setTextColor(Color.GREEN);
        /* set up buttons for music scales */
        //a.setOnClickListener(new ButtonListener());
        b = (Button) findViewById(R.id.button7);
        b.setOnClickListener(new ButtonListener());
        c = (Button) findViewById(R.id.button1);
        c.setOnClickListener(new ButtonListener());
        d = (Button) findViewById(R.id.button2);
        d.setOnClickListener(new ButtonListener());
        e = (Button) findViewById(R.id.button3);
        e.setOnClickListener(new ButtonListener());
        f = (Button) findViewById(R.id.button4);
        f.setOnClickListener(new ButtonListener());
        g = (Button) findViewById(R.id.button5);
        g.setOnClickListener(new ButtonListener());
        a = (Button) findViewById(R.id.button6);
        a.setOnClickListener(new ButtonListener());
        b= (Button) findViewById(R.id.button7);
        b.setOnClickListener(new ButtonListener());
        initControls();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_main, menu);
        return true;
    }
    
    @Override
	public void onProgressChanged(SeekBar seekBar, int progress,
			boolean fromUser) {
		length = progress;
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		// TODO Auto-generated method stub
	}
	
	public void triggerAudio(String selection, double freq) {
		if(getOctaveState().equals("octaveUp"))
			playSound = new PlaySound(
					getCheckButtonState(), length, (freq * 2), getNumberOfChannels(), harmonicBtn);
		else
			playSound = new PlaySound(
					getCheckButtonState(), length, freq, getNumberOfChannels(), harmonicBtn);
	}
	
	public String getCheckButtonState() {
		if(squareBtn.isChecked()){
			soundLabel.setText("Square Wave");
			return "square";
		}else if(waWaBtn.isChecked()){
			soundLabel.setText("WaWa Wave");
			return "wawa";
		}else if(fmBtn.isChecked()){
			soundLabel.setText("FM Sweep");
			return "fm";
		}else if(decayPulseBtn.isChecked()){
			soundLabel.setText("Decay Pulse");
			return "decayPulse";
		}else if(mixedSineBtn.isChecked()){
			soundLabel.setText("Mixed Sines Wave");
			return "mixedSines";
		}
		return "";
	}
	
	public String getNumberOfChannels() {
		if(stereoBtn.isChecked())
			return "stereo";
		else
			return "mono";
	}
	
	public String getOctaveState() {
		if(octaveBtn.isChecked()) {
			return "octaveUp";
		}
		return "";
	}
	
	private void initControls(){
        try{
            volSeekBar.setMax(audioManager
                    .getStreamMaxVolume(AudioManager.STREAM_MUSIC));
            volSeekBar.setProgress(audioManager
                    .getStreamVolume(AudioManager.STREAM_MUSIC));   


            volSeekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener(){
                @Override
                public void onStopTrackingTouch(SeekBar arg0){
                }

                @Override
                public void onStartTrackingTouch(SeekBar arg0){
                }

                @Override
                public void onProgressChanged(SeekBar arg0, int progress, boolean arg2){
                    audioManager.setStreamVolume(AudioManager.STREAM_MUSIC,
                            progress, 0);
                }
            });
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
	
	private class ButtonListener implements OnClickListener {

		@Override
		public void onClick(View v) {
			switch(v.getId()) {
			// pitch frequencies for each key
			case R.id.button1 : 
				// C
				triggerAudio(selection, 261.626);
				break;
			case R.id.button2 :
				// D
				triggerAudio(selection, 293.665);
				break;
			case R.id.button3 : 
				// E
				triggerAudio(selection, 329.628);
				break;
			case R.id.button4 :
				// F
				triggerAudio(selection, 349.228);
				break;
			case R.id.button5 :
				// G
				triggerAudio(selection, 391.995);
				break;
			case R.id.button6 :
				// A
				triggerAudio(selection, 440.00);
				break;
			case R.id.button7 :
				// B
				triggerAudio(selection, 493.883);
				break;
			}			
		}	
	}
}
