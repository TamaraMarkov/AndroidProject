package com.example.mathematics;

public class Fraction{
    private int sliceCount;
    private int maxSliceCount;
    public Fraction(int sliceCount,int maxSliceCount){
        this.sliceCount=sliceCount;
        this.maxSliceCount=maxSliceCount;
    }

    public int getMaxSliceCount() {
        return maxSliceCount;
    }

    public void setSliceCount(int sliceCount) {
        this.sliceCount = sliceCount;
    }

    public int getSliceCount() {
        return sliceCount;
    }
}
