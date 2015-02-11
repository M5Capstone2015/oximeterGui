package com.example.m5.oximetergui.NuJack;

import java.util.List;

/**
 * Created by Hunt on 2/9/2015.
 */
public class NuJack {

    private AudioReceiver _audioReceiver;
    private Decoder _decoder;
    private OnDataAvailableListener _listener;
    boolean _running = false;
    Thread _inputThread;

    public NuJack(OnDataAvailableListener listener)
    {
        _audioReceiver = new AudioReceiver();
        _decoder = new Decoder();
        _listener = listener;
    }

    public void Start()
    {
        if (_listener == null)
            return;

        // Start reading
        _audioReceiver.startAudioIO();


        // Start input processor
        _running = true;
        _inputThread = new Thread(_inputProcessor);
        _inputThread.start();
    }

    Runnable _inputProcessor = new Runnable() {

        public void run()
        {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

            while (_running)
            {
                List<Integer> data = _audioReceiver.fakeAudioRead();
                List<Integer> result = _decoder.HandleData(data);

                if (result != null && result.size() == 8) {  // TODO clean this shit up.
                    _listener.DataAvailable(convertBitsToString(result));
                }
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
        _audioReceiver.stopAudioIO();

        try
        {
            _inputThread.join();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public void RegisterListener(OnDataAvailableListener listener)
    {
        _listener = listener;
    }
}
