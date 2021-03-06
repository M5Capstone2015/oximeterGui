package com.example.m5.oximetergui.NuJack;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Hunt on 1/28/2015.
 */
public class Decoder{

    int counter0 = 0;
    int counter1 = 0;
    int counter2 = 0;
    int startflag = 0;
    public List<Integer> bitlist = new ArrayList<Integer>();

    public void HandleBit(int transition)
    {
        updateAverage(transition);
        float freq = checkFreq(_movingAverage);

        if (freq == 0) {
            counter0 = counter0 + 1;
            counter1 = 0;
            counter2 = 0;
            if (10 == counter0) {
                if (startflag == 1) {
                    System.out.println("ZERO_10");
                    bitlist.add(0);
                }
            }
            if (30 == counter0) {
                counter0 = 10;
                if (startflag == 1) {
                    System.out.println("ZERO_30");
                    bitlist.add(0);
                }
            }
        }
        else if (freq == 1) {
            counter0 = 0;
            counter1 = counter1 + 1;
            counter2 = 0;
            if (counter1 == 15)
            {
                //bitlist.clear();
                System.out.println("TWO_30");
                counter1 = 0;
                startflag = 1;
            }
        }
        else if (freq == 2) {
            counter0 = 0;
            counter1 = 0;

            counter2 = counter2 + 1;
            if (8 == counter2) {
                if (startflag == 1) {
                    System.out.println("ONE_8");
                    bitlist.add(1);
                }
            }
            if (18 == counter2) {
                counter2 = 8;
                if (startflag == 1) {
                    System.out.println("ONE_18");
                    bitlist.add(1);
                }
            }
        }
        if (bitlist.size() == 9)
        {
            startflag = 0;

            _readCount++;
            //readData.add();

            /* ---------------------------------------- */
            _readings.clear();
            _foundBit = true;
            //readingData = convertBitsToString();
        }
    }

    boolean _foundBit = false;
    public boolean HandleData(List<Integer> data, StringBuilder res)
    {
        for (Integer i : data) {
            this.HandleBit(i);
            if (_foundBit) {
                res.append(nuByte.convertBits(bitlist));
                return true;
            }
        }
        //return ":(";
        //res.append("Your beat!");
        return false;
    }

    /*
    private String convertBitsToString() {
        String accum = "";
        for (Integer i : bitlist)
            accum += String.valueOf(i);
        return accum;
    }
    */


    private float checkFreq(float number)
    {
        if (number <= 4)
            number = 0;
        else if (4 < number && number <= 6)
            number = 1;
        else if (6 < number)
            number = 2;
        return number;
    }

    private List<String> _readings = new ArrayList<>();

    private int _readCount = 0;

    private int _count = 0;
    private int _sum = 0;
    private float _movingAverage = 0.0f;
    private List<Integer> dataStack = new ArrayList<Integer>();

    private void updateAverage(int newNum)
    {
        dataStack.add(newNum);

        if (dataStack.size() > 5)
        {
            int firstNum = dataStack.get(0); // Get first number in stack
            _sum -= firstNum; // Subtract it from sum
            dataStack.remove(0); // Remove first number from stack
        }
        _sum += newNum; // Add new tail to sum
        _movingAverage = (float) _sum / (float) 5; // Recalc average
    }
}