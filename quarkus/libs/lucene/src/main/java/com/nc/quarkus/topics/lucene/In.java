package com.nc.quarkus.topics.lucene;

import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.LongBuffer;
import java.util.Map;
import java.util.Set;

import org.apache.lucene.store.AlreadyClosedException;
import org.apache.lucene.store.IndexInput;
import org.apache.lucene.store.RandomAccessInput;

final class In extends IndexInput implements RandomAccessInput {
	private static final LongBuffer EMPTY_LONGBUFFER = LongBuffer.allocate(0);

	final ByteBuffer chunk;

	LongBuffer[] views;

	public In(String resourceDescription, byte[] chunk) {
		super(resourceDescription);
		this.chunk = ByteBuffer.wrap(chunk);
	}

	public In(String resourceDescription, ByteBuffer chunk) {
		super(resourceDescription);
		this.chunk = chunk;
	}

	@Override
	public In clone() {
		var rv = slice("clone", 0, chunk.capacity());
		rv.seek(getFilePointer());

		return rv;
	}

	@Override
	public void close() throws IOException {

	}

	@Override
	public long getFilePointer() {
		return chunk.position();
	}

	private LongBuffer[] lazyInitViews() {
		var views = this.views;
		if (views == null) {
			views = new LongBuffer[Long.BYTES];
			var chunk = this.chunk;
			for (int i = 0; i < Long.BYTES; ++i) {
				if (i < chunk.limit()) {
					var dup = chunk.duplicate().order(ByteOrder.LITTLE_ENDIAN);
					dup.position(i);
					views[i] = dup.asLongBuffer();
				} else {
					views[i] = EMPTY_LONGBUFFER;
				}
			}
			this.views = views;
		}
		return views;
	}

	@Override
	public long length() {
		return chunk.capacity();
	}

	@Override
	public byte readByte() throws IOException {
		return chunk.get();
	}

	@Override
	public byte readByte(long pos) throws IOException {
		return chunk.get((int) pos);
	}

	@Override
	public void readBytes(byte[] dst, int off, int len) throws IOException {
		chunk.get(dst, off, len);
	}

	@Override
	public int readInt() throws IOException {
		return chunk.getInt();
	}

	@Override
	public int readInt(long pos) throws IOException {
		return chunk.getInt((int) pos);
	}

	@Override
	public void readLELongs(long[] dst, int off, int len) throws IOException {
		var views = lazyInitViews();

		try {
			var position = chunk.position();
			var view = views[position & 0x07];
			view.position(position >>> 3);
			view.get(dst, off, len);
			chunk.position(position + (len << 3));
		} catch (BufferUnderflowException e) {
			super.readLELongs(dst, off, len);
		} catch (NullPointerException npe) {
			throw new AlreadyClosedException("Already closed: " + this);
		}
	}

	@Override
	public long readLong() throws IOException {
		return chunk.getLong();
	}

	@Override
	public long readLong(long pos) throws IOException {
		return chunk.getLong((int) pos);
	}

	@Override
	public Map<String, String> readMapOfStrings() throws IOException {
		return super.readMapOfStrings();
	}

	@Override
	public Set<String> readSetOfStrings() throws IOException {
		return super.readSetOfStrings();
	}

	@Override
	public short readShort() throws IOException {
		return chunk.getShort();
	}

	@Override
	public short readShort(long pos) throws IOException {
		return chunk.getShort((int) pos);
	}

	@Override
	public String readString() throws IOException {
		return super.readString();
	}

	@Override
	public int readVInt() throws IOException {
		return super.readVInt();
	}

	@Override
	public long readVLong() throws IOException {
		return super.readVLong();
	}

	@Override
	public int readZInt() throws IOException {
		return super.readZInt();
	}

	@Override
	public long readZLong() throws IOException {
		return super.readZLong();
	}

	@Override
	public void seek(long pos) {
		chunk.position((int) pos);
	}

	@Override
	public void skipBytes(long count) throws IOException {
		seek(getFilePointer() + count);
	}

	@Override
	public In slice(String sliceDescription, long offset, long length) {
		var slice = chunk.slice((int) offset, (int) length);
		return new In(sliceDescription, slice);
	}

	@Override
	public String toString() {
		return "RamIn [" + super.toString() + ", chunk=" + chunk + "]";
	}
}
