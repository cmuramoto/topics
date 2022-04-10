package __.java.util.function;

import java.util.function.Function;

import lombok.SneakyThrows;

@FunctionalInterface
public interface ThrowingFunction<T, R> extends Function<T, R> {

	@Override
	@SneakyThrows
	default R apply(T t) {
		return applyThrowing(t);
	}

	public R applyThrowing(T t) throws Throwable;

}
