package com.ahsailabs.beritakita;

/**
 * Created by ahmad s on 17/01/21.
 */

class Cat implements AnimalInterface {
    @Override
    public void animalSound() {
        // The body of animalSound() is provided here
        System.out.println("The cat says: meong meong");
    }

    @Override
    public void sleep() {
        // The body of sleep() is provided here
        System.out.println("Zzz");
    }
}
