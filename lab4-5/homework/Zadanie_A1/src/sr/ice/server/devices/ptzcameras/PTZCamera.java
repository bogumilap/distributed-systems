package sr.ice.server.devices.ptzcameras;


import IoT.*;
import com.zeroc.Ice.Current;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class PTZCamera extends Camera implements CameraOperation {
    private String name;
    private final String brand;
    private final String model;
    private final String type;
    private short pan;
    private short tilt;
    private short zoom;
    private final ReentrantLock lock;

    public PTZCamera(String name, String brand, String model) {
        this.name = name;
        this.brand = brand;
        this.model = model;
        this.type = "PTZCamera";
        this.pan = 0;
        this.tilt = 0;
        this.zoom = 0;
        this.lock = new ReentrantLock();
    }

    @Override
    public Map<String, String> getInfo(Current current) {
        lock.lock();

        Map<String, String> data = new HashMap<>();
        data.put("name", name);
        data.put("brand", brand);
        data.put("model", model);
        data.put("type", type);
        data.put("pan", String.valueOf(pan));
        data.put("tilt", String.valueOf(tilt));
        data.put("zoom", String.valueOf(zoom));

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
    public void changeSettings(Map<String, Short> settings, Current current) throws UnrecognisedSettingException, IllegalSettingValueException {
        lock.lock();
        for (Map.Entry<String, Short> entry : settings.entrySet()) {
            Short value = entry.getValue();
            switch (entry.getKey()) {
                case "pan" -> {
                    if (value > 360 || value < 0) {
                        lock.unlock();
                        throw new IllegalSettingValueException("Pan value has to be between 0 and 360");
                    }
                    pan = value;
                }
                case "tilt" -> {
                    if (value > 180 || value < 0) {
                        lock.unlock();
                        throw new IllegalSettingValueException("Tilt value has to be between 0 and 90");
                    }
                    tilt = value;
                }
                case "zoom" -> {
                    if (value > 25 || value < 0) {
                        lock.unlock();
                        throw new IllegalSettingValueException("Zoom value has to be between 0 and 25");
                    }
                    zoom = value;
                }
                default -> {
                    lock.unlock();
                    throw new UnrecognisedSettingException("Unrecognised setting " + entry.getKey() + " for " + type);
                }
            }
        }
        lock.unlock();
    }

    @Override
    public void returnToFactorySettings(Current current) {
        lock.lock();

        pan = 0;
        tilt = 0;
        zoom = 0;

        lock.unlock();
    }
}
