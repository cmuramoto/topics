package com.nc.topics.spell.service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import org.apache.lucene.document.IntPoint;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.IndexSearcher;

import com.nc.quarkus.topics.lucene.HeapDirectory;

import lombok.SneakyThrows;
import lombok.extern.jbosslog.JBossLog;

@JBossLog
public class FastIndex {

	static final IndexReader IR;

	static {
		try {
			var elapsed = System.nanoTime();
			var base = Paths.get("").toAbsolutePath();
			var path = base.resolve("index").toAbsolutePath();

			while (!Files.exists(path)) {
				base = base.getParent();
				path = base.resolve("index");
			}

			log.infof("Loading dir from %s", path);

			var dir = HeapDirectory.open(path, true);

			IR = DirectoryReader.open(dir);
			elapsed = System.nanoTime() - elapsed;
			log.infof("Loaded dir from %s in %dms", path, TimeUnit.NANOSECONDS.toMillis(elapsed));
		} catch (Exception e) {
			throw new ExceptionInInitializerError(e);
		}
	}

	@SneakyThrows
	public static String hex(int v) {
		var is = new IndexSearcher(IR);

		// var r = is.search(new TermQuery(new Term("key", String.valueOf(v))), 1);
		var r = is.search(IntPoint.newExactQuery("point", v), 1);

		var sd = r.scoreDocs;

		if (sd != null && sd.length == 1) {
			var id = sd[0].doc;

			var doc = is.doc(id);

			return doc.get("hex");
		}

		return null;
	}
}