#ifndef IOT_ICE
#define IOT_ICE

module IoT
{
    exception ValueOutOfRangeException { string reason; };
    exception UnknownSettingException { string reason; };
    exception UnsupportedBeverageTypeException { string reason; };

    dictionary<string, string> DeviceInfo;
    dictionary<string, string> Settings;

    class IoTDevice {
        string name;  // customowa nazwa urządzenia
        string brand;  // marka
        string model;  // model
        string type;  // typ urządzenia (np. coffeemaker)
    };
    interface IoTDeviceOperation {
        idempotent DeviceInfo getInfo();
        idempotent void changeSettings(Settings settings) throws UnknownSettingException, ValueOutOfRangeException;
    };

    // ekspresy do kawy
    enum BeverageType {AMERICANO, CAPPUCCINO, ESPRESSO, CARAMELMACCHIATO, TEA, HOTCHOCOLATE, HOTMILK};
    sequence<BeverageType> AvailableBeverageTypes;

    class BeverageOrder {
        BeverageType beverageType;
        short volume;
    };
    sequence<BeverageOrder> BeverageOrders;

    class Beverage {
        BeverageType beverageType;
        short volume;
    };
    sequence<Beverage> Coffees;

    class CoffeeMaker extends IoTDevice
    {
        short volume;
        AvailableBeverageTypes beverageTypes;
    };
    interface CoffeeMakerOperation extends IoTDeviceOperation {
        Beverage makeBeverage(BeverageType beverageType) throws UnsupportedBeverageTypeException;
    };
};
// slice2java --output-dir generated slice/iot.ice
// generated/CoffeeMaker.java requires changing "protected" to "public" in methods _iceWriteImpl and _iceReadImpl
#endif