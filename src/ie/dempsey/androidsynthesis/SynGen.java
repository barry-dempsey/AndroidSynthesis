package ie.dempsey.androidsynthesis;

import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

public class SynGen {
	ByteBuffer byteBuffer;
	ShortBuffer shortBuffer;
	int byteLength;
	int channels;
	float sampleRate;
	double freq;
	double sinValue;
	String selection;
	String harmonics;
	
	public SynGen(double freq, String harmonics) {
		//save incoming
		this.freq = freq;
		this.harmonics = harmonics;
	}

	public void getSyntheticData(byte[] synDataBuffer, String selection) {
		// save selection and generate synthetic data
		this.selection = selection;
		byteBuffer = ByteBuffer.wrap(synDataBuffer);
		shortBuffer = byteBuffer.asShortBuffer();
		byteLength = synDataBuffer.length;
		if(selection.equals("square")) squareTones();
		if(selection.equals("wawa")) waWaPulse();
		if(selection.equals("fm")) fmSweep();
		if(selection.equals("decayPulse")) decayPulse();
		if(selection.equals("mixedSines")) tones();
	}
	
	public void squareTones() {
		channels = 1;
		sampleRate = 44100.0f;
		int bytesPerSamp = 2;
		int sampLength = byteLength/bytesPerSamp;
		for(int count = 0; count < sampLength; count ++) {
				double time = count / sampleRate;
				if(harmonics.equals("no")) {
					sinValue =
							(Math.sin(2 * Math.PI * freq * time));			
				}else {
					sinValue =
						(Math.sin(2 * Math.PI * freq * time) +
								Math.sin(2 * Math.PI * (freq / 1.5) * time) +
								Math.sin(2 * Math.PI * (freq / 2) * time) / 2 );
				} 
				shortBuffer.put((short) (100 * sinValue));
			}					
	}
	
	public void waWaPulse(){
	    channels = 1; //Java allows 1 or 2
	    int bytesPerSamp = 2; //Based on channels
	    sampleRate = 44100.0F;
	    int sampLength = byteLength/bytesPerSamp;
	    int cnt2 = -8000;
	    int cnt3 = -16000;
	    int cnt4 = -24000;
	    for(int cnt1 = 0; cnt1 < sampLength;cnt1++,cnt2++,cnt3++,cnt4++){
	      double val = waWaPulseHelper(cnt1,sampLength);
	      if(cnt2 > 0){
	        val += -0.7 * waWaPulseHelper(cnt2,sampLength);
	      }
	      
	      if(cnt3 > 0){
	        val += 0.49 * waWaPulseHelper(cnt3,sampLength);
	      }
	      
	      if(cnt4 > 0){
	        val += -0.34 * waWaPulseHelper(cnt4,sampLength);
	      }
	      shortBuffer.put((short)val);
	    }
	}
	
	public double waWaPulseHelper(int cnt,int sampLength){
		   //The value of scale controls the rate of
		   // decay - large scale, fast decay.
	      double scale = 2*cnt;
	      if(scale > sampLength) scale = sampLength;
	      double gain = 
	             100 * (sampLength-scale) / sampLength;
	      double time = cnt/sampleRate;
	      double thisFreq = freq * 2;
	      double sinValue =
	    		  (Math.sin(2 * Math.PI * thisFreq * time) +
	    				  Math.sin(2 * Math.PI * (thisFreq/1.8) * time) +
	    				  	Math.sin(2 * Math.PI * (thisFreq/1.5) * time)) / 3.0;
	    return(short)(gain*sinValue);
    }
	
	public void fmSweep() {
		channels = 1;
	    int bytesPerSamp = 2;
	    sampleRate = 441000.0F;
	    int sampLength = byteLength / bytesPerSamp;
	    double lowFreq = freq;
	    double highFreq = freq * 50;
	    for(int cnt = 0; cnt < sampLength; cnt++){
	      double time = cnt / sampleRate;
	      double freq = lowFreq +
	               cnt*(highFreq-lowFreq)/sampLength;
	      double sinValue =
	                   Math.sin(2* Math.PI * freq * time);
	      shortBuffer.put((short)(100 * sinValue));
	    }
	}
	
	public void decayPulse(){
	    channels = 1;//Java allows 1 or 2
	    int bytesPerSamp = 2;//Based on channels
	    sampleRate = 44100.0F;
	    // Allowable 8000,11025,16000,22050,44100
	    int sampLength = byteLength / bytesPerSamp;
	    for(int cnt = 0; cnt < sampLength; cnt++){
	      //The value of scale controls the rate of
	      // decay - large scale, fast decay.
	      double scale = 2*cnt;
	      if(scale > sampLength) scale = sampLength;
	      double thisFreq = freq * 2;
	      double gain = 
	             100 * (sampLength-scale) / sampLength;
	      double time = cnt / sampleRate;
	      double sinValue =
	        (Math.sin(2 * Math.PI * thisFreq * time) +
	        		Math.sin(2 * Math.PI * (thisFreq/1.8) * time) +
	        			Math.sin(2 * Math.PI * (thisFreq/1.5) * time)) /3.0;
	      shortBuffer.put((short)(gain*sinValue));
	    }
	 }
	
	public void tones(){
	    channels = 1;//Java allows 1 or 2
	    //Each channel requires two 8-bit bytes per
	    // 16-bit sample.
	    int bytesPerSamp = 2;
	    sampleRate = 44100.0F;
	    // Allowable 8000,11025,16000,22050,44100
	    int sampLength = byteLength / bytesPerSamp;
	    for(int cnt = 0; cnt < sampLength; cnt++){
	      double time = cnt/sampleRate;
	      double sinValue =
	        (Math.sin(2 * Math.PI * freq * time) +
	        		Math.sin(2 * Math.PI * (freq / 1.8) * time) +
	        			Math.sin(2 * Math.PI * (freq / 1.5) * time)) / 3.0;
	      shortBuffer.put((short)(100 * sinValue));
	    }
	}
}
