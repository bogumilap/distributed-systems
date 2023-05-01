package sr.ice.server;

import com.zeroc.Ice.Communicator;
import com.zeroc.Ice.Identity;
import com.zeroc.Ice.ObjectAdapter;
import com.zeroc.Ice.Util;
import sr.ice.server.servants.coffeemakers.BeanCoffeMakerServant;
import sr.ice.server.servants.coffeemakers.CapsuleCoffeeMakerServant;
import sr.ice.server.servants.ptzcameras.PTZCameraServant;

public class IceServer {
    public void t1(String[] args) {
        int status = 0;
        Communicator communicator = null;

        try {
            // 1. Inicjalizacja ICE - utworzenie communicatora
            communicator = Util.initialize(args);

            // 2. Konfiguracja adaptera (Edit Configurations > "--Ice.Config=config.server")
            ObjectAdapter adapter = communicator.createObjectAdapter("Adapter1");

            // 3. Utworzenie serwanta/serwantów
            BeanCoffeMakerServant beanCoffeMakerServant = new BeanCoffeMakerServant("Bean #1", "PHILIPS", "123ABC");
            CapsuleCoffeeMakerServant capsuleCoffeeMakerServant = new CapsuleCoffeeMakerServant("Capsule #1", "DeLonghi", "XYZ");
            PTZCameraServant ptzCameraServant = new PTZCameraServant("PTZ", "Canon", "Security 45321");

            // 4. Dodanie wpisów do tablicy ASM, skojarzenie nazwy obiektu (Identity) z serwantem
            adapter.add(beanCoffeMakerServant, new Identity("BeanCoffeeMaker", "coffeemakers"));
            adapter.add(capsuleCoffeeMakerServant, new Identity("CapsuleCoffeeMaker", "coffeemakers"));
            adapter.add(ptzCameraServant, new Identity("PTZCamera", "cameras"));

            // 5. Aktywacja adaptera i wejście w pętlę przetwarzania żądań
            adapter.activate();

            System.out.println("Entering event processing loop...");

            communicator.waitForShutdown();

        } catch (Exception e) {
            e.printStackTrace(System.err);
            status = 1;
        }
        if (communicator != null) {
            try {
                communicator.destroy();
            } catch (Exception e) {
                e.printStackTrace(System.err);
                status = 1;
            }
        }
        System.exit(status);
    }


    public static void main(String[] args) {
        IceServer app = new IceServer();
        app.t1(args);
    }
}