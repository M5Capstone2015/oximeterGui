package com.example.m5.oximetergui.NuJack;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunt on 2/3/2015.
 */
public class AudioReceiver {

    public AudioReceiver(AudioRecord aru)
    {
        _audioRecord = aru;
    }

    public AudioReceiver()
    {
        _audioRecord = findAudioRecord();
    }

    ///////////////////////////////////////////////
    // Constants
    ///////////////////////////////////////////////

    // Most Android devices support 'CD-quality' sampling frequencies.
    final private int _sampleFrequency = 44100;

    // HiJack is powered at 21kHz
    private int _powerFrequency = 21000;

    // IO is FSK-modulated at either 613 or 1226 Hz (0 / 1)
    final private int _ioBaseFrequency = 613;

    AudioRecord _audioRecord;

    ///////////////////////////////////////////////
    // Input state
    ///////////////////////////////////////////////

    private short[] _recBuffer;

    private enum SearchState { ZERO_CROSS, NEGATIVE_PEAK, POSITIVE_PEAK };
    private SearchState _searchState = SearchState.ZERO_CROSS;

    // Part of a circular buffer to find the peak of each
    // signal.
    private int _toMean[] = new int[] {0, 0, 0};
    private int _toMeanPos = 0;

    // Part of a circular buffer to keep track of any
    // bias in the signal over the past 144 measurements
    private int _biasArray[] = new int[144];
    private boolean _biasArrayFull = false;
    private double _biasMean = 0.0;
    private int _biasArrayPos = 0;

    // Keeps track of the maximal value between two
    // zero-crossings to find the distance between
    // peaks
    private int _edgeDistance = 0;

    private List<Integer> _freqStack = new ArrayList<>();

    private void processInputBuffer(int shortsRead) {

        // We are basically trying to figure out where the edges are here,
        // in order to find the distance between them and pass that on to
        // the higher levels.
        double meanVal = 0.0;
        //System.out.println("shortsRead:  " + shortsRead);
        for (int i = 0; i < shortsRead; i++) {

            meanVal = addAndReturnMean(_recBuffer[i]) - addAndReturnBias(_recBuffer[i]);
            _edgeDistance++;

            // Cold boot we simply set the search based on
            // where the first region is located.
            if (_searchState == SearchState.ZERO_CROSS) {
                _searchState = meanVal < 0 ? SearchState.NEGATIVE_PEAK : SearchState.POSITIVE_PEAK;
            }

            // Have we just seen a zero transistion?
            if ((meanVal < 0 && _searchState == SearchState.POSITIVE_PEAK) ||
                    (meanVal > 0 && _searchState == SearchState.NEGATIVE_PEAK)) {

                //_sink.handleNextBit(_edgeDistance, _searchState == SearchState.POSITIVE_PEAK);
                _freqStack.add(_edgeDistance);
                _edgeDistance = 0;
                _searchState = (_searchState == SearchState.NEGATIVE_PEAK) ? SearchState.POSITIVE_PEAK : SearchState.NEGATIVE_PEAK;
            }
        }

        //System.out.println("func finished");
    }

    private double addAndReturnMean(int in) {
        _toMean[_toMeanPos++] = in;
        _toMeanPos = _toMeanPos % _toMean.length;

        double sum = 0.0;

        for (int i = 0; i < _toMean.length; i++) {
            sum += _toMean[i];
        }

        return sum / _toMean.length;
    }

    private double addAndReturnBias(int in) {
        if (_biasArrayFull) {
            _biasMean -= (double)_biasArray[_biasArrayPos] / (double)_biasArray.length;
        }

        _biasArray[_biasArrayPos++] = in;
        _biasMean += (double)in / (double)_biasArray.length;

        // If we're at the end of the bias array we move the
        // position back to 0 and recalculate the mean from scratch
        // keep small inaccuracies from influencing it.
        if (_biasArrayPos == _biasArray.length) {
            double totalSum = 0.0;
            for (int i = 0; i < _biasArray.length; i++) {
                totalSum += _biasArray[i];
            }
            _biasMean = totalSum / (double)_biasArray.length;
            _biasArrayPos = 0;
            _biasArrayFull = true;
        }

        return _biasMean;
    }

    public void startAudioIO()
    {
        attachAudioResources();
        _audioRecord.startRecording();
    }

    public List<Integer> fakeAudioRead(List<Short> data)  // Change this to return any freq coefficients.
    {
        _freqStack.clear();

        _recBuffer = new short[data.size() + 1];

        for (int i = 0; i < data.size(); i++)
            _recBuffer[i] = data.get(i);

        processInputBuffer(data.size());
        return _freqStack;
    }

    public List<Integer> Read(int samples)
    {
        _freqStack.clear();

        //for (int i = 0; i < samples; i++) { // todo pass amount of samples to take
            int shortsRead = _audioRecord.read(_recBuffer, 0, _recBuffer.length);
            processInputBuffer(shortsRead);
        //}
        return _freqStack;
    }

    private void attachAudioResources() {

        int bufferSize = getBufferSize();
        int recBufferSize = 5;

        //int recBufferSize =
        //AudioRecord.getMinBufferSize(_sampleFrequency, AudioFormat.CHANNEL_IN_MONO, AudioFormat.ENCODING_PCM_16BIT);

		/*
		_audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT,
		        _sampleFrequency, AudioFormat.CHANNEL_IN_MONO,
		        AudioFormat.ENCODING_PCM_16BIT, recBufferSize);
			*/
        _audioRecord = findAudioRecord(); // maybe change this to try defualt then iterate.

        _recBuffer = new short[recBufferSize * 10];
    }

    private void releaseAudioResources()
    {
        _audioRecord.release();
        _audioRecord = null;
        _recBuffer = null;
    }

    public void Stop()
    {
        releaseAudioResources();
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

    private int getBufferSize()
    {
        return _sampleFrequency / _ioBaseFrequency / 2;
    }
}
