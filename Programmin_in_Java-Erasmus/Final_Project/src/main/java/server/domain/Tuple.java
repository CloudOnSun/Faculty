package main.java.server.domain;

public class Tuple<T, T1> {
    private T a;
    private T1 b;

    public Tuple(T a, T1 b) {
        this.a = a;
        this.b = b;
    }

    public T getFirst() {
        return a;
    }

    public void setFirst(T a) {
        this.a = a;
    }

    public T1 getSecond() {
        return b;
    }

    public void setSecond(T1 b) {
        this.b = b;
    }

    @Override
    public String toString() {
        return "Tuple{" +
                "a=" + a +
                ", b=" + b +
                '}';
    }
}