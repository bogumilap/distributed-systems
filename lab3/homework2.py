import cProfile
import time
from random import randint
from sys import getsizeof

import ray
from ray.exceptions import GetTimeoutError

from common import get_array, ray_decorator


# Perform sample modification of objects in ray
# Excercises 2.1) Create large lists and python dictionaries,
# put them in object store. Write a Ray task to process them.
# > 100 KB


def prepare_numbers():
    files = {"data/file_3_v1.txt": 4500,
             "data/file_3_v2.txt": 5500}
    for file, file_length in files.items():
        existing_numbers = set()
        col_counter = 0
        with open(file, "w") as current_file:
            while len(existing_numbers) < file_length:
                new_number = randint(0, 5 * 10 ** 8)
                if new_number not in existing_numbers:
                    existing_numbers.add(new_number)
                    current_file.write(f"{new_number}, ")
                    col_counter += 1
                    if col_counter == 10:
                        col_counter = 0
                        current_file.write("\n")


def print_object_info(obj: list | dict):
    obj_type = "list" if isinstance(obj, list) else "dict"
    print(f"Size of {obj_type}: {getsizeof(obj) / 1000} KB")
    print(f"Length of {obj_type}: {len(obj)} elements.")
    print()


@ray.remote
def task(obj: list | dict):
    print_object_info(obj)
    if isinstance(obj, list):
        result = [0 for _ in range(len(obj))]
        for i in range(len(obj)):
            for j in range(- len(obj) // 8, len(obj) // 8):
                result[i] += obj[j]
        return result
    if isinstance(obj, dict):
        result = 0
        for k, v in obj.items():
            for k1, v1 in obj.items():
                if diff := abs(k - k1) != 0:
                    if res := obj.get(diff):
                        for i in range(10):
                            result += res[i]
                    obj[k], obj[k1] = obj[k1], obj[k]
        return result


def time_out_function(obj_refs):
    for obj_ref in obj_refs:
        task_result = task.remote(obj_ref)
        start = time.time()
        try:
            ray.get(task_result, timeout=10)
        except GetTimeoutError:
            print(f"`get` timed out - ", end="")
        print(f"{(time.time() - start):.3f} s.")


def prepare_arrays():
    refs = []
    for counter in (5, 6, 7):
        array = get_array(counter)
        refs.append(ray.put(array))
    return refs


def prepare_dicts():
    dict_files = ["data/file_3_v1.txt", "data/file_3_v2.txt"]
    refs = []
    for dict_file in dict_files:
        keys = []
        with open(dict_file, "r") as file:
            for line in file.readlines():
                keys.extend([int(num) for num in line.split(", ") if num != "\n"])
        dictionary = dict.fromkeys(keys)
        for k in dictionary.keys():
            dictionary[k] = [k * 10 + i for i in range(10)]
        refs.append(ray.put(dictionary))
    return refs


@ray_decorator
def prepare_and_process_task(use_cProfile: bool):
    refs = prepare_arrays() + prepare_dicts()
    if use_cProfile:
        cProfile.run("time_out_function(refs)")
    else:
        time_out_function(refs)


if __name__ == "__main__":
    prepare_and_process_task(False)


# (task pid=2848) Size of list: 51.256 KB
# (task pid=2848) Length of list: 6400 elements.
# (task pid=2848)
# 1.339 s.
# (task pid=2848) Size of list: 102.456 KB
# (task pid=2848) Length of list: 12800 elements.
# (task pid=2848)
# 4.599 s.
# (task pid=2848) Size of list: 204.856 KB
# (task pid=2848) Length of list: 25600 elements.
# (task pid=2848)
# `get` timed out - 10.096 s.
# (task pid=15336) Size of dict: 147.552 KB
# (task pid=15336) Length of dict: 4500 elements.
# (task pid=15336)
# 8.598 s.
# (task pid=15336) Size of dict: 295.0 KB
# (task pid=15336) Length of dict: 5500 elements.
# (task pid=15336)
# `get` timed out - 10.082 s.
