package sr.ice.server.devices.coffeemakers;

import IoT.*;
import com.zeroc.Ice.Current;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class BeanCoffeeMaker extends CoffeeMaker implements CoffeeMakerOperation {
    private String name;
    private final String brand;
    private final String model;
    private final String type;
    private short volume;  // ml
    private final List<BeverageType> beverageTypes;

    private final ReentrantLock lock;

    public BeanCoffeeMaker(String name, String brand, String model) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.type = "BeanCoffeeMaker";
        this.volume = 150;
        this.beverageTypes = Arrays.asList(
                BeverageType.AMERICANO,
                BeverageType.ESPRESSO);
        this.lock = new ReentrantLock();
    }

    public BeanCoffeeMaker(String name) {
        this.name = name;
        this.brand = "brand";
        this.model = "model";
        this.type = "BeanCoffeeMaker";
        this.volume = 150;
        this.beverageTypes = Arrays.asList(
                BeverageType.AMERICANO,
                BeverageType.ESPRESSO);
        this.lock = new ReentrantLock();
    }

    @Override
    public Beverage makeBeverage(BeverageType beverageType, Current current) throws UnsupportedBeverageTypeException {
        lock.lock();
        if (! beverageTypes.contains(beverageType)) {
            lock.unlock();
            throw new UnsupportedBeverageTypeException(type + " cannot produce " + beverageType.name());
        }
        Beverage beverage = new Beverage(beverageType, volume);
        lock.unlock();
        return beverage;
    }

    @Override
    public Map<String, String> getInfo(Current current) {
        this.lock.lock();
        Map<String, String> data = CoffeeMakerUtils.getInfo(this);
        this.lock.unlock();
        return data;
    }

    @Override
    public void changeSettings(Map<String, String> settings, Current current) throws UnknownSettingException {
        this.lock.lock();
        CoffeeMakerUtils.changeSettings(this, settings);
        this.lock.unlock();
    }
}
