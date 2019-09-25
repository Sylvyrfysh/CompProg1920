from pathlib import Path

if len(list(Path('resources').iterdir())) == 0:
    raise Exception('Resources do not appear to have been extracted! Run `resources.bat` in the root of this project '
                    'to get them!')