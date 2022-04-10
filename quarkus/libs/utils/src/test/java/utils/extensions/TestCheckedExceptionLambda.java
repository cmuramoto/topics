package utils.extensions;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import __.java.nio.file.PathExtensions;
import lombok.SneakyThrows;
import lombok.experimental.ExtensionMethod;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@ExtensionMethod({ PathExtensions.class, Arrays.class })
public class TestCheckedExceptionLambda {

	Stream<byte[]> map(Stream<Path> paths) {
		return paths.map(p -> p.read());
	}

	@Test
	@SneakyThrows
	public void run() {

		var path = Paths.get("pom.xml");

		var chunk = path.read();
		Assertions.assertTrue(chunk.length > 0);

		var streamed = Stream.of(path).map(p -> p.read()).findFirst().orElseThrow();

		Assertions.assertTrue(chunk.equals(streamed));

		log.info("Cool!");
	}

}
