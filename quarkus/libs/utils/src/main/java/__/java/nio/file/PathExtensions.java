package __.java.nio.file;

import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

import lombok.SneakyThrows;

public class PathExtensions {

	@SneakyThrows
	public static boolean deleteIfExists(Path p) {
		return Files.deleteIfExists(p);
	}

	public static boolean exists(Path p, LinkOption... options) {
		return Files.exists(p, options);
	}

	@SneakyThrows
	public static byte[] read(Path p) {
		return Files.readAllBytes(p);
	}
}
