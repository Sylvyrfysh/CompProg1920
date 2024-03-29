import sys

from npj import Python3Wrapper
from npj.ProblemSets import Problems


def tester():
    primes = []
    for i in range(32):
        primes.append(i * i)
    ins = [int(check_int) for check_int in sys.stdin.readline().split() if check_int.isdigit()]
    passes = 0
    for i in range(ins[0], ins[1] + 1):
        if i in primes:
            passes += 1
    print(passes)


# More problems can be found by autocompleting Problems and the classes it contains
Python3Wrapper.loop_problems(Problems.CSAcademy.odd_divisors, tester)
