package com.nc.topics.spell;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.IntPoint;
import org.apache.lucene.document.StringField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import com.nc.quarkus.topics.lucene.HeapDirectory;

import io.quarkus.test.junit.TestProfile;
import lombok.SneakyThrows;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
@TestProfile(CreateIndexProfile.class)
@TestMethodOrder(OrderAnnotation.class)
public class CreateIndex {

	static final int MAX = 1000_000;

	static final int LOG_MODULUS = MAX / 100;

	final Path index = Paths.get("index");

	public CreateIndex() {
	}

	@Test
	@Order(0)
	public void create() {
		create(index, MAX);
	}

	@SneakyThrows
	public void create(Path index, int max) {
		var config = new IndexWriterConfig();
		config.setMaxBufferedDocs(Integer.MAX_VALUE);

		try (var dir = HeapDirectory.open(index, false)) {
			try (var iw = new IndexWriter(dir, config)) {
				var i = 0;

				for (; i < max; i++) {
					var doc = new Document();

					doc.add(new IntPoint("point", i));
					// doc.add(new StringField("key", Integer.toString(i), Store.NO));
					doc.add(new StringField("hex", Integer.toHexString(i), Store.YES));

					iw.addDocument(doc);

					if ((i + 1) % LOG_MODULUS == 0) {
						log.infof("Wrote %d", i + 1);
					}
				}
				iw.forceMerge(1, true);
			}
		}
	}

	@Test
	@Order(1)
	public void load() {
		load(MAX);
	}

	@SneakyThrows
	public void load(int max) {
		try (var dir = HeapDirectory.open(index, true); var ir = DirectoryReader.open(dir)) {
			var is = new IndexSearcher(ir);

			for (var i = 0; i < max; i++) {

				var docs = is.search(IntPoint.newExactQuery("point", i), 1);
				// var docs = is.search(new TermQuery(new Term("key", Integer.toString(i))), 1);

				var docId = docs.scoreDocs[0].doc;

				var doc = is.doc(docId);

				var hex = doc.get("hex");

				if (!hex.equals(Integer.toHexString(i))) {
					throw new IllegalArgumentException();
				}

				if ((i + 1) % LOG_MODULUS == 0) {
					log.infof("Read %d", i + 1);
				}
			}
		}
	}
}