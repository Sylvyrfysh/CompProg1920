from pathlib import Path

resource_path = Path(Path().absolute().parent.parent).absolute()

while resource_path.name != 'CompProg1920':
    resource_path = resource_path.parent

resource_path = resource_path.joinpath(Path('resources'))

print(resource_path)

if not resource_path.exists():
    raise Exception('Resources do not appear to have been extracted! Run `resources.bat` in the root of this project '
                    'to get them!')

if len(list(resource_path.iterdir())) == 0:
    raise Exception('Resources do not appear to have been extracted! Run `resources.bat` in the root of this project '
                    'to get them!')