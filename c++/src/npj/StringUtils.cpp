//
// Created by johnsonn on 09/26/2019.
//

#include "StringUtils.h"
#include <sstream>

namespace npj {
    std::string &ltrim(std::string &str, const std::string &chars) {
        str.erase(0, str.find_first_not_of(chars));
        return str;
    }

    std::string &rtrim(std::string &str, const std::string &chars) {
        str.erase(str.find_last_not_of(chars) + 1);
        return str;
    }

    std::string &trim(std::string &str, const std::string &chars) {
        return ltrim(rtrim(str, chars), chars);
    }

    std::vector<std::string> *splitIntoLines(const std::string &str) {
        auto *ret = new std::vector<std::string>;

        std::istringstream strstream(str);
        for (std::string line; getline(strstream, line); ret->push_back(line));

        return ret;
    }

    std::string joinToString(const std::vector<std::string> *lines) {
        std::string ret;

        for(const auto & line : *lines) {
            ret += line + '\n';
        }
        ret.erase(ret.size() - 1);

        return ret;
    }
}