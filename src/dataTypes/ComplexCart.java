package dataTypes;

public class ComplexCart {

    private double real, imaginary;

    public ComplexCart(double re, double im) {
        this.real = re;
        this.imaginary = im;
    }

    public double Re() {
        return real;
    }

    public double Im() {
        return imaginary;
    }

    public void setRe(double re) {
        this.real = re;
    }

    public void setIm(double im) {
        this.imaginary = im;
    }

    public ComplexExp toExponential() {
        return new ComplexExp(modulus(),Math.atan2(imaginary,real));
    }

    public double modulus() {
        return Math.sqrt(real*real+imaginary*imaginary);
    }

    public void add(ComplexCart c) {
        real += c.Re();
        imaginary += c.Im();
    }

    public void sub(ComplexCart c) {
        real -= c.Re();
        imaginary -= c.Im();
    }



}
