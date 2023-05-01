import Ice
from IoT import *
from handlers.coffee_makers.beancm_handler import BeanCoffeeMakerHandler
from handlers.coffee_makers.capsulecm_handler import CapsuleCoffeeMakerHandler


config_file = "../config.client"


if __name__ == "__main__":
    with Ice.initialize(config_file) as communicator:
        devices = {"BeanCoffeeMaker": BeanCoffeeMakerHandler("BeanCoffeeMaker", communicator),
                   "CapsuleCoffeeMaker": CapsuleCoffeeMakerHandler("CapsuleCoffeeMaker", communicator)}

        print("Entering event processing loop...")

        print("Available devices:")
        for device_name in devices:
            print(device_name)

        while True:
            print()
            device_name = input("select device: ")
            if not (device := devices.get(device_name)):
                print("ERROR: Device not recognised.")
                print("Available devices:")
                for device_name in devices:
                    print(device_name)
                continue

            device.print_allowed_actions()
            action = input("action: ")
            device.handle_action(action)
