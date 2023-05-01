#ifndef IOT_ICE
#define IOT_ICE

module IoT
{
    // DEVICE
    exception UnrecognisedSettingException { string reason; };  // thrown if wrong setting is passed to changeSettings() method

    dictionary<string, string> DeviceInfo;
    dictionary<string, string> Settings;

    // device has to be split to class IoTDevice and interface IoTDeviceOperation because interface can't contain fields
    class IoTDevice {
        string name;  // custom device name, can be changed bu user
        string brand;
        string model;
        string type;  // device's type (e.g. BeanCoffeeMaker)
    };
    interface IoTDeviceOperation {
        idempotent DeviceInfo getInfo();
        void changeName(string name);
        void changeSettings(Settings settings) throws UnrecognisedSettingException;
        void returnToFactorySettings();
    };

    // COFFEE MAKERS
    enum BeverageType {AMERICANO, CAPPUCCINO, ESPRESSO, CARAMELMACCHIATO, TEA, HOTCHOCOLATE, HOTMILK};
    sequence<BeverageType> AvailableBeverageTypes;
    exception UnsupportedBeverageTypeException { string reason; };  // requested beverage isn't available for selected coffee maker

    enum Ingredient {COFFEEBEANS, COFFEECAPSULES, CARAMELMACCHIATOCAPSULES, TEACAPSULES, CHOCOLATECAPSULES, MILK};
    dictionary<Ingredient, short> IngredientsQuantity;  // how much e.g. coffee beans there are in the coffee maker
    dictionary<BeverageType, IngredientsQuantity> RequiredIngredientsQuantity;  // how much e.g. coffee beans are needed to make coffee
    exception NotEnoughIngredientsException { string reason; };  // coffee maker doesn't have enough ingredients for selected beverage
    exception IllegalIngredientQuantityException { string reason; };  // set ingredient quantity is bigger than possible to store in coffee maker
    exception IllegalIngredientException { string reason; };  // coffee maker doesn't store selected ingredient

    struct Beverage {
        BeverageType beverageType;
        short volume;
    };

    class CoffeeMaker extends IoTDevice
    {
        AvailableBeverageTypes beverageTypes;
        IngredientsQuantity ingredientsQuantity;
        IngredientsQuantity maxIngredientsQuantity;
        RequiredIngredientsQuantity requiredIngredientsQuantity;
        short beveragesVolume;  // volume set by user
        short beveragesVolumeInitial;  // factory setting of volume
    };
    interface CoffeeMakerOperation extends IoTDeviceOperation {
        Beverage makeBeverage(BeverageType beverageType) throws UnsupportedBeverageTypeException, NotEnoughIngredientsException;
        void increaseIngredientQuantity(Ingredient ingredient, short quantity) throws IllegalIngredientException, IllegalIngredientQuantityException;
    };
};
// slice2java --output-dir generated slice/iot.ice
// generated/CoffeeMaker.java requires changing "protected" to "public" in methods _iceWriteImpl and _iceReadImpl
#endif