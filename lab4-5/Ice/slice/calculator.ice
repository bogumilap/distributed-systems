
#ifndef CALC_ICE
#define CALC_ICE

module Demo
{
  enum operation { MIN, MAX, AVG };
  
  exception NoInput {};

  struct A
  {
    short a;
    long b;
    float c;
    string d;
  };

  sequence<long> Array;

  interface Calc
  {
    idempotent long add(int a, int b);
    idempotent long subtract(int a, int b);
    void op(A a1, short b1); //załóżmy, że to też jest operacja arytmetyczna ;)
    idempotent float avg(Array array);
  };
// slice2java --output-dir generated slice/calculator.ice
};

#endif
