package sr.ice.server;

import Demo.A;
import Demo.Calc;
import com.zeroc.Ice.Current;
import com.zeroc.Ice.InvocationCanceledException;

import java.util.Arrays;

public class CalcI implements Calc {
	private static final long serialVersionUID = -2448962912780867770L;
	long counter = 0;

	@Override
	public long add(int a, int b, Current __current) {
		System.out.println("Received request from " + __current.id.name);
		System.out.println("ADD: a = " + a + ", b = " + b + ", result = " + (a + b));

		if (a > 1000 || b > 1000) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		if (__current.ctx.values().size() > 0) {
			System.out.println("There are some properties in the context");
		}

		return a + b;
	}

	@Override
	public long subtract(int a, int b, Current __current) {
		System.out.println("Received request from " + __current.id.name);
		System.out.println("SUBTRACT: a = " + a + ", b = " + b + ", result = " + (a - b));

		if (a > 1000 || b > 1000) {
			try {
				Thread.sleep(6000);
			} catch (InterruptedException ex) {
				Thread.currentThread().interrupt();
			}
		}

		if (__current.ctx.values().size() > 0) {
			System.out.println("There are some properties in the context");
		}

		return a - b;
	}


	@Override
	public /*synchronized*/ void op(A a1, short b1, Current current) {
		System.out.println("OP" + (++counter));
		try {
			Thread.sleep(500);
		} catch (java.lang.InterruptedException ex) {
			Thread.currentThread().interrupt();
		}
	}

	@Override
	public float avg(long[] array, Current __current) {
		System.out.println("Received request from " + __current.id.name);
		System.out.print("AVG: array = " + Arrays.toString(array) + ", result = ");
		int N = array.length;
		if (N == 0) {
			System.out.println("undefined");
			throw new InvocationCanceledException(new IllegalArgumentException("Array is empty"));
		}
		float avg = (float) Arrays.stream(array).sum() / N;
		System.out.println(avg);
		return avg;
	}
}