from pathlib import Path

from npj.ProblemSets import Problem

resource_path = Path('resources')

def get_valid_problem_names(paths):
    returns = []
    path = resource_path / paths[0] / paths[1]
    for entry in path.iterdir():
        if entry.name[entry.name.index('.') + 1:] == 'in':
            returns.append(entry.name[:entry.name.index('.')])
    return path, returns

def loop_problems(problem, method):
    if not isinstance(problem, Problem):
        raise NotImplementedError('Must speficy a problem like `NCNA18(\'A\')`!')
    path, entries = get_valid_problem_names(problem.get_paths())
    good = 0
    for file_name in entries:
        prob_path = path / (file_name + '.in')
        ans_path = path / (file_name + '.ans')
        in_txt = prob_path.read_text()
        ans_txt = ans_path.read_text().strip()
        res = method(in_txt).strip()
        if res == ans_txt:
            good += 1
        else:
            print("""
                    |============
                    |%s: FAIL!!! Expected
                    |%s
                    |---
                    |Got
                    |---
                    |%s
                    |============""" % (file_name, ans_txt, res))
    print("%s: PASS %d / %d" % (problem, good, len(entries)))