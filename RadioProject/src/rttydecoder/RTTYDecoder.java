/*	This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>. */

package rttydecoder;

import java.io.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.AudioFileFormat;
import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.TargetDataLine;

public class RTTYDecoder implements Runnable {
	private final static int SAMPLERATE = 48000;
	private final static int BUFFERSIZE = SAMPLERATE;
	//private final static int FREQ0 = 915;
	//private final static int FREQ1 = 1085;
	private final static double BITSPERSEC = 45.45;

	private static enum RTTYMode { letters, symbols };
	private static RTTYMode mode = RTTYMode.letters;
    private static char[] RTTYLetters = ("<" + "E" + "\n" + "A" + " " + "S" + "I" + "U" + "\n" + "D" + "R" + "J" + "N" + "F" + "C" + "K" + "T" + "Z" + "L" + "W" + "H" + "Y" + "P" + "Q" + "O" + "B" + "G" + "^" + "M" + "X" + "V" + "^").toCharArray();
    private static char[] RTTYSymbols = ("<" + "3" + "\n" + "-" + " " + "," + "8" + "7" + "\n" + "$" + "4" + "#" + "," + "." + ":" + "(" + "5" + "+" + ")" + "2" + "." + "6" + "0" + "1" + "9" + "7" + "." + "^" + "." + "/" + "=" + "^").toCharArray();

	private TargetDataLine tdl;
	private double[] xvBP0, yvBP0, xvBP1, yvBP1, xvLP, yvLP;
	private int oneBitSampleCount;
	private int DPLLOldVal = 0;
	private int DPLLBitPhase = 0;
	public ByteArrayOutputStream baos;
	private static Clip clip;
	public static boolean clipIsDone = false;

	private String audioFilePath = "RadioProject//resources//wavs//rtty//rtty_test.wav";

	private String decodedText = "";


	public RTTYDecoder(TargetDataLine tdl) {
		this.tdl = tdl;

		oneBitSampleCount = (int)Math.round(SAMPLERATE/BITSPERSEC);
		System.out.println("One bit length: " + 1/BITSPERSEC + " seconds, " + oneBitSampleCount + " samples");

		xvBP0 = new double[5];
		yvBP0 = new double[5];
		xvBP1 = new double[5];
		yvBP1 = new double[5];
		xvLP = new double[3];
		yvLP = new double[3];
		
		baos = new ByteArrayOutputStream(); // this will store output sound data for debugging purposes 
	}

	// converts a double sample to 2 bytes
	private byte[] getBytesFromDouble(final double audioDataIn) {
		byte[] audioDataBytes = new byte[2];

		// saturation
		double audioData = Math.min(1.0, Math.max(-1.0, audioDataIn));

		// scaling and conversion to integer
		int sample = (int) Math.round((audioData + 1.0) * 32767.5) - 32768;

		byte high = (byte) ((sample >> 8) & 0xFF);
		byte low = (byte) (sample & 0xFF);
		audioDataBytes[0] = low;
		audioDataBytes[1] = high;

		return audioDataBytes;
	}

	// saves the audio data given in audioDataBytes to a .wav file
	public void writeWavFile(final byte[] audioDataBytes, final int storedSamples, final String fileName) {
		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, SAMPLERATE, 16, 1, 2, SAMPLERATE, false);
		AudioInputStream audioInputStream = new AudioInputStream(new ByteArrayInputStream(audioDataBytes), audioFormat, storedSamples);

