#ifndef C___CPPWRAPPER_H
#define C___CPPWRAPPER_H

#include "Problem.h"

namespace npj {
    class Wrapper {
    public:
        typedef void(*wrapperFunc)();
        static void testWrapper(const npj::Problem *problem, wrapperFunc func);
    };
}

#endif //C___CPPWRAPPER_H
