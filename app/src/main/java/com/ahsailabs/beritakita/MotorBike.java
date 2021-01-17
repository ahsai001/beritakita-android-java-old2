package com.ahsailabs.beritakita;

/**
 * Created by ahmad s on 17/01/21.
 */
class MotorBike extends Vehicle {
    @Override
    public void honk() {
        System.out.println("Tin tin!");
    }

    public static void main(String[] args) {
        // Create a MotorBike object
        MotorBike myBike = new MotorBike();

        myBike.honk();
        //Tin tin

        Vehicle myVehicle = myBike;
        myVehicle.honk();
        //Tin tin
    }
}
