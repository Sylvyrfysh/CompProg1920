//
// Created by johnsonn on 09/26/2019.
//

#ifndef C___STRINGUTILS_H
#define C___STRINGUTILS_H

#include <string>
#include <vector>

namespace npj {
    /**
     * Trims the left side (beginning) of the string of the specified characters. The string is modified and returned.
     * @param str The string to trim.
     * @param chars The characters to remove from the string
     * @return The string passed in
     */
    std::string &ltrim(std::string &str, const std::string &chars = "\t\n\v\f\r ");

    /**
     * Trims the right side (end) of the string of the specified characters. The string is modified and returned.
     * @param str The string to trim.
     * @param chars The characters to remove from the string
     * @return The string passed in
     */
    std::string &rtrim(std::string &str, const std::string &chars = "\t\n\v\f\r ");

    /**
     * Trims both sides of the string of the specified characters. The string is modified and returned.
     * @param str The string to trim.
     * @param chars The characters to remove from the string
     * @return The string passed in
     */
    std::string &trim(std::string &str, const std::string &chars = "\t\n\v\f\r ");

    /**
     * Splits the given string into lines.
     * @param str
     * @return
     */
    std::vector<std::string> *splitIntoLines(const std::string &str);

    /**
     *
     * @param lines
     * @return
     */
    std::string joinToString(const std::vector<std::string> *lines);
}

#endif //C___STRINGUTILS_H
