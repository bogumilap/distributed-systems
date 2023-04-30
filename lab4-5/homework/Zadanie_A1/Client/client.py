import Ice
from IoT import *
from handlers.coffee_makers.beancm_handler import BeanCoffeeMakerHandler
from handlers.coffee_makers.capsulecm_handler import CapsuleCoffeeMakerHandler


config_file = "../config.client"


def get_proxies(communicator):
    devices = dict()
    with open(config_file) as file:
        for line in file:
            if line == "\n":
                continue
            if line.startswith("# END DEVICE DEFINITIONS"):
                break

            name = line.split("=", 1)[0]

            if "BeanCoffeeMaker" in name:
                # base = communicator.stringToProxy("BeanCoffeeMaker:default -p 10000")
                device = BeanCoffeeMakerHandler(name, communicator)
            elif "capsuleCoffeeMaker" in name:
                device = CapsuleCoffeeMakerHandler(name, communicator)
            else:
                raise ValueError("Device", name, " not recognized.")

            devices[name] = device
    return devices


if __name__ == "__main__":
    with Ice.initialize(config_file) as communicator:
        # don't create proxies yet - they'll be created lazily with first
        # communication attempt, this way server can lazily create servants
        devices = get_proxies(communicator)
        if not devices:
            print("No proxies found for provided file.")
            exit(1)

        print("Client ready, entering processing loop.")

        print("Available devices:")
        for device_name in devices:
            print(device_name)

        print()
        print("To use device, enter it's name first. Empty input exits to "
              "main menu (unless written otherwise).")
        print()

        while True:
            print("Select device:", end="")
            name = input("\n=> ")
            if not name or name == "exit":
                print("Goodbye!")
                exit(0)
            if name not in devices:
                print("Unknown device.")
                print()
                continue

            device = devices[name]
            device.print_allowed_actions()

            action = input("\n====> ")
            if not action:
                continue
            if action not in device.allowed_actions:
                print("Illegal action for ", device.name, ".", sep="")
                print()
                continue

            try:
                device.handle_action(action)
            except Ice.EndpointParseException:
                print("Incorrect port for device ", device.name,
                      ", removing it from available devices.", sep="")
                del devices[name]
                print()
                print("Available devices:")
                for device_name in devices:
                    print(device_name)
                print()