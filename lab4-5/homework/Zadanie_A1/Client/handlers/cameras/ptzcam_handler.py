from IoT import *
from handlers.utils import get_info, change_name, change_settings, return_to_factory_settings


class PTZCameraHandler:
    def __init__(self, name, communicator):
        self.name = name
        self._proxy = None
        self.communicator = communicator
        self.allowed_actions = {"getInfo",
                                "changeName",
                                "changeSettings",
                                "returnToFactorySettings"}
        self.settings = ["pan", "tilt", "zoom"]

    @property
    def proxy(self):
        if not self._proxy:
            proxy = self.communicator.propertyToProxy(self.name)
            self._proxy = CameraOperationPrx.checkedCast(proxy)
        return self._proxy

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
