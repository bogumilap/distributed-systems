package sr.ice.server.servants.coffeemakers;

import IoT.*;
import com.zeroc.Ice.Current;
import sr.ice.server.devices.coffeemakers.BeanCoffeeMaker;

import java.util.Map;

public class BeanCoffeMakerServant implements CoffeeMakerOperation {
    private String name;
    private final String brand;
    private final String model;
    private static BeanCoffeeMaker instance;

    public BeanCoffeMakerServant(String name, String brand, String model) {
        this.name = name;
        this.brand = brand;
        this.model = model;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    private void createInstance() {
        if (instance == null) {
            System.out.println("Creating instance...");
            instance = new BeanCoffeeMaker(this.name, this.brand, this.model);
        }
    }

    @Override
    public Beverage makeBeverage(BeverageType beverageType, Current current) throws UnsupportedBeverageTypeException, NotEnoughIngredientsException {
        createInstance();
        return instance.makeBeverage(beverageType, current);
    }

    @Override
    public void increaseIngredientQuantity(Ingredient ingredient, short quantity, Current current) throws IllegalIngredientException, IllegalIngredientQuantityException {
        createInstance();
        instance.increaseIngredientQuantity(ingredient, quantity, current);
    }

    @Override
    public Map<String, String> getInfo(Current current) {
        createInstance();
        System.out.println(instance.getInfo(current));
        return instance.getInfo(current);
    }

    @Override
    public void changeName(String name, Current current) {
        createInstance();
        instance.changeName(name, current);
    }

    @Override
    public void changeSettings(Map<String, String> settings, Current current) throws UnrecognisedSettingException {
        createInstance();
        instance.changeSettings(settings, current);
    }

    @Override
    public void returnToFactorySettings(Current current) {
        createInstance();
        instance.returnToFactorySettings(current);
    }
}
