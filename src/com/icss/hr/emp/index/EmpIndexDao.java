package com.icss.hr.emp.index;


import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.Filter;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Sort;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.Highlighter;
import org.apache.lucene.search.highlight.InvalidTokenOffsetsException;
import org.apache.lucene.search.highlight.QueryScorer;
import org.apache.lucene.search.highlight.SimpleFragmenter;
import org.apache.lucene.search.highlight.SimpleHTMLFormatter;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import com.icss.hr.emp.pojo.Emp;

/**
 * ��Աȫ�ļ���dao 
 */
@Repository
public class EmpIndexDao {
	
	//��Ա����Ŀ¼��ʹ����Դ�ļ��ļ�ֵ��
	@Value("#{prop.emp_index_path}")
	private String empIndexDao;
	
	// ���ķִ���
	public Analyzer analyzer = new SmartChineseAnalyzer(Version.LUCENE_47);
	
	/**
	 * ��������
	 * @throws IOException 
	 */
	public void create(Document document) throws IOException {		
		//���������ķִ���
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		//����Ŀ¼����
		Directory directory = FSDirectory.open(new File(empIndexDao));
		//����д�����
		IndexWriter indexWriter = new IndexWriter(directory, config);
		//��������
		indexWriter.addDocument(document);
		//�ύ
		indexWriter.commit();
		//�ر�
		indexWriter.close();		
	}
	
	/**
	 * ��������
	 * @throws IOException 
	 */
	public void update(Term term,Document document) throws IOException {
		//���������ķִ���
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		//����Ŀ¼����
		Directory directory = FSDirectory.open(new File(empIndexDao));
		//����д�����
		IndexWriter indexWriter = new IndexWriter(directory, config);
		//�޸�����
		indexWriter.updateDocument(term, document);
		//�ύ
		indexWriter.commit();
		//�ر�
		indexWriter.close();		
	}	
	
	/**
	 * ɾ������
	 * @throws IOException 
	 */
	public void delete(Term term) throws IOException {
		//���������ķִ���
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		//����Ŀ¼����
		Directory directory = FSDirectory.open(new File(empIndexDao));
		//����д�����
		IndexWriter indexWriter = new IndexWriter(directory, config);
		//ɾ������
		indexWriter.deleteDocuments(term);
		//�ύ
		indexWriter.commit();
		//�ر�
		indexWriter.close();		
	}		
	
	/**
	 * ��������
	 * @throws IOException 
	 * @throws InvalidTokenOffsetsException 
	 */
	public List<Emp> search(Query query) throws IOException, InvalidTokenOffsetsException {
	
		//���������ķִ���
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_47, analyzer);
		//����Ŀ¼����
		Directory directory = FSDirectory.open(new File(empIndexDao));
		
		//��������
		IndexReader indexReader = DirectoryReader.open(directory);
		IndexSearcher indexSearcher = new IndexSearcher(indexReader);
		
		//���˶���(���ӵ�ɾѡ������Ч�ʱȽϵͣ���Ϊ��Ҫ���α�������)
		Filter filter = null;
		
		//�������Ĭ�ϰ�����ض����򣬾���ƥ���Խ��Խ��ǰ�������ڣ�
		Sort sort = new Sort();
		
		//�õ�����������ǰ100�м�¼
		TopDocs topDocs = indexSearcher.search(query,100,sort);
		
		//�ܼ�¼��
		int recordCount = topDocs.totalHits;
		
		//�ĵ�����
		ArrayList<Emp> empList = new ArrayList<>();
		
		// ============�����ͽ�ȡĳ���ֶε��ı�ժҪ����=============
		// ���û��Ƶ���β�ַ���
		SimpleHTMLFormatter formatter = new SimpleHTMLFormatter(
				"<font color=red>", "</font>");
		// �﷨������ʾ����
		Highlighter highlighter = new Highlighter(formatter, new QueryScorer(
				query));
		// 100���Ǳ�ʾժҪ������
		highlighter.setTextFragmenter(new SimpleFragmenter(100));
		// ===================================================
		
		//�����ĵ�����
		for (int i = 0;i < recordCount;i ++) {
			//���ԭʼ�ĵ�
			ScoreDoc scoreDoc = topDocs.scoreDocs[i];
			//����ĵ��ڲ����
			int docSn = scoreDoc.doc;
			//���Document�ĵ�
			Document document = indexSearcher.doc(docSn);
			
			//��ó�Ա���
			Integer empId = Integer.parseInt(document.get("empId"));
			//��ó�Ա����
			String empName = document.get("empName");
			//��ó�Ա�绰
			String empPhone = document.get("empPhone");
			//��ó�Ա���ҽ���
			String empInfo = document.get("empInfo");
			
			//�ѳ�Ա���ҽ��ܽ��н�ȡժҪ�Լ���Ӹ���
			TokenStream tream = analyzer.tokenStream("empInfo",
					new StringReader(empInfo));
			String empInfoFragment = highlighter
					.getBestFragment(tream, empInfo);
			
			//�������û�а��������ؼ��ֻ�ԭʼ���ݲ���100��
			if (empInfoFragment == null) {
				int minLen = empInfo.length() >= 100 ? 100 : empInfo.length();
				empInfoFragment = empInfo.substring(0, minLen);				
			}
						
			//��װΪ��Ա����
			Emp emp = new Emp();
			emp.setEmpId(empId);
			emp.setEmpName(empName);
			emp.setEmpPhone(empPhone);
			emp.setEmpInfo(empInfoFragment);
			
			//�ŵ������� 
			empList.add(emp);
		}		
		
		return empList;
	}

}