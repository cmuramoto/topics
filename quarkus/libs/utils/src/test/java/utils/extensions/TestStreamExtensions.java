package utils.extensions;

import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import __.java.util.stream.StreamExtensions;
import lombok.val;
import lombok.experimental.ExtensionMethod;

@ExtensionMethod({ StreamExtensions.class })
public class TestStreamExtensions {

	Stream<Integer> generate(int min, int max) {
		return IntStream.range(min, max).boxed();
	}

	@Test
	public void willCreateMutableList() {
		val s = generate(0, 10);
		val exp = generate(0, 10).toList();

		val r = s.list();

		Assertions.assertEquals(exp, r);

		r.add(10);

		Assertions.assertEquals(generate(0, 11).toList(), r);

		try {
			exp.add(10);
			Assertions.fail("JDK to list is immutable");
		} catch (Exception e) {

		}
	}

	@Test
	public void willCreateMutableSet() {
		val s = generate(0, 10);
		val exp = generate(0, 10).collect(Collectors.toSet());

		val r = s.set();

		Assertions.assertEquals(exp, r);

		r.add(10);
		exp.add(10);

		Assertions.assertEquals(exp, r);
		Assertions.assertEquals(generate(0, 11).collect(Collectors.toSet()), exp);
	}

}