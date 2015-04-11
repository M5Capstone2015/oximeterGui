package com.example.m5.oximetergui.NuJack;

import java.util.List;

public class nuByte
{
    private int _value = 0;
    private int _count = 0;

    public float GetValue()
    {
        return  ((float) _value) / 10;
    }

    static public String convertBits(List<Integer> bits)
    {
        int _count = 0;
        int _value = 0;
        //for (Integer i : bits)
        for (int i = 1; i < 9; i++)
        {
            int val = bits.get(i);
            if (_count > 0) // We don't need to shift to set first bit
                _value <<= 1;

            _value += val;
            _count++;
        }
        //float res = ((float) _value + 750 ) / 10;
        //float res = ((float) _value) + 70;
        int res =  _value + 70;
        String s = "";
        s += res;
        return s;
        //return String.format("%.1f", res);
    }

    public float intVal()
    {
        return _value;
    }

    public boolean push(int bit)
    {
        if (bit != 0 && bit != 1)
        {
            // log error
            return false;
        }

        if (_count > 0) // We don't need to shift to set first bit
            _value <<= 1;

        _value += bit;
        _count++;
        return true;
    }

    public boolean IsByteComplete()
    {
        if (_count >= 9)
            return true;
        else
            return false;
    }

    public void Reset()
    {
        _value = 0;
        _count = 0;
    }
}
