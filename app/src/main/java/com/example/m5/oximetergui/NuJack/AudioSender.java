package com.example.m5.oximetergui.NuJack;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

/**
 * Created by Hunt on 2/11/2015.
 */
public class AudioSender {

    AudioTrack _track;

    public AudioSender()
    {
        _track = Initialize();
        _track.play();
    }

    public void Send()
    {
        short[] outputWave = new short[500];
        _track.write(outputWave, 0, outputWave.length);
    }

    private AudioTrack Initialize()
    {
        try {
            return new AudioTrack(AudioManager.STREAM_MUSIC, // TODO check if this works.
                    44100, AudioFormat.CHANNEL_OUT_STEREO,
                    AudioFormat.ENCODING_PCM_16BIT, 44100,
                    AudioTrack.MODE_STREAM);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return null;
        }
    }
}
