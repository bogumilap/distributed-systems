from IoT import *
from handlers.utils import get_info, change_settings, change_name, return_to_factory_settings


class BeanCoffeeMakerHandler:
    def __init__(self, name, communicator):
        self.name = name
        self.proxy = CoffeeMakerOperationPrx.checkedCast(communicator.propertyToProxy(name))
        self.communicator = communicator
        self.allowed_actions = {"getInfo",
                                "changeName",
                                "changeSettings",
                                "returnToFactorySettings",
                                "makeBeverage",
                                "increaseIngredientQuantity"}
        self.settings = ["beveragesVolume"]
        self.beverages = {
            "ESPRESSO": BeverageType.ESPRESSO,
            "AMERICANO": BeverageType.AMERICANO}
        self.ingredients = {"COFFEEBEANS": Ingredient.COFFEEBEANS}

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
            case "makeBeverage":
                print(f"Available beverages: {', '.join([beverage for beverage in self.beverages.keys()])}")
                beverage_name = input("beverage: ")
                if not (beverage_type := self.beverages.get(beverage_name)):
                    print("ERROR: Beverage name not recognised")
                    return
                try:
                    beverage = self.proxy.makeBeverage(beverage_type)
                except (UnsupportedBeverageTypeException, NotEnoughIngredientsException) as e:
                    print("ERROR:", e.reason)
                    return
                print(f"Prepared {beverage.beverageType}.")
            case "increaseIngredientQuantity":
                print(f"Stored ingredients: {', '.join([ingredient for ingredient in self.ingredients.keys()])}")
                ingredient = input("ingredient: ")
                quantity = input("quantity: ")
                try:
                    self.proxy.increaseIngredientQuantity(self.ingredients.get(ingredient), int(quantity))
                except (IllegalIngredientException, IllegalIngredientQuantityException) as e:
                    print(f"ERROR: {e.reason}")
                    return
                except ValueError as e:
                    print(f"ERROR: {e}")
                    return
                print(f"Successfully set quantity of {ingredient} to {quantity} units")
            case _:
                print("ERROR: Action not recognised")