		try {
			FileOutputStream fileOutputStream = new FileOutputStream(fileName);
			AudioSystem.write(audioInputStream, AudioFileFormat.Type.WAVE, fileOutputStream);
			audioInputStream.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// this function returns a sample from the sound device
	public double getSample() {
		int bytesRead;
		byte[] sample = new byte[2];
		double sampleDouble = 0;

		try {
			do {
				// waiting for the buffer to get filled
				while (tdl.available() < 2)
					Thread.sleep(0, 1); // without this, the audio will be choppy

				bytesRead = tdl.read(sample, 0, 2);
			} while (bytesRead < 2);

			// converting frame stored as bytes to double
			sampleDouble = ((sample[0] & 0xFF) | (sample[1] << 8)) / 32768.0;
		} catch (InterruptedException e) {
		}

		return sampleDouble;
	}

	// for filter designing, see http://www-users.cs.york.ac.uk/~fisher/mkfilter/
	// order 2 Butterworth, freqs: 865-965 Hz
	public double bandPassFreq0(double sampleIn) {
		xvBP0[0] = xvBP0[1]; xvBP0[1] = xvBP0[2]; xvBP0[2] = xvBP0[3]; xvBP0[3] = xvBP0[4]; 
	    xvBP0[4] = sampleIn / 2.356080041e+04;
	    yvBP0[0] = yvBP0[1]; yvBP0[1] = yvBP0[2]; yvBP0[2] = yvBP0[3]; yvBP0[3] = yvBP0[4]; 
	    yvBP0[4] = (xvBP0[0] + xvBP0[4]) - 2 * xvBP0[2]
	            	+ (-0.9816582826 * yvBP0[0]) + (3.9166274264 * yvBP0[1])
	                + (-5.8882201843 * yvBP0[2]) + (3.9530488323 * yvBP0[3]);
	    return yvBP0[4];
	}

	// order 2 Butterworth, freqs: 1035-1135 Hz
	public double bandPassFreq1(double sampleIn) {
		xvBP1[0] = xvBP1[1]; xvBP1[1] = xvBP1[2]; xvBP1[2] = xvBP1[3]; xvBP1[3] = xvBP1[4]; 
	    xvBP1[4] = sampleIn / 2.356080365e+04;
	    yvBP1[0] = yvBP1[1]; yvBP1[1] = yvBP1[2]; yvBP1[2] = yvBP1[3]; yvBP1[3] = yvBP1[4]; 
	    yvBP1[4] = (xvBP1[0] + xvBP1[4]) - 2 * xvBP1[2]
	            	+ (-0.9816582826 * yvBP1[0]) + (3.9051693660 * yvBP1[1])
	                + (-5.8653953990 * yvBP1[2]) + (3.9414842213 * yvBP1[3]);
	    return yvBP1[4];
	}

	// order 2 Butterworth, freq: 50 Hz
	public double lowPass(double sampleIn) {
		xvLP[0] = xvLP[1]; xvLP[1] = xvLP[2]; 
		xvLP[2] = sampleIn / 9.381008646e+04;
		yvLP[0] = yvLP[1]; yvLP[1] = yvLP[2]; 
		yvLP[2] = (xvLP[0] + xvLP[2]) + 2 * xvLP[1]
                   + (-0.9907866988 * yvLP[0]) + (1.9907440595 * yvLP[1]);
		return yvLP[2];
	}

	// this function returns the bit value of the current sample
	public int demodulator() {
		double sample = getSample();

		double line0 = bandPassFreq0(sample);
		double line1 = bandPassFreq1(sample);
		// calculating the RMS of the two lines (squaring them)
		line0 *= line0;
		line1 *= line1;

		// inverting line 1
		line1 *= -1;

		// summing the two lines
		line0 += line1;

		// lowpass filtering the summed line
		line0 = lowPass(line0);

		baos.write(getBytesFromDouble(line0), 0, 2); // writing to the output wav for debugging purposes

		if (line0 > 0)
			return 0;
		else
			return 1;
	}

	// this function returns at the half of a bit with the bit's value
	public int getBitDPLL() {
		boolean phaseChanged = false;
		int val = 0;

		while (DPLLBitPhase < oneBitSampleCount) {
			val = demodulator();

			if (!phaseChanged && val != DPLLOldVal) {
				if (DPLLBitPhase < oneBitSampleCount/2)
					DPLLBitPhase += oneBitSampleCount/8; // early
				else
					DPLLBitPhase -= oneBitSampleCount/8; // late
				phaseChanged = true;
			}
			DPLLOldVal = val;
			DPLLBitPhase++;
		}
		DPLLBitPhase -= oneBitSampleCount;

		// putting a tick to the output wav signing the moment when the DPLL returned
		baos.write(100);
		baos.write(100);
		baos.write(100);
		baos.write(100);
		baos.write(100);
		baos.write(100);
		return val;
	}

	// this function returns only when the start bit is successfully received
	public void waitForStartBit() {
		int bitResult;

		while (!Thread.interrupted()) {
			// waiting for a falling edge
			do {
				bitResult = demodulator();
			} while (bitResult == 0 && !Thread.interrupted());
			
			do {
				bitResult = demodulator();
			} while (bitResult == 1 && !Thread.interrupted());

			// waiting half bit time
			for (int i = 0; i < oneBitSampleCount/2; i++)
				bitResult = demodulator();

			if (bitResult == 0)
				break;
		}
	}
	
	@Override
	public void run() {
		tdl.start();
		decodedText = "";

		int byteResult = 0;
		int byteResultp = 0;
		int bitResult;
		
		while (!Thread.interrupted() && !clipIsDone) {
			waitForStartBit();

			System.out.print("0 "); // first bit is the start bit, it's zero

			// reading 7 more bits
			for (byteResultp = 1, byteResult = 0; byteResultp < 8; byteResultp++) {
				bitResult = getBitDPLL();

				switch (byteResultp) {
					case 6: // stop bit 1
						System.out.print(" " + bitResult);
						break;
					case 7: // stop bit 2
						System.out.print(bitResult);
						break;
					default:
						System.out.print(bitResult);
						byteResult += bitResult << (byteResultp-1);
				}
			}

			switch (byteResult) {
				case 31:
					mode = RTTYMode.letters;
					System.out.println(" ^L^");
					addToDecodedTest(" ^L^");
					break;
				case 27:
					mode = RTTYMode.symbols;
					System.out.println(" ^F^");
					addToDecodedTest(" ^F^");
					break;
				default:
					switch (mode) {
						case letters:
							System.out.println(" *** " + RTTYLetters[byteResult] + "(" + byteResult + ")");
							addToDecodedTest(RTTYLetters[byteResult]);
							break;
						case symbols:
							System.out.println(" *** " + RTTYSymbols[byteResult] + "(" + byteResult + ")");
							addToDecodedTest(RTTYSymbols[byteResult]);
							break;
					}
			}
		}

		tdl.stop();
		tdl.close();
	}

	public void addToDecodedTest(String text)
	{
		if(!clipIsDone)
		{
			decodedText += " " + text;
		}
	}

	public void addToDecodedTest(char letter)
	{
		if(!clipIsDone)
		{
			decodedText += " " + letter;
		}
	}

	public String getDecodedText()
	{
		return decodedText;
	}

	public static void javaAudioPlaySoundExample(String audioFilePath) throws Exception
	{
		File soundFile = new File(audioFilePath);
		Line.Info linfo = new Line.Info(Clip.class);
		Line line = AudioSystem.getLine(linfo);
		clip = (Clip) line;
		clip.addLineListener(new LineListener() {

			public void update(LineEvent le) {
				LineEvent.Type type = le.getType();
				if (type == LineEvent.Type.OPEN) {
					System.out.println("OPEN");
				} else if (type == LineEvent.Type.CLOSE) {
					System.out.println("CLOSE");
				} else if (type == LineEvent.Type.START) {
					clipIsDone = false;
					System.out.println("START");
				} else if (type == LineEvent.Type.STOP) {
					System.out.println("STOP");
					clipIsDone = true;
					clip.close();
				}
			}

		});

		AudioInputStream ais = AudioSystem.getAudioInputStream(soundFile);
		clip.open(ais);
		clip.start();
	}
}
