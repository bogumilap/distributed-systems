package sr.grpc.server;

import io.grpc.stub.StreamObserver;
import sr.grpc.gen.AdvancedCalculatorGrpc.AdvancedCalculatorImplBase;
import sr.grpc.gen.BracketedArithmeticOpArguments;
import sr.grpc.gen.ComplexArithmeticOpResult;

import java.util.List;

public class AdvancedCalculatorImpl extends AdvancedCalculatorImplBase 
{
	@Override
	public void complexOperation(sr.grpc.gen.ComplexArithmeticOpArguments request,
			io.grpc.stub.StreamObserver<sr.grpc.gen.ComplexArithmeticOpResult> responseObserver) 
	{
		System.out.println("multipleArgumentsRequest (" + request.getOptypeValue() + ", #" + request.getArgsCount() +")");
		
		if(request.getArgsCount() == 0) {
			System.out.println("No agruments");
		}
		
		double res = getResult(request.getOptype().name(), request.getArgsList());
		
		ComplexArithmeticOpResult result = ComplexArithmeticOpResult.newBuilder().setRes(res).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}

	private double getResult(String operation, List<Double> args) {
		double res = 0;
		switch (operation) {
			case "SUM":
				for (Double d : args) res += d;
				break;
			case "AVG":
				for (Double d : args) res += d;
				res /= args.size();
				break;
			case "MIN":
				res = args.get(0);
				for (Double d : args) {
					if (d < res) {
						res = d;
					}
				}
				break;
			case "MAX":
				res = args.get(0);
				for (Double d : args) {
					if (d > res) {
						res = d;
					}
				}
			default:
				System.out.println("ERROR: Unrecognised operation.");
				break;
		}
		return res;
	}

	private double getBracketedResult(BracketedArithmeticOpArguments bracketedOpArguments) {
		BracketedArithmeticOpArguments.Arg1Case arg1Case = bracketedOpArguments.getArg1Case();
		double arg1Result = switch (arg1Case) {
			case COMPLEXARG1 -> getResult(bracketedOpArguments.getComplexArg1().getOptype().name(), bracketedOpArguments.getComplexArg1().getArgsList());
			case BRACKETEDARG1 -> getBracketedResult(bracketedOpArguments.getBracketedArg1());
			case ARG1_NOT_SET -> throw new IllegalArgumentException("arg1 is not set");
		};

		BracketedArithmeticOpArguments.Arg2Case arg2Case = bracketedOpArguments.getArg2Case();
		double arg2Result = switch (arg2Case) {
			case COMPLEXARG2 -> getResult(bracketedOpArguments.getComplexArg2().getOptype().name(), bracketedOpArguments.getComplexArg2().getArgsList());
			case BRACKETEDARG2 -> getBracketedResult(bracketedOpArguments.getBracketedArg2());
			case ARG2_NOT_SET -> throw new IllegalArgumentException("arg2 is not set");
		};

		return switch (bracketedOpArguments.getOptype()) {
			case MULTIPLY -> arg1Result * arg2Result;
			case DIVIDE -> arg1Result / arg2Result;
			case UNRECOGNIZED -> throw new IllegalArgumentException("operation" + bracketedOpArguments.getOptype() + "was not recognised");
		};
	}

	@Override
	public void bracketedComplexOperation(BracketedArithmeticOpArguments request, StreamObserver<ComplexArithmeticOpResult> responseObserver) {
		double res = getBracketedResult(request);
		ComplexArithmeticOpResult result = ComplexArithmeticOpResult.newBuilder().setRes(res).build();
		responseObserver.onNext(result);
		responseObserver.onCompleted();
	}
}
