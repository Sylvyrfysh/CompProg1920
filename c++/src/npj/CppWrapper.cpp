#include "CppWrapper.h"
#include "NPJFilesystem.h"
#include "StringUtils.h"
#include <iostream>
#include <fstream>
#include <sstream>

namespace fs = filesystem;

namespace npj {
    fs::path rootPath = "../../resources";

    void Wrapper::testWrapper(const npj::Problem *problem, wrapperFunc func) {
        auto problemPaths = problem->getProblemPaths();
        fs::path problemPath = (rootPath / problemPaths[0]) / problemPaths[1];

        auto acceptedFilenames = std::vector<std::string>();
        fs::directory_iterator i(problemPath), end;
        for (; i != end; ++i) {
            std::istringstream fillFilename(i->path().string());
            std::string filenameWithExt;
            //get the last part of the filename
            while (getline(fillFilename, filenameWithExt, '\\')) {}

            std::string filename;
            std::string ext;
            std::istringstream filenameWithExtStream(filenameWithExt);
            //split into name and ext
            getline(filenameWithExtStream, filename, '.');
            getline(filenameWithExtStream, ext);
            if (ext == "in") {
                acceptedFilenames.push_back(filename);
            }
        }

        std::streambuf *cinbuf = std::cin.rdbuf();
        std::streambuf *coutbuf = std::cout.rdbuf();
        int pass = 0;
        for (const auto &name: acceptedFilenames) {
            fs::path problemFile = problemPath / (name + ".in");
            std::ifstream probInFile(problemFile.c_str());

            fs::path ansFile = problemPath / (name + ".ans");
            std::ifstream ansInFile;
            ansInFile.open(ansFile.c_str());
            std::vector<std::string> ansLines;
            for (std::string line; getline(ansInFile, line); ansLines.push_back(line));

            std::cin.rdbuf(probInFile.rdbuf());

            std::ostringstream read;
            std::cout.rdbuf(read.rdbuf());

            func();

            std::string receivedAns = read.str();
            auto *receivedLines = npj::splitIntoLines(receivedAns);

            std::cout.rdbuf(coutbuf);
            if (problem->checkFunction(receivedLines, &ansLines)) {
                ++pass;
            } else {
                printf("============\n"
                             "%s: FAIL!!! Expected\n"
                             "%s\n"
                             "---\n"
                             "Got\n"
                             "---\n"
                             "%s\n"
                             "============\n",
                             name.c_str(),
                             npj::joinToString(&ansLines).c_str(),
                             npj::joinToString(receivedLines).c_str());
            }
        }
        std::cout << problemPaths[0] << "." << problemPaths[1] << " PASS: " << pass << "/" << acceptedFilenames.size() << std::endl;
        delete[] problemPaths;
    }
}