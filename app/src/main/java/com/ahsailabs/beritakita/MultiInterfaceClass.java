package com.ahsailabs.beritakita;

/**
 * Created by ahmad s on 17/01/21.
 */
class MultiInterfaceClass implements FirstInterface, SecondInterface {
    @Override
    public void myMethod() {
        System.out.println("Some text..");
    }

    @Override
    public void myOtherMethod() {
        System.out.println("Some other text...");
    }
}
