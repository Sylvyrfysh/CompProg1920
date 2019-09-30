cmake_minimum_required(VERSION 3.14)
project(c__)

set(CMAKE_CXX_STANDARD 17)

add_executable(c__ src/main.cpp src/npj/CppWrapper.h src/npj/CppWrapper.cpp src/npj/Problem.cpp src/npj/Problem.h src/npj/StringUtils.cpp src/npj/StringUtils.h src/npj/NPJFilesystem.cpp src/npj/NPJFilesystem.h)