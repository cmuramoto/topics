package com.nc.quarkus.topics.lucene;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.apache.lucene.index.IndexFileNames;
import org.apache.lucene.store.BaseDirectory;
import org.apache.lucene.store.IOContext;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.store.NoLockFactory;

import lombok.SneakyThrows;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public final class HeapDirectory extends BaseDirectory {

	@SuppressWarnings("unchecked")
	@SneakyThrows
	private static Map<String, byte[]> deserializeChunks(Path src) {
		try (var in = new ObjectInputStream(Files.newInputStream(src))) {
			return (Map<String, byte[]>) in.readObject();
		}
	}

	public static HeapDirectory open(Path src, boolean readOnly) throws IOException {
		var rd = new HeapDirectory();
		if (readOnly) {
			rd.chunks = deserializeChunks(src);
		} else {
			Files.deleteIfExists(src);
			rd.sync = src;
		}

		return rd;
	}

	Path sync;
	Map<String, byte[]> chunks = new HashMap<>();

	public HeapDirectory() {
		super(NoLockFactory.INSTANCE);
	}

	@Override
	@SneakyThrows
	public void close() {
		var chunks = this.chunks;
		if (sync == null || chunks == null || chunks.isEmpty()) {
			return;
		}
		try (var out = new ObjectOutputStream(Files.newOutputStream(sync, StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING))) {
			out.writeObject(chunks);
		}
	}

	@Override
	public IndexOutput createOutput(String name, IOContext context) throws IOException {
		return new Out("ram", name, this);
	}

	@Override
	public IndexOutput createTempOutput(String prefix, String suffix, IOContext context) throws IOException {
		var name = IndexFileNames.segmentFileName(prefix, suffix + "_" + Long.toString(System.nanoTime(), Character.MAX_RADIX), "tmp");

		return new Out("ram", name, this);
	}

	@Override
	public void deleteFile(String name) throws IOException {
		chunks.remove(name);
	}

	@Override
	public long fileLength(String name) throws IOException {
		return chunks.values().stream().mapToInt(v -> v.length).sum();
	}

	@Override
	public Set<String> getPendingDeletions() throws IOException {
		return Collections.emptySet();
	}

	@Override
	public String[] listAll() throws IOException {
		return this.chunks.keySet().toArray(String[]::new);
	}

	@Override
	public IndexInput openInput(String name, IOContext context) throws IOException {
		return new In(name, this.chunks.get(name));
	}

	@Override
	public void rename(String source, String dest) throws IOException {
		this.chunks.put(dest, Objects.requireNonNull(this.chunks.remove(source)));
	}

	public void set(String name, byte[] chunk) {
		if (chunks.putIfAbsent(name, chunk) != null) {
			throw new IllegalStateException("Attempt to overwrite " + name);
		}
	}

	@Override
	public void sync(Collection<String> names) throws IOException {
		log.infof("Requested sync of %s (noop)", names);
	}

	@Override
	public void syncMetaData() throws IOException {
		log.infof("Requested syncMetaData (noop)");
	}
}
