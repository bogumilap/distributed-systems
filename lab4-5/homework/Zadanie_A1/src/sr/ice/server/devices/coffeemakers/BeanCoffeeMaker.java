package sr.ice.server.devices.coffeemakers;

import IoT.*;
import com.zeroc.Ice.Current;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class BeanCoffeeMaker extends CoffeeMaker implements CoffeeMakerOperation {
    private String name;
    private final String brand;
    private final String model;
    private final String type;
    private final short beveragesVolumeInitial;  // ml
    private short beveragesVolume;  // ml
    private final List<BeverageType> beverageTypes;
    private final Map<Ingredient, Short> ingredientsQuantity;
    private final Map<Ingredient, Short> maxIngredientsQuantity;
    private final Map<BeverageType, Map<Ingredient, Short>> requiredIngredientsQuantity;

    private final ReentrantLock lock;

    public BeanCoffeeMaker(String name, String brand, String model) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.type = "BeanCoffeeMaker";
        this.beveragesVolumeInitial = 150;
        this.beveragesVolume = this.beveragesVolumeInitial;
        this.beverageTypes = Arrays.asList(
                BeverageType.AMERICANO,
                BeverageType.ESPRESSO);

        this.ingredientsQuantity = new HashMap<>();
        this.ingredientsQuantity.put(Ingredient.COFFEEBEANS, (short) 500);

        this.maxIngredientsQuantity = new HashMap<>();
        this.maxIngredientsQuantity.put(Ingredient.COFFEEBEANS, (short) 500);

        this.requiredIngredientsQuantity = new HashMap<>();
        this.requiredIngredientsQuantity.put(BeverageType.AMERICANO, getMapForIngredient(Ingredient.COFFEEBEANS, (short) 7));
        this.requiredIngredientsQuantity.put(BeverageType.ESPRESSO, getMapForIngredient(Ingredient.COFFEEBEANS, (short) 7));

        this.lock = new ReentrantLock();
    }

    private Map<Ingredient, Short> getMapForIngredient(Ingredient ingredient, Short units) {
        Map<Ingredient, Short> map = new HashMap<>();
        map.put(ingredient, units);
        return map;
    }

    @Override
    public Beverage makeBeverage(BeverageType beverageType, Current current) throws UnsupportedBeverageTypeException, NotEnoughIngredientsException {
        lock.lock();
        if (! beverageTypes.contains(beverageType)) {
            throw new UnsupportedBeverageTypeException(type + " cannot produce " + beverageType.name());
        }
        Map<Ingredient, Short> requiredIngredients = requiredIngredientsQuantity.get(beverageType);
        for (Map.Entry<Ingredient, Short> requiredIngredient : requiredIngredients.entrySet()) {
            String ingredientName = requiredIngredient.getKey().name();
            short requiredIngredientQuantity = requiredIngredient.getValue();
            short availableIngredientQuantity = ingredientsQuantity.get(requiredIngredient.getKey());
            if (requiredIngredientQuantity > availableIngredientQuantity) {
                throw new NotEnoughIngredientsException(beverageType.name() + " requires " + requiredIngredientQuantity
                        + " units of " + ingredientName + "; only " + availableIngredientQuantity + " units are available");
            }
        }
        Beverage beverage = new Beverage(beverageType, beveragesVolume);
        for (Map.Entry<Ingredient, Short> requiredIngredient : requiredIngredients.entrySet()) {
            short requiredIngredientQuantity = requiredIngredient.getValue();
            short availableIngredientQuantity = ingredientsQuantity.get(requiredIngredient.getKey());
            ingredientsQuantity.put(requiredIngredient.getKey(), (short) (availableIngredientQuantity - requiredIngredientQuantity));
        }
        lock.unlock();
        return beverage;
    }

    @Override
    public void increaseIngredientQuantity(Ingredient ingredient, short quantity, Current current) throws IllegalIngredientException, IllegalIngredientQuantityException {
        lock.lock();
        if (! maxIngredientsQuantity.containsKey(ingredient)) {
            throw new IllegalIngredientException(type + " cannot store " + ingredient.name());
        }
        if (quantity > maxIngredientsQuantity.get(ingredient)) {
            throw new IllegalIngredientQuantityException("Selected too big quantity (" + quantity + ") of " + ingredient.name() + "; "
                    + type + " can store up to " + maxIngredientsQuantity.get(ingredient) + " units of it");
        }
        ingredientsQuantity.put(ingredient, quantity);
        lock.unlock();
    }

    @Override
    public Map<String, String> getInfo(Current current) {
        lock.lock();
        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("brand", brand);
        data.put("model", model);
        data.put("type", type);
        data.put("beveragesVolume", String.valueOf(beveragesVolume));
        data.put("beverageTypes", String.valueOf(beverageTypes));
        data.put("ingredientsQuantity", String.valueOf(ingredientsQuantity));
        lock.unlock();
        return data;
    }

    @Override
    public void changeName(String name, Current current) {
        lock.lock();
        this.name = name;
        lock.unlock();
    }

    @Override
    public void changeSettings(Map<String, String> settings, Current current) throws UnrecognisedSettingException {
        lock.lock();
        for (Map.Entry<String, String> entry : settings.entrySet()) {
            switch (entry.getKey()) {
                case "beveragesVolume" -> beveragesVolume = Short.parseShort(entry.getValue());
                default ->
                        throw new UnrecognisedSettingException("Unrecognised setting " + entry.getKey() + " for " + type);
            }
        }
        lock.unlock();
    }

    @Override
    public void returnToFactorySettings(Current current) {
        lock.lock();
        beveragesVolume = beveragesVolumeInitial;
        lock.unlock();
    }
}
