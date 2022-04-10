package com.nc.topics.spell;

import java.nio.file.Files;
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
import org.apache.lucene.store.MMapDirectory;

import lombok.SneakyThrows;

public class CreateFSIndex {

	static final int MAX = 1000;

	@SneakyThrows
	static void del(Path p) {
		if (!Files.isDirectory(p)) {
			Files.deleteIfExists(p);
		}
	}

	public static void main(String[] args) throws Throwable {
		var config = new IndexWriterConfig();
		config.setMaxBufferedDocs(Integer.MAX_VALUE);
		var index = Paths.get("mmap");

		try (var s = Files.walk(index, 1)) {
			s.forEach(CreateFSIndex::del);
		}

		try (var dir = MMapDirectory.open(index); var iw = new IndexWriter(dir, config)) {
//			iw.deleteAll();
//			iw.commit();

			var i = 0;

			for (; i < MAX; i++) {
				var doc = new Document();

				doc.add(new IntPoint("point", i));
				doc.add(new StringField("hex", Integer.toHexString(i), Store.YES));

				iw.addDocument(doc);
			}

			System.out.println(i);
		}

		try (var dir = MMapDirectory.open(index); var ir = DirectoryReader.open(dir)) {
			var is = new IndexSearcher(ir);

			var docs = is.search(IntPoint.newExactQuery("point", 0), 1);

			var docId = docs.scoreDocs[0].doc;

			var doc = is.doc(docId);

			System.out.println(doc.get("hex"));

		}
	}
}
