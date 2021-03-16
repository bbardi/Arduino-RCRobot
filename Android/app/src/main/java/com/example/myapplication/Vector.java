package com.example.myapplication;

public class Vector {
    public double x;
    public double y;

    public Vector(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public Vector() {
        x = 0.0f;
        y = 0.0f;
    }

    public void normalize(){
        double length = Math.sqrt(x*x + y*y);
        x /= length;
        y /= length;
    }
    public double length(){
        return Math.sqrt(x*x + y*y);
    }
    public double angle(Vector v2){
        return Math.acos((this.x*v2.x + this.y*v2.y)/(v2.length()*this.length()));
    }
    public double Cross(Vector v2){
        return (this.x*v2.y)-(this.y*v2.x);
    }
    @Override
    public String toString() {
        return "Vector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
