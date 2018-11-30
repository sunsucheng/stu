package com.icss.hr.dataapi.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpEntity;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

/**
 * ����������ƽ̨������ѯ�ӿ�
 */
@WebServlet("/ConsServlet")
public class ConsServlet extends HttpServlet {

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		//���ñ���
		request.setCharacterEncoding("utf-8");
		response.setContentType("text/html;charset=utf-8");
		
		//��Ӧ�����
		PrintWriter out = response.getWriter();
		
		//�������
		String consName = request.getParameter("consName");
		String type = request.getParameter("type");

		// ����Ĭ�ϵ�httpClientʵ��
		CloseableHttpClient httpclient = HttpClients.createDefault();

		// ����httppost��������ƽ̨���ڱ��뷢��https����
		String url = "https://api.avatardata.cn/Constellation/Query";
		HttpPost httppost = new HttpPost(url);

		// ������������
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		params.add(new BasicNameValuePair("key", "93d246822adf462c9786f5a0803bb20b"));
		params.add(new BasicNameValuePair("consName", consName));
		params.add(new BasicNameValuePair("type", type));

		try {
			// ������������
			UrlEncodedFormEntity uefEntity = new UrlEncodedFormEntity(params, "UTF-8");
			httppost.setEntity(uefEntity);

			// ��ӡ�����ַ
			System.out.println("�����ַ�� " + httppost.getURI());

			// ����post����
			CloseableHttpResponse resp = httpclient.execute(httppost);

			// ��ӡ��Ӧ״̬
			System.out.println("״̬�룺" + resp.getStatusLine().getStatusCode());

			// ��Ӧʵ��
			HttpEntity entity = resp.getEntity();

			if (entity != null) {
				out.write(EntityUtils.toString(entity, "UTF-8"));
			}

			// �رգ��ͷ�����
			resp.close();
			httpclient.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

}