//
// Created by johnsonn on 09/25/2019.
//

#ifndef C___PROBLEM_H
#define C___PROBLEM_H

#include <string>
#include <vector>

namespace npj {
    class Problem {
    public:
        typedef bool (checkFunc(std::vector<std::string>*, std::vector<std::string>*));
        checkFunc *checkFunction;
        static bool defaultCheckFunction(std::vector<std::string>* expect, std::vector<std::string>* get);

        [[nodiscard]] virtual std::string *getProblemPaths() const = 0;

        explicit Problem(checkFunc checkFunction) {
            if (checkFunction == nullptr) {
                this->checkFunction = defaultCheckFunction;
            } else {
                this->checkFunction = checkFunction;
            }
        };
    };

    class InternalProblem {
    protected:
        class NCNA : public Problem {
        protected:
            explicit NCNA(const char problem, checkFunc checkFunction = nullptr) : Problem(checkFunction), problem(problem) {};
            const char problem;
            const std::string problemString = std::string("Problem");
        };

    public:
        class NCNA18 : public NCNA {
        public:
            [[nodiscard]] std::string *getProblemPaths() const override;

            explicit NCNA18(const char problem, checkFunc checkFunction = nullptr) : NCNA(problem, checkFunction) {}
        };

        class NCNA17 : public NCNA {
        public:
            [[nodiscard]] std::string *getProblemPaths() const override;

            explicit NCNA17(const char problem, checkFunc checkFunction = nullptr) : NCNA(problem, checkFunction) {}
        };

        class CSAcademy : public Problem {
        protected:
            const std::string problemName;
        public:
            [[nodiscard]] std::string *getProblemPaths() const override;

            explicit CSAcademy(std::string problemName, checkFunc checkFunction = nullptr)
                    : Problem(checkFunction), problemName(std::move(problemName)) {}
        };
    };

    class Problems {
    public:
        class NCNA18 {
        public:
            static const Problem *ProblemA;
            static const Problem *ProblemB;
            static const Problem *ProblemC;
            static const Problem *ProblemD;
            static const Problem *ProblemE;
            static const Problem *ProblemF;
            static const Problem *ProblemG;
            static const Problem *ProblemH;
            static const Problem *ProblemI;
            static const Problem *ProblemJ;
        };

        class NCNA17 {
        public:
            static const Problem *ProblemA;
            static const Problem *ProblemB;
            static const Problem *ProblemC;
            static const Problem *ProblemD;
            static const Problem *ProblemE;
            static const Problem *ProblemF;
            static const Problem *ProblemG;
            static const Problem *ProblemH;
            static const Problem *ProblemI;
            static const Problem *ProblemJ;
        };

        class CSAcademy {
        public:
            static const Problem *BubblesLoop;
            static const Problem *OddDivisors;
        };
    };
}


#endif //C___PROBLEM_H
