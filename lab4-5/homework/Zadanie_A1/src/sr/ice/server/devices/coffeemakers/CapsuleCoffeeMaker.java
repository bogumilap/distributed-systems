package sr.ice.server.devices.coffeemakers;

import IoT.*;
import com.zeroc.Ice.Current;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class CapsuleCoffeeMaker extends CoffeeMaker implements CoffeeMakerOperation {
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

    public CapsuleCoffeeMaker(String name, String brand, String model) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.type = "CapsuleCoffeeMaker";
        this.beveragesVolumeInitial = 200;
        this.beveragesVolume = this.beveragesVolumeInitial;
        this.beverageTypes = Arrays.asList(
                BeverageType.AMERICANO,
                BeverageType.CAPPUCCINO,
                BeverageType.ESPRESSO,
                BeverageType.CARAMELMACCHIATO,
                BeverageType.TEA,
                BeverageType.HOTCHOCOLATE,
                BeverageType.HOTMILK);

        this.ingredientsQuantity = new HashMap<>();
        this.ingredientsQuantity.put(Ingredient.COFFEECAPSULES, (short) 15);
        this.ingredientsQuantity.put(Ingredient.CARAMELMACCHIATOCAPSULES, (short) 10);
        this.ingredientsQuantity.put(Ingredient.TEACAPSULES, (short) 15);
        this.ingredientsQuantity.put(Ingredient.CHOCOLATECAPSULES, (short) 5);
        this.ingredientsQuantity.put(Ingredient.MILK, (short) 500);

        this.maxIngredientsQuantity = new HashMap<>();
        this.maxIngredientsQuantity.put(Ingredient.COFFEECAPSULES, (short) 15);
        this.maxIngredientsQuantity.put(Ingredient.CARAMELMACCHIATOCAPSULES, (short) 15);
        this.maxIngredientsQuantity.put(Ingredient.TEACAPSULES, (short) 15);
        this.maxIngredientsQuantity.put(Ingredient.CHOCOLATECAPSULES, (short) 15);
        this.maxIngredientsQuantity.put(Ingredient.MILK, (short) 500);

        this.requiredIngredientsQuantity = new HashMap<>();
        this.requiredIngredientsQuantity.put(BeverageType.AMERICANO,
                getMapOfIngredients(new Ingredient[]{Ingredient.COFFEECAPSULES}, new short[]{1}));
        this.requiredIngredientsQuantity.put(BeverageType.CAPPUCCINO,
                getMapOfIngredients(new Ingredient[]{Ingredient.COFFEECAPSULES, Ingredient.MILK}, new short[]{1, 100}));
        this.requiredIngredientsQuantity.put(BeverageType.ESPRESSO,
                getMapOfIngredients(new Ingredient[]{Ingredient.COFFEECAPSULES}, new short[]{1}));
        this.requiredIngredientsQuantity.put(BeverageType.CARAMELMACCHIATO,
                getMapOfIngredients(new Ingredient[]{Ingredient.CARAMELMACCHIATOCAPSULES}, new short[]{1}));
        this.requiredIngredientsQuantity.put(BeverageType.TEA,
                getMapOfIngredients(new Ingredient[]{Ingredient.TEACAPSULES}, new short[]{1}));
        this.requiredIngredientsQuantity.put(BeverageType.HOTCHOCOLATE,
                getMapOfIngredients(new Ingredient[]{Ingredient.CHOCOLATECAPSULES}, new short[]{1}));
        this.requiredIngredientsQuantity.put(BeverageType.HOTMILK,
                getMapOfIngredients(new Ingredient[]{Ingredient.MILK}, new short[]{150}));

        this.lock = new ReentrantLock();
    }

    private Map<Ingredient, Short> getMapOfIngredients(Ingredient[] ingredients, short[] units) {
        Map<Ingredient, Short> map = new HashMap<>();
        for (int i=0; i<ingredients.length; i++) {
            map.put(ingredients[i], units[i]);
        }
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
    public void changeSettings(Map<String, Short> settings, Current current) throws UnrecognisedSettingException {
        lock.lock();

        for (Map.Entry<String, Short> entry : settings.entrySet()) {
            if ("beveragesVolume".equals(entry.getKey())) {
                beveragesVolume = entry.getValue();
            } else {
                lock.unlock();
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
