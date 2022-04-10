package __.java.util.stream;

import java.util.Comparator;
import java.util.List;
import java.util.NavigableSet;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public final class StreamExtensions {

	public static <T> List<T> list(Stream<T> stream) {
		return stream.collect(Collectors.toList());
	}

	public static <T extends Comparable<T>> NavigableSet<T> navigableSet(Stream<T> stream) {
		return stream.collect(Collectors.toCollection(TreeSet::new));
	}

	public static <T> NavigableSet<T> navigableSet(Stream<T> stream, Comparator<T> cmp) {
		return stream.collect(Collectors.toCollection(() -> new TreeSet<>(cmp)));
	}

	public static <T> Set<T> set(Stream<T> stream) {
		return stream.collect(Collectors.toSet());
	}
}
