class Problem:
    def get_paths(self):
        raise NotImplementedError('You must pick a subclass of problem like NCNA18(\'A\') !')

    def __str__(self):
        return self.get_paths()[0] + self.get_paths()[1]


class NCNA17(Problem):
    def get_paths(self):
        return 'NCNA2017', 'Problem' + self.letter

    def __init__(self, letter):
        self.letter = str(letter).capitalize()
        if self.letter.encode()[0] not in range('A'.encode()[0], 'K'.encode()[0]):
            raise NameError('NCNA17 Only has problems A through J!')


ncna17_a = NCNA17('A')
ncna17_b = NCNA17('B')
ncna17_c = NCNA17('C')
ncna17_d = NCNA17('D')
ncna17_e = NCNA17('E')
ncna17_f = NCNA17('F')
ncna17_g = NCNA17('G')
ncna17_h = NCNA17('H')
ncna17_i = NCNA17('I')
ncna17_j = NCNA17('J')


class NCNA18(Problem):
    def get_paths(self):
        return 'NCNA2018', 'Problem' + self.letter

    def __init__(self, letter):
        self.letter = str(letter).capitalize()
        if self.letter.encode()[0] not in range('A'.encode()[0], 'K'.encode()[0]):
            raise NameError('NCNA18 Only has problems A through J!')


ncna18_a = NCNA18('A')
ncna18_b = NCNA18('B')
ncna18_c = NCNA18('C')
ncna18_d = NCNA18('D')
ncna18_e = NCNA18('E')
ncna18_f = NCNA18('F')
ncna18_g = NCNA18('G')
ncna18_h = NCNA18('H')
ncna18_i = NCNA18('I')
ncna18_j = NCNA18('J')


class CSAcademy(Problem):
    def get_paths(self):
        return 'CSAcademy', self.name

    def __init__(self, name):
        self.name = name


csacademy_bubblesloop = CSAcademy('BubblesLoop')
csacademy_odddivisors = CSAcademy('OddDivisors')
