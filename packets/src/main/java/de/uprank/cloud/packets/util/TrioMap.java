package de.uprank.cloud.packets.util;

public class TrioMap<F, S, T> {

    private F first;
    private S second;
    private T third;

    public TrioMap(F first, S second, T third) {
        this.first = first;
        this.second = second;
        this.third = third;
    }

    public F getFirst() {
        return first;
    }

    public S getSecond() {
        return second;
    }

    public T getThird() {
        return third;
    }

    public void setFirst(F first) {
        this.first = first;
    }

    public void setSecond(S second) {
        this.second = second;
    }

    public void setThird(T third) {
        this.third = third;
    }
}
