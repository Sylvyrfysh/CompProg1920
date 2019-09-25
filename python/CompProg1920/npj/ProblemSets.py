class Problem:
    def get_paths(self):
        raise NotImplementedError('You must pick a subclass of problem like Problems.NCNA18.a!')

    def __str__(self):
        return self.get_paths()[0] + "." + self.get_paths()[1]

    def get_help(self):
        return "There is no custom help written for this problem!"


class NCNA(Problem):
    letter: str

    def get_paths(self):
        raise NotImplementedError('You must pick a subclass of problem like Problems.NCNA18.a!')

    def get_help(self):
        return """This problem set can be found at resources/%s/ProblemListing.pdf""" % self.get_paths()[0]

    def __init__(self, letter: str):
        if len(letter) != 1:
            raise Exception('%s requires one letter problem names!' % self.__class__.__qualname__)
        self.letter = letter.capitalize()
        if self.letter.encode()[0] not in range('A'.encode()[0], 'K'.encode()[0]):
            raise NameError('%s only has problems A through J!' % self.__class__.__qualname__)


class NCNA17(NCNA):
    def get_paths(self):
        return 'NCNA2017', 'Problem' + self.letter


class NCNA18(NCNA):
    def get_paths(self):
        return 'NCNA2018', 'Problem' + self.letter


class CSAcademy(Problem):
    name: str
    url: str

    def get_paths(self):
        return 'CSAcademy', self.name

    def __init__(self, name: str, url: str):
        self.name = name
        self.url = url

    def get_help(self):
        return """This problem is located at %s . Running your code in the online IDE will test more cases than are 
        entered here. You need to be logged in to this website in order for it to work, and may need to go back and 
        then forward or reload the page in order to get it to work."""


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
        odd_divisors = CSAcademy('OddDivisors',
                                 'https://csacademy.com/contest/archive/task/odd-divisor-count/')
        bubbles_loop = CSAcademy('BubblesLoop',
                                 'https://csacademy.com/ieeextreme-practice/task/979a09a0cd8c4e98dd0a690f39a55bd2/')
