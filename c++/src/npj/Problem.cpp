#pragma clang diagnostic push
#pragma ide diagnostic ignored "cert-err58-cpp"

#include "Problem.h"
#include "StringUtils.h"

namespace npj {
#ifdef _MSC_VER
#define CHARIFY(x) #@x
#else
#define CONCAT_H(x,y,z) x##y##z
#define SINGLEQUOTE '
#define CONCAT(x,y,z) CONCAT_H(x,y,z)
#define CHARIFY(x) CONCAT(SINGLEQUOTE , x , SINGLEQUOTE )
#endif
#define CREATE_NCNA(year, letter) const npj::Problem* Problems::NCNA##year::Problem##letter = new npj::InternalProblem::NCNA##year(CHARIFY(letter));
    const npj::Problem *Problems::NCNA18::ProblemA = new npj::InternalProblem::NCNA18('A');
    CREATE_NCNA(18, B)
    CREATE_NCNA(18, C)
    CREATE_NCNA(18, D)
    CREATE_NCNA(18, E)
    CREATE_NCNA(18, F)
    CREATE_NCNA(18, G)
    CREATE_NCNA(18, H)
    CREATE_NCNA(18, I)
    CREATE_NCNA(18, J)
    const npj::Problem *Problems::NCNA17::ProblemA = new npj::InternalProblem::NCNA17('A');
    CREATE_NCNA(17, B)
    CREATE_NCNA(17, C)
    CREATE_NCNA(17, D)
    CREATE_NCNA(17, E)
    CREATE_NCNA(17, F)
    CREATE_NCNA(17, G)
    CREATE_NCNA(17, H)
    CREATE_NCNA(17, I)
    CREATE_NCNA(17, J)
#undef CREATE_NCNA
#undef CHARIFY
#ifndef _MSC_VER
#undef CONCAT
#undef SINGLEQUOTE
#undef CONCAT_H
#endif
#define STRINGIFY(x) #x
#define CREATE_CSACADEMY_I(name, none) const npj::Problem* Problems::CSAcademy::name##none = new npj::InternalProblem::CSAcademy(STRINGIFY(name));
#define CREATE_CSACADEMY(name) CREATE_CSACADEMY_I(name,)
    const npj::Problem *Problems::CSAcademy::BubblesLoop = new npj::InternalProblem::CSAcademy("BubblesLoop");
    CREATE_CSACADEMY(OddDivisors)
#undef STRINGIFY
#undef CREATE_CSACADEMY_I
#undef CREATE_CSACADEMY

    std::string *InternalProblem::NCNA18::getProblemPaths() const {
        auto *ret = new std::string[2];
        ret[0] = "NCNA2018";
        ret[1] = problemString + problem;
        return ret;
    }

    std::string *InternalProblem::NCNA17::getProblemPaths() const {
        auto *ret = new std::string[2];
        ret[0] = "NCNA2017";
        ret[1] = problemString + problem;
        return ret;
    }

    std::string *InternalProblem::CSAcademy::getProblemPaths() const {
        auto *ret = new std::string[2];
        ret[0] = "CSAcademy";
        ret[1] = problemName;
        return ret;
    }

    bool Problem::defaultCheckFunction(std::vector<std::string> *expect, std::vector<std::string> *get) {
        if (expect->size() != get->size()) {
            return false;
        }
        auto getIter = get->begin();
        for (auto ptr = expect->begin(); ptr < expect->end(); ptr++, getIter++) {
            if (npj::trim(*getIter) != npj::trim(*ptr)) {
                return false;
            }
        }
        return true;
    }
}
#pragma clang diagnostic pop