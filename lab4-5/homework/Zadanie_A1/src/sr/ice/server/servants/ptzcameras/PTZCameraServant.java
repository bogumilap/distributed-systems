package sr.ice.server.servants.ptzcameras;

import IoT.IllegalSettingValueException;
import IoT.CameraOperation;
import IoT.UnrecognisedSettingException;
import com.zeroc.Ice.Current;
import sr.ice.server.devices.coffeemakers.CapsuleCoffeeMaker;
import sr.ice.server.devices.ptzcameras.PTZCamera;

import java.util.Map;

public class PTZCameraServant implements CameraOperation {
    private String name;
    private final String brand;
    private final String model;
    private static PTZCamera instance;

    public PTZCameraServant(String name, String brand, String model) {
        this.name = name;
        this.brand = brand;
        this.model = model;
    }

    private void createInstance() {
        if (instance == null) {
            instance = new PTZCamera(this.name, this.brand, this.model);
        }
    }

    @Override
    public Map<String, String> getInfo(Current current) {
        createInstance();
        return instance.getInfo(current);
    }

    @Override
    public void changeName(String name, Current current) {
        createInstance();
        instance.changeName(name, current);
    }

    @Override
    public void changeSettings(Map<String, Short> settings, Current current) throws IllegalSettingValueException, UnrecognisedSettingException {
        createInstance();
        instance.changeSettings(settings, current);
    }

    @Override
    public void returnToFactorySettings(Current current) {
        createInstance();
        instance.returnToFactorySettings(current);
    }
}
