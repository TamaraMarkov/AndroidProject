package com.example.mathematics;

import android.util.Log;

import androidx.annotation.NonNull;

public class FractionNumber implements Comparable<FractionNumber> {
        private int integer;

    public int getInteger() {
        return integer;
    }

    public int getNumerator() {
        return numerator;
    }

    public int getDenominator() {
        return denominator;
    }

    private int numerator;
        private int denominator;

        public FractionNumber(int integer, int numerator, int denominator) {
            this.integer = integer;
            this.numerator = numerator;
            this.denominator = denominator;
        }
        @Override
        public int compareTo(FractionNumber o) {
            // https://www.geeksforgeeks.org/program-compare-two-fractions/
            int a = this.numerator + this.denominator * this.integer;
            int b = this.denominator;
            int c = o.numerator + o.denominator * o.integer;
            int d = o.denominator;

            // Compute ad-bc
            int Y = a * d - b * c;
            Log.d("Fraction", "fractions: " + this + "," + o +
                    "\nresult is: " + String.format("a:%d, b=%d, c=%d, d=%d, Y=%d", a, b, c, d, Y));
            return Integer.signum(Y);
        }

        @NonNull
        @Override
        public String toString() {
            return integer + " " + numerator + "/" + denominator;
        }
        public static FractionNumber parse(String s){
            String[] s1 = s.split(" ");
            int integer=Integer.parseInt(s1[0]);
            String[] s2 = s1[1].split("/");
            int nomirator= Integer.parseInt(s2[0]);
            int denominator= Integer.parseInt(s2[1]);
            return new FractionNumber(integer,nomirator,denominator);

        }


    }
