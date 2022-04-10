package __.java.lang;

public class Prims {

	public static double toMillis(double nanos) {
		return nanos / 1_000_000;
	}

	public static double toMillis(long nanos) {
		return ((double) nanos) / 1_000_000;
	}
}
