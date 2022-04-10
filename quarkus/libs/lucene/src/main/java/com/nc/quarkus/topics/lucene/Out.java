package com.nc.quarkus.topics.lucene;

import java.io.IOException;
import java.util.Arrays;
import java.util.zip.CRC32;
import java.util.zip.Checksum;

import org.apache.lucene.store.BufferedChecksum;
import org.apache.lucene.store.IndexOutput;
import org.apache.lucene.util.ArrayUtil;

final class Out extends IndexOutput {

	boolean closed;
	private int pos;
	byte[] chunk;
	final HeapDirectory dir;
	final Checksum crc;

	public Out(String resourceDescription, String name, HeapDirectory dir) {
		super(resourceDescription, name);
		this.chunk = new byte[1024];
		this.dir = dir;
		this.crc = new BufferedChecksum(new CRC32());
	}

	@Override
	public void close() throws IOException {
		if (closed) {
			return;
		}
		if (dir != null) {
			dir.set(this.getName(), Arrays.copyOf(chunk, pos));
		}
		pos = 0;
		closed = true;
	}

	@Override
	public long getChecksum() throws IOException {
		return crc.getValue();
	}

	@Override
	public long getFilePointer() {
		return pos;
	}

	byte[] require(int length) {
		var c = chunk;
		if (c == null) {
			c = new byte[length];
		} else if (c.length < length) {
			// c = Arrays.copyOf(c, length);
			c = ArrayUtil.grow(c, length);
		}
		chunk = c;
		return c;
	}

	@Override
	public void writeByte(byte b) throws IOException {
		crc.update(b);
		var pos = this.pos;
		require(pos + 1)[pos] = b;
		this.pos++;
	}

	@Override
	public void writeBytes(byte[] b, int offset, int length) throws IOException {
		crc.update(b, offset, length);
		var pos = this.pos;
		var chunk = require(pos + length - offset);

		System.arraycopy(b, offset, chunk, pos, length);

		this.pos += (length - offset);
	}
}
