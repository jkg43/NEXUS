package dataTypes;

public class ComplexExp {

    private double modulus, angle;

    public ComplexExp(double modulus, double angle) {
        this.modulus = modulus;
        this.angle = angle;
    }

    public double modulus() {
        return modulus;
    }

    public double angle() {
        return angle;
    }

    public void setModulus(double modulus) {
        this.modulus = modulus;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    public ComplexCart toCartesian() {
        return new ComplexCart(modulus * Math.cos(angle), modulus * Math.sin(angle));
    }

    public void mult(ComplexExp c) {
        modulus *= c.modulus();
        angle += c.angle();
    }

    public void div(ComplexExp c) {
        modulus /= c.modulus();
        angle -= c.angle();
    }


}
