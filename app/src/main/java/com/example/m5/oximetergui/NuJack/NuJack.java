package com.example.m5.oximetergui.NuJack;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunt on 2/9/2015.
 */
public class NuJack {

    private AudioRecord _aru;

    private AudioReceiver _audioReceiver;
    private AudioSender _audioSender;
    private Decoder _decoder;

    private OnDataAvailableListener _listener;
    private boolean _running = false;
    private Thread _inputThread;
    private Thread _outputThread;

    public NuJack(OnDataAvailableListener listener)
    {
        _audioReceiver = new AudioReceiver();
        _audioSender = new AudioSender();
        _decoder = new Decoder();
        _listener = listener;
        _aru = findAudioRecord();
        int recBufferSize =
                AudioRecord.getMinBufferSize(44100, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);
        buffer = new short[recBufferSize * 10];
    }

    public void Start()
    {
        if (_listener == null)
            return;

        // Start reading
        _audioReceiver.startAudioIO();

        _running = true;

        // Start input processor
        _inputThread = new Thread(_inputProcessor);
        _inputThread.start();

        // Start power source
        _outputThread = new Thread(_outputProcessor);
        //_outputThread.start();
    }

    short[] buffer = null;


    Runnable _inputProcessor = new Runnable() {

        public void run()
        {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

            while (_running)
            {
                try
                {
                    _aru.startRecording();
                    int shortsRead = _aru.read(buffer, 0, buffer.length);

                    List<Short> rawData = new ArrayList<>();
                    for (int i = 0; i < shortsRead; i++) { // Copy data from buffer into stack.
                        rawData.add(buffer[i]);
                    }

                    List<Integer> freqs = _audioReceiver.fakeAudioRead(rawData);

                    Decoder dec = new Decoder();

                    //List<Integer> data = _audioReceiver.Read(1);
                    StringBuilder sb = new StringBuilder();
                    //boolean result = _decoder.HandleData(freqs, sb);
                    boolean result = dec.HandleData(freqs, sb);

                    if (result)
                        _listener.DataAvailable(sb.toString());

                    //Thread.sleep(300); // Take a reading every 0.3 seconds.
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        }

    };

    Runnable _outputProcessor = new Runnable() {
        public void run()
        {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

            while (_running)
            {
                if (_audioSender != null)
                    _audioSender.Send();
            }
        }
    };

    private String convertBitsToString(List<Integer> bitlist)
    {
        String accum = "";
        for (Integer i : bitlist)
            accum += String.valueOf(i);
        return accum;
    }

    /**
     * Stop reading and processing data.
     */
    public void Stop()
    {
        _running = false;
        _audioReceiver.Stop();

        try
        {
            _inputThread.join();
            _outputThread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private static int[] mSampleRates = new int[] {44100, 22050, 11025, 8000};
    private AudioRecord findAudioRecord()
    {
        for (int rate : mSampleRates)
        {
            for (short audioFormat : new short[] { AudioFormat.ENCODING_PCM_8BIT, AudioFormat.ENCODING_PCM_16BIT })
            {
                for (short channelConfig : new short[] { AudioFormat.CHANNEL_IN_MONO, AudioFormat.CHANNEL_IN_STEREO })
                {
                    try {
                        //Log.d(C.TAG, "Attempting rate " + rate + "Hz, bits: " + audioFormat + ", channel: "
                        //+ channelConfig);
                        int bufferSize = AudioRecord.getMinBufferSize(rate, channelConfig, audioFormat);

                        if (bufferSize != AudioRecord.ERROR_BAD_VALUE) {
                            // check if we can instantiate and have a success
                            AudioRecord recorder = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, rate, channelConfig, audioFormat, bufferSize);

                            if (recorder.getState() == AudioRecord.STATE_INITIALIZED)
                                return recorder;
                        }
                    } catch (Exception e) {
                        //Log.e(C.TAG, rate + "Exception, keep trying.",e);
                    }
                }
            }
        }
        return null;
    }

    public void RegisterListener(OnDataAvailableListener listener)
    {
        _listener = listener;
    }
}
