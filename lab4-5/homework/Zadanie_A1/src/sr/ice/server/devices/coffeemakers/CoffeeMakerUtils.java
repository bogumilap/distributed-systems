package sr.ice.server.devices.coffeemakers;

import IoT.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class CoffeeMakerUtils {
    public static Map<String, String> getInfo(CoffeeMaker coffeeMaker) {
        Map<String, String> data = new HashMap<>();
        data.put("name", coffeeMaker.name);
        data.put("brand", coffeeMaker.brand);
        data.put("model", coffeeMaker.model);
        data.put("type", coffeeMaker.type);
        data.put("volume", String.valueOf(coffeeMaker.volume));
        data.put("beverageTypes", Arrays.toString(coffeeMaker.beverageTypes));
        return data;
    }

    public static void changeSettings(CoffeeMaker coffeeMaker, Map<String, String> settings) throws UnknownSettingException {
        for (Map.Entry<String, String> entry: settings.entrySet()) {
            switch (entry.getKey()) {
                case "volume" -> coffeeMaker.volume = Short.parseShort(entry.getValue());
                default ->
                        throw new UnknownSettingException("Unrecognised setting " + entry.getKey() + " for " + coffeeMaker.type);
            }
        }
    }
}