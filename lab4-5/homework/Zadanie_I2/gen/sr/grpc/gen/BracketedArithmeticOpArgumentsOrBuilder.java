// Generated by the protocol buffer compiler.  DO NOT EDIT!
// source: calculator.proto

package sr.grpc.gen;

public interface BracketedArithmeticOpArgumentsOrBuilder extends
    // @@protoc_insertion_point(interface_extends:calculator.BracketedArithmeticOpArguments)
    com.google.protobuf.MessageOrBuilder {

  /**
   * <code>.calculator.TwoArgOperationType optype = 1;</code>
   * @return The enum numeric value on the wire for optype.
   */
  int getOptypeValue();
  /**
   * <code>.calculator.TwoArgOperationType optype = 1;</code>
   * @return The optype.
   */
  sr.grpc.gen.TwoArgOperationType getOptype();

  /**
   * <code>.calculator.ComplexArithmeticOpArguments complexArg1 = 2;</code>
   * @return Whether the complexArg1 field is set.
   */
  boolean hasComplexArg1();
  /**
   * <code>.calculator.ComplexArithmeticOpArguments complexArg1 = 2;</code>
   * @return The complexArg1.
   */
  sr.grpc.gen.ComplexArithmeticOpArguments getComplexArg1();
  /**
   * <code>.calculator.ComplexArithmeticOpArguments complexArg1 = 2;</code>
   */
  sr.grpc.gen.ComplexArithmeticOpArgumentsOrBuilder getComplexArg1OrBuilder();

  /**
   * <code>.calculator.BracketedArithmeticOpArguments bracketedArg1 = 3;</code>
   * @return Whether the bracketedArg1 field is set.
   */
  boolean hasBracketedArg1();
  /**
   * <code>.calculator.BracketedArithmeticOpArguments bracketedArg1 = 3;</code>
   * @return The bracketedArg1.
   */
  sr.grpc.gen.BracketedArithmeticOpArguments getBracketedArg1();
  /**
   * <code>.calculator.BracketedArithmeticOpArguments bracketedArg1 = 3;</code>
   */
  sr.grpc.gen.BracketedArithmeticOpArgumentsOrBuilder getBracketedArg1OrBuilder();

  /**
   * <code>.calculator.ComplexArithmeticOpArguments complexArg2 = 4;</code>
   * @return Whether the complexArg2 field is set.
   */
  boolean hasComplexArg2();
  /**
   * <code>.calculator.ComplexArithmeticOpArguments complexArg2 = 4;</code>
   * @return The complexArg2.
   */
  sr.grpc.gen.ComplexArithmeticOpArguments getComplexArg2();
  /**
   * <code>.calculator.ComplexArithmeticOpArguments complexArg2 = 4;</code>
   */
  sr.grpc.gen.ComplexArithmeticOpArgumentsOrBuilder getComplexArg2OrBuilder();

  /**
   * <code>.calculator.BracketedArithmeticOpArguments bracketedArg2 = 5;</code>
   * @return Whether the bracketedArg2 field is set.
   */
  boolean hasBracketedArg2();
  /**
   * <code>.calculator.BracketedArithmeticOpArguments bracketedArg2 = 5;</code>
   * @return The bracketedArg2.
   */
  sr.grpc.gen.BracketedArithmeticOpArguments getBracketedArg2();
  /**
   * <code>.calculator.BracketedArithmeticOpArguments bracketedArg2 = 5;</code>
   */
  sr.grpc.gen.BracketedArithmeticOpArgumentsOrBuilder getBracketedArg2OrBuilder();

  sr.grpc.gen.BracketedArithmeticOpArguments.Arg1Case getArg1Case();

  sr.grpc.gen.BracketedArithmeticOpArguments.Arg2Case getArg2Case();
}