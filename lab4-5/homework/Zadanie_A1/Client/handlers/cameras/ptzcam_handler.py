from IoT import *
from handlers.utils import get_info, change_name, change_settings, return_to_factory_settings


class PTZCameraHandler:
    def __init__(self, name, communicator):
        self.name = name
        self.proxy = CameraOperationPrx.checkedCast(communicator.propertyToProxy(name))
        self.communicator = communicator
        self.allowed_actions = {"getInfo",
                                "changeName",
                                "changeSettings",
                                "returnToFactorySettings"}
        self.settings = ["pan", "tilt", "zoom"]

    def print_allowed_actions(self):
        new_line = "\n"
        print(f"Allowed actions: {new_line}{new_line.join([f'  - {action}' for action in self.allowed_actions])}")

    def handle_action(self, action):
        match action:
            case "getInfo":
                get_info(self)
            case "changeName":
                change_name(self)
            case "changeSettings":
                change_settings(self)
            case "returnToFactorySettings":
                return_to_factory_settings(self)
            case _:
                print("ERROR: Action not recognised")
