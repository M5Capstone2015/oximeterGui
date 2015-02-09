package com.example.m5.oximetergui.NuJack;

/**
 * Created by Hunt on 2/9/2015.
 */
public class NuJack {

    private AudioReceiver _audioReceiver;
    private Decoder _decoder;
    private OnDataAvailableListener _listener;

    public void Start()
    {
        //if (byteAvailable == null)
        //  return;

        //_audioReceiver.start();

        // Put repeating thread here
        //List<int> data = _audioReceiver.readData(3);
        //_decoder.ReadData(data);
    }

    public void Stop()
    {
        //_audioReceiver.Stop();
    }

    public void RegisterListener(OnDataAvailableListener listener)
    {
        //_decoder.RegisterListener(listener);
    }
}
