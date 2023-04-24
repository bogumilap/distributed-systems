import time
from fractions import Fraction

import ray
import random

from ray.types import ObjectRef

from common import ray_decorator


# 3.0 start remote cluster settings and observe actors in cluster
# a) make screenshot of dependencies -> file ex3.0.png


# 3.1, 3.2:
CALLERS = ["A", "B", "C"]


@ray.remote
class MethodStateCounter:
    def __init__(self):
        self.invokers = {
            "A": {
                "state": 0,
                "computed_values": []
            },
            "B": {
                "state": 0,
                "computed_values": []
            },
            "C": {
                "state": 0,
                "computed_values": []
            }
        }

    # 3.2 Modify method invoke to return a random int value between [5, 25]
    def invoke(self, name):
        if name not in self.invokers:
            raise ValueError(f"Invoker {name} doesn't exist.")
        # pretend to do some work here
        time.sleep(0.5)
        # update times invoked
        self.invokers[name]["state"] += 1
        # generate value between 5 and 25
        value = random.randint(5, 25)
        self.invokers[name]["computed_values"].append(value)
        # return generated value
        return value

    # 3.1a) - Get number of times an invoker name was called (modify)
    def get_invoker_state(self, name):
        # return the state of the named invoker
        if name not in self.invokers:
            raise ValueError(f"Invoker {name} doesn't exist.")
        return self.invokers[name]["state"]

    # 3.1b) - Get a list of values computed by invoker name (add)
    def get_all_invoker_computed_values(self):
        # return the computed values of all invokers
        return {invoker: self.invokers[invoker]["computed_values"] for invoker in self.invokers.keys()}

    # 3.1c) - Get state of all invokers (modify)
    def get_all_invoker_state(self):
        # return the state of all invokers
        return {invoker: self.invokers[invoker]["state"] for invoker in self.invokers.keys()}


@ray_decorator
def exercise_3_1_and_3_2():
    # Create an instance of our Actor
    worker_invoker = MethodStateCounter.remote()
    print(worker_invoker)

    # Invoke a random caller and fetch the value or invocations of a random caller
    print("method callers")
    for _ in range(5):
        random_name_invoker = random.choice(CALLERS)
        generated_value = ray.get(worker_invoker.invoke.remote(random_name_invoker))
        print(f"Named caller: {random_name_invoker} returned value {generated_value}")

    # Fetch the count of all callers
    print(ray.get(worker_invoker.get_all_invoker_state.remote()))
    print(ray.get(worker_invoker.get_all_invoker_computed_values.remote()))


# 3.3 Take a look on implement parallel Pi computation
# based on https://docs.ray.io/en/master/ray-core/examples/highly_parallel.html
#
# Implement calculating pi as a combination of actor (which keeps the
# state of the progress of calculating pi as it approaches its final value)
# and a task (which computes candidates for pi)
@ray.remote
class ProgressActor:
    def __init__(self, total_num_samples: int):
        self.total_samples_number: int = total_num_samples
        self.task_completed_samples_number: dict[str, int] = {}

    def report_progress(self, task_id: str, completed_samples_number: int):
        self.task_completed_samples_number[task_id] = completed_samples_number

    def get_progress(self) -> float:
        return sum(self.task_completed_samples_number.values()) / self.total_samples_number


@ray.remote
def task(id: str, samples_number: int, actor: ObjectRef[ProgressActor]):
    in_count = 0
    for i in range(samples_number):
        x, y = random.random(), random.random()
        if x * x + y * y <= 1:
            in_count += 1
        if i % 1000 == 0:
            actor.report_progress.remote(id, i)
    actor.report_progress.remote(id, samples_number)
    return Fraction(in_count, samples_number)


@ray_decorator
def exercise_3_3():
    tasks_number = 5
    samples_number = 1000 * 1000
    actor = ProgressActor.remote(tasks_number * samples_number)
    results = [task.remote(f"task{i + 1}", samples_number, actor) for i in range(tasks_number)]
    while True:
        progress = ray.get(actor.get_progress.remote())
        print(f"Progress: {int(progress * 100)}%")
        if progress == 1:
            break
        time.sleep(1)
    pi = 4 * float(sum(ray.get(results)) / tasks_number)
    print(f"Estimated: {pi}")


if __name__ == "__main__":
    # exercise_3_1_and_3_2()
    exercise_3_3()

