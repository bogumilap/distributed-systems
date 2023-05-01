from IoT import *


def get_info(handler):
    data = handler.proxy.getInfo()
    print(handler.name + " data:")
    for name, val in data.items():
        print(f"{name}: {val}")


def change_name(handler):
    new_name = input("enter new name: ")
    handler.proxy.changeName(new_name)


def change_settings(handler):
    print(f"available settings: {handler.settings}")
    print("type <setting> <new_value> to change setting or enter empty input to leave this mode")
    new_settings = {}
    while True:
        line = input("> ")
        if not line:
            break
        try:
            setting, new_value = line.split(" ", 1)
            setting = setting.strip()
            new_value = new_value.strip()
            new_settings[setting] = int(new_value)
        except ValueError:
            print("ERROR: wrong input format")
    try:
        handler.proxy.changeSettings(new_settings)
    except (UnrecognisedSettingException, IllegalSettingValueException) as e:
        print(f"ERROR: {e.reason}")
    print()


def return_to_factory_settings(handler):
    handler.proxy.returnToFactorySettings()
