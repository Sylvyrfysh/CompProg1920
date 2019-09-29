# CompProg1920
Competitive Programming '19-'20

# Competitive Programming Workshop
Please sign up here: [Google Forms](https://forms.gle/nvYNCUMaHifznWsu6)

## On Pull
In order to properly set up all of the resources, you __must__ run `resources.bat`. This will set up all the resources in the location for each programming language.

## IntelliJ Setup for Java and Kotlin
0. Make sure you have run `resources.bat`!!!
1. Open IntelliJ
2. Go to `File > Open`. Go to the cloned directory. __The IntelliJ Project is in the jvm > CompProg1920 Folder. You must select the correct folder.__
3. Click through the import dialog.
4. Click on `Event Log` in the lower right hand corner.
5. Click on `Import Module from Gradle`
6. Check the `Use auto-import` box, then click OK
7. Wait for the project to sync. This may take a minute or two!
8. Open your preferred language example in the project explorer under `src > main > *language* > example`
9. Click the green run button next to the main function
10. If you get a module not specified error, change `Use classpath of module` to `CompProg1920.main`
11. If everything worked, you shold get a message saying that there were 3/3 passes!

Find the examples here:
- [Java](jvm/CompProg1920/src/main/java/example/CSAcademyOddDivisorsJava.java)
- [Kotlin](jvm/CompProg1920/src/main/kotlin/example/CSAcademyOddDivisorsKt.kt)

## PyCharm Setup for Python
In PyCharm, go to `File > Open`. Navigate to the root folder, go to the python folder, and select CompProg1920 from it. You might need to set the location of your python installation in `File > Settings > Project: CompProg1920 > Project Interpreter`. Example is at
- [Python](python/CompProg1920/example/example.py)

## CLion Setup for C/C++
Coming soon...

## Repo To-Do List
- [ ] Add C++ support
- [x] Add Python 3 support
- [ ] Add C support
- [ ] Add Create-Your-Own-Problem instructions
- [ ] Add Create-Your-Own-Problem PR template

## Want to add something?
Go ahead and create a PR! Whether it's new language support, a new problem set, same language with a new IDE, anything, create a PR! Make sure you:
- Allow us to push to your branch. Without this, we can't make changes we need to.
- State the purpose clearly and have a description. This lets us assess what your change intends to do.
- State anything you need us to do. Adding a new language/IDE might require extracting resources to a new place. We'll do our part to make your PR work.

# Current Tentative Schedule / Important Dates
| Date/Time    | Location     | Agenda                                 | Together                          | Advanced                                               | Beginner                                        |
| ------------ | ------------ | -------------------------------------- | --------------------------------- | ------------------------------------------------------ | ----------------------------------------------- |
| 9/25 5:00    | DH344        | [Week 1 Agenda](weeks/week1/README.md) | Intro to the club and tool setup  | Working in groups, basics review, approach discussion  | Basics of Competitive Programming and Designing |
| 10/2 5:00    | DH344        |                                        |                                   | Problems in rules-based solving, final group selection | Data Structures and their uses                  |
| 10/9 5:00    | DH344        |                                        |                                   | Selected problems, mock group competition              | Recursion and rules-based solutions             |
| 10/16 *6-8*  | DH310        |                                        | Special Guest Alumni Connor Hibbs | IEEEXtreme final preparations                          | Group problem solving                           |
| 10/18, 10/19 | DH TBD       |                                        | IEEEXtreme Competition!           | IEEEXtreme Competition!                                | IEEEXtreme Competition!                         |
| 11/9         | Epic Systems |                                        | ICPC                              | ICPC                                                   | ICPC                                            |
