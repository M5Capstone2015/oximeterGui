package com.example.m5.oximetergui.NuJack;

import java.util.List;

/**
 * Created by Hunt on 2/9/2015.
 */
public class NuJack {

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
        _outputThread.start();
    }

    Runnable _inputProcessor = new Runnable() {

        public void run()
        {
            Thread.currentThread().setPriority(Thread.MIN_PRIORITY);

            while (_running)
            {
                List<Integer> data = _audioReceiver.Read(1);
                List<Integer> result = _decoder.HandleData(data);

                if (result != null && result.size() == 8) {  // TODO refactor this to Integer obj
                    _listener.DataAvailable(convertBitsToString(result));
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

    public void RegisterListener(OnDataAvailableListener listener)
    {
        _listener = listener;
    }
}
