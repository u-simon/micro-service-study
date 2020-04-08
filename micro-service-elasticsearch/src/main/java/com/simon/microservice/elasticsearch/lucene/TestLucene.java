package com.simon.microservice.elasticsearch.lucene;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StoredField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.MatchAllDocsQuery;
import org.apache.lucene.search.Query;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author simon
 * @date 2020/3/26 11:14
 * @describe 倾我一生一世念, 来似飞花散似烟
 */
public class TestLucene {


	public void testMatchAllDocsQuery() throws Exception {
		Directory directory = FSDirectory.open(Paths.get(""));

		// 创建IndexReader对象,需要制定Directory对象
		IndexReader indexReader = DirectoryReader.open(directory);

		// 创建IndexSearcher对象, 需要制定IndexReader对象
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);

		// 创建查询条件
		// 使用MatchAllDocsQuery查询索引目录中的所有文档
		Query query = new MatchAllDocsQuery();

	}

	/**
	 * 创建索引
	 * 
	 * @throws IOException
	 */
	public void testCreateIndex() throws IOException {
		// 指定创建索引库的存放位置Directory对象
		Directory directory = FSDirectory.open(Paths.get(""));

		// 指定一个标准分析器,对文档内容进行分析
		Analyzer analyzer = new StandardAnalyzer();

		// 创建你IndexWriterConfig对象
		IndexWriterConfig config = new IndexWriterConfig(analyzer);

		// 创建一个indexWriter对象
		IndexWriter indexWriter = new IndexWriter(directory, config);

		// 原始文档的路径
		File file = new File("/Users");
		File[] files = file.listFiles();

		for (File file1 : files) {
			// 创建Document对象
			Document document = new Document();

			String name = file1.getName();
			// 创建文件名域
			// String name 域的名称
			// String value 域的内容
			// Store store 是否存储
			Field fileNameField = new TextField("fileName", name, Field.Store.YES);
			// 文件路径
			String path = file1.getPath();
			Field filePathField = new StoredField("filePath", path);

			// 文件内容
			String content = FileUtils.readFileToString(file1, "UTF-8");
			TextField fileContentField = new TextField("fileContent", content, Field.Store.YES);

			document.add(fileNameField);
			document.add(filePathField);
			document.add(fileContentField);

			// 使用indexWriter对象将document对象写入索引库, 此过程进行索引创建, 并将索引和document对象写入索引库
			indexWriter.addDocument(document);
		}
		indexWriter.close();
	}

}
