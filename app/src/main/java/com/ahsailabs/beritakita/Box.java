package com.ahsailabs.beritakita;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by ahmad s on 17/01/21.
 */
public class Box<T extends Number> {
    private T t;

    public void add(T t) {
        this.t = t;
    }

    public T get() {
        return t;
    }

    public static void main(String[] args) {
        Box<Integer> integerBox = new Box<Integer>();
        Box<Double> doubleBox = new Box<Double>();
        //Box<String> stringBox = new Box<String>(); //error, because of out of bounds

        integerBox.add(10);
        doubleBox.add(10d);
        //stringBox.add(new String("Hello World"));

        System.out.printf("Integer Value :%d\n\n", integerBox.get());
        System.out.printf("Double Value :%f\n\n", doubleBox.get());
        //System.out.printf("String Value :%s\n", stringBox.get());
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public <A> List<A> fromArrayToList(A[] a) {
        return Arrays.stream(a).collect(Collectors.toList());
    }
}
