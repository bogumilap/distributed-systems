import logging
import re
from functools import wraps

import ray

FILES = ["data/file_1.txt", "data/file_2.txt"]


def get_array(extend_counter: int = 1) -> list:
    array = []
    number_regex = r"\s*(\d+),*"
    for file in FILES:
        with open(file, "r") as file_content:
            for line in file_content.readlines():
                if numbers := re.findall(number_regex, line):
                    array.extend([int(number) for number in numbers])
    for _ in range(extend_counter):
        array.extend(array)
    return array


def ray_decorator(f):
    @wraps(f)
    def wrapper(*args, **kwargs):
        # ray.init(address='auto', ignore_reinit_error=True, logging_level=logging.ERROR)
        if ray.is_initialized:
            ray.shutdown()
        ray.init(logging_level=logging.ERROR)

        f(*args, **kwargs)

        ray.shutdown()

    return wrapper


