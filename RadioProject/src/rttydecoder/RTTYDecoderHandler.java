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
import javax.sound.sampled.*;


public class RTTYDecoderHandler {
	private final static int SAMPLERATE = 48000;
	private final static int BUFFERSIZE = SAMPLERATE;

	private String audioFilePath = "RadioProject//resources//wavs//rtty//rtty_test.wav";

	public RTTYDecoder decodeRttyWavFile(String audioPath) {
		AudioFormat audioFormat = new AudioFormat(AudioFormat.Encoding.PCM_SIGNED, SAMPLERATE, 16, 1, 2, SAMPLERATE, false);
		DataLine.Info info = new DataLine.Info(TargetDataLine.class, audioFormat, BUFFERSIZE);

		if(audioPath != null)
			audioFilePath = audioPath;

		TargetDataLine targetDataLine = null;
		try {
			targetDataLine = (TargetDataLine) AudioSystem.getLine(info);
			targetDataLine.open(audioFormat, BUFFERSIZE);
			System.out.println("Buffer size: " + targetDataLine.getBufferSize());
		} catch (LineUnavailableException e1) {
			e1.printStackTrace();
		}

		// creating the recorder thread from this class' instance
		RTTYDecoder rttyDecoder = new RTTYDecoder(targetDataLine);
		Thread rttyDecoderThread = new Thread(rttyDecoder);

		// we use this to read line from the standard input
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		rttyDecoderThread.setPriority(Thread.MAX_PRIORITY);
		rttyDecoderThread.start();

		try {
			rttyDecoder.javaAudioPlaySoundExample(audioFilePath);
		}         catch (Exception ex)
		{
			System.out.println(ex.getMessage());
		}
		System.out.println("Recording...");

/*
		while(!rttyDecoder.clipIsDone) {
			try {
				br.readLine();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
*/

		System.out.println("Stopping...");
		rttyDecoder.writeWavFile(rttyDecoder.baos.toByteArray(), rttyDecoder.baos.size() / 2, "output.wav");

		rttyDecoderThread.interrupt();

		try {
			// waiting for the recorder thread to stop
			rttyDecoderThread.join();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Recording stopped.");
		return rttyDecoder;
	}
}
