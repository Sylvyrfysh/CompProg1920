from npj import Python3Wrapper
from npj import ProblemSets


def tester(s):
    primes = []
    for i in range(32):
        primes.append(i * i)
    ins = [int(check_int) for check_int in s.split() if check_int.isdigit()]
    passes = 0
    for i in range(ins[0], ins[1] + 1):
        if i in primes:
            passes += 1
    return str(passes)


Python3Wrapper.loop_problems(ProblemSets.csacademy_odddivisors, tester)
