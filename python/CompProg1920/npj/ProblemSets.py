class Problem:
    def get_paths(self):
        raise NotImplementedError('You must pick a subclass of problem like NCNA18(\'A\') !')

    def __str__(self):
        return self.get_paths()[0] + "." + self.get_paths()[1]


class NCNA17(Problem):
    def get_paths(self):
        return 'NCNA2017', 'Problem' + self.letter

    def __init__(self, letter):
        self.letter = str(letter).capitalize()
        if self.letter.encode()[0] not in range('A'.encode()[0], 'K'.encode()[0]):
            raise NameError('NCNA17 Only has problems A through J!')


class NCNA18(Problem):
    def get_paths(self):
        return 'NCNA2018', 'Problem' + self.letter

    def __init__(self, letter):
        self.letter = str(letter).capitalize()
        if self.letter.encode()[0] not in range('A'.encode()[0], 'K'.encode()[0]):
            raise NameError('NCNA18 Only has problems A through J!')


class CSAcademy(Problem):
    def get_paths(self):
        return 'CSAcademy', self.name

    def __init__(self, name):
        self.name = name


class Problems:
    class NCNA17:
        a = NCNA17('a')
        b = NCNA17('b')
        c = NCNA17('c')
        d = NCNA17('d')
        e = NCNA17('e')
        f = NCNA17('f')
        g = NCNA17('g')
        h = NCNA17('h')
        i = NCNA17('i')
        j = NCNA17('j')

    class NCNA18:
        a = NCNA18('a')
        b = NCNA18('b')
        c = NCNA18('c')
        d = NCNA18('d')
        e = NCNA18('e')
        f = NCNA18('f')
        g = NCNA18('g')
        h = NCNA18('h')
        i = NCNA18('i')
        j = NCNA18('j')

    class CSAcademy:
        odd_divisors = CSAcademy('OddDivisors')
        bubbles_loop = CSAcademy('BubblesLoop')
