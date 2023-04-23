import ray
import time
import cProfile
from copy import deepcopy

from common import get_array, ray_decorator

# Perform parallel bubble sort in ray
# Excercises 1.1) Try using local bubble sort and remote bubble sort, show difference
REPEATS_NUMBER = 100


def bubble_sort(array: list):
    for i in range(len(array)):
        for j in range(len(array)):
            if array[i] < array[j]:
                array[i], array[j] = array[j], array[i]


@ray.remote
def bubble_sort_distributed(array):
    return bubble_sort(array)


# Normal Python in a single process
def run_local(array: list) -> list:
    start_time = time.time()
    results = [bubble_sort(array) for _ in range(REPEATS_NUMBER)]
    print(f"local run time: {time.time() - start_time}")
    return results


# Distributed on a Ray cluster
@ray_decorator
def run_remote(array: list) -> list:
    start_time = time.time()
    results_sorting = [bubble_sort_distributed.remote(array) for _ in range(REPEATS_NUMBER)]
    results = ray.get(results_sorting)
    print(f"remote run time: {time.time() - start_time}")
    return results


if __name__ == "__main__":
    numbers_array = get_array()

    print('local run')
    local_results = run_local(deepcopy(numbers_array))
    # cProfile.run("run_local(deepcopy(numbers_array))")

    print('remote run')
    ray_results = run_remote(deepcopy(numbers_array))
    # cProfile.run("run_remote(deepcopy(numbers_array))")

