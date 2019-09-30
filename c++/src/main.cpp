#include "npj/Problem.h"
#include "npj/CppWrapper.h"
#include <iostream>
#include <vector>
#include <algorithm>
#include <sstream>

void oddDivisors() {
    int a, b;
    std::cin >> a >> b;
    std::vector<int> ints;
    for(int i = 1; i < 32; ++i) {
        ints.push_back(i * i);
    }

    int cnt = 0;
    for(int i = a; i <= b; ++i) {
        if(std::find(ints.begin(), ints.end(), i) != ints.end()) {
            ++cnt;
        }
    }

    std::cout << cnt << std::endl;
}

void bubblesLoop() {
    std::string readLine;
    std::getline(std::cin, readLine);
    int lines = std::stoi(readLine);

    for(int i = 0; i < lines; ++i) {
        std::string line1;
        std::getline(std::cin, line1);
        std::string p1, p2;
        bool appP1 = true;
        for(char& c: line1) {
            if(c == ' ') {
                appP1 = false;
            } else if(appP1) {
                p1 += c;
            } else {
                p2 += c;
            }
        }
        int vertices = std::stoi(p1);
        int edges = std::stoi(p2);
        std::string data;
        std::getline(std::cin, data);
        auto* verts = new std::vector<int>[vertices];
        std::stringstream stream(data);
        for(int e = 0; e < edges; ++e) {
            int first, second;
            stream >> first;
            stream >> second;
            verts[first].push_back(second);
            verts[second].push_back(first);
        }

        bool changed = true;
        while(changed) {
            changed = false;
            for(int pos = 0; pos < vertices; ++pos) {
                if(verts[pos].empty()) {
                    continue;
                } else if(verts[pos].size() == 1) {
                    changed = true;
                    int other = verts[pos][0];
                    verts[pos].clear();
                    std::size_t ind = 0;
                    for(; ind < verts[other].size(); ++ind) {
                        if(verts[other][ind] == pos) {
                            break;
                        }
                    }
                    verts[other].erase(verts[other].begin() + ind);
                }
            }
        }

        std::size_t total = 0;
        for(int pos = 0; pos < vertices; ++pos) {
            total += verts[pos].size();
        }
        if (total > 1) {
            total = 1;
        }
        std::cout << total << std::endl;
    }
}

int main() {
    npj::Wrapper::testWrapper(npj::Problems::CSAcademy::OddDivisors, oddDivisors);
    npj::Wrapper::testWrapper(npj::Problems::CSAcademy::BubblesLoop, bubblesLoop);
    return 0;
}