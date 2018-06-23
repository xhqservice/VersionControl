package com.jadlsoft.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.util.Assert;
import org.w3c.dom.CharacterData;
import org.w3c.dom.Comment;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.EntityReference;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 文档处理类
 * 
 * @author wrc
 * @since 2008-12-31 下午03:29:20
 */
public class DocumentUtils {
	/**
	 * log
	 */
	private static final Log logger = LogFactory.getLog(DocumentUtils.class);
	
	/**
	 * 默认buffer大小
	 */
	public static final int BUFFER_SIZE = 4096;
	
	/**
	 * 默认文件名
	 */
	public static final String DEFAULT_FILE_NAME = "data";

	/**
	 * zip文件
	 */
	public static final String DEFAULT_ZIP_FILE_SUFFIX = ".zip";

	/**
	 * xml文件
	 */
	public static final String DEFAULT_XML_FILE_SUFFIX = ".xml";

	/**
	 * 默认文件内容编码
	 */
	public static final String DEFAULT_ENCODING = "UTF-8";

	/**
	 * 默认根节点
	 */
	public static final String DEFAULT_ROOT = "root";
	
	
	//---------------------------------------------------------------------
	// DOM
	//---------------------------------------------------------------------
	
	/**
	 * @return A new instance of a DOM Document object.
	 */
	public static Document newDocument() {
		return newDocumentBuilder().newDocument();
	}

	/**
	 * @return A new instance of a DocumentBuilder.
	 */
	public static DocumentBuilder newDocumentBuilder() {
		try {
			return DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 根据Document对象创建元素节点
	 * 
	 * @param doc
	 * @param elementNode
	 * @return 新的无属性，无值的Element对象
	 */
	public static Element createElement(Document doc, String elementNode) {
		return createElement(doc, elementNode, "");
	}

	/**
	 * 根据Document对象创建元素节点
	 * 
	 * @param doc
	 * @param elementNode
	 * @param text
	 * @return 新的无属性，有值的Element对象
	 */
	public static Element createElement(Document doc, String elementNode, String text) {
		return createElement(doc, elementNode, null, new HashMap(), text);
	}

	/**
	 * 根据Document对象创建元素节点
	 * 
	 * @param doc
	 * @param elementNode
	 * @param elementNodeAttributes
	 * @return 新的属性值为空，无值的Element对象
	 */
	public static Element createElement(Document doc, String elementNode, String[] elementNodeAttributes) {
		return createElement(doc, elementNode, elementNodeAttributes, new HashMap());
	}

	/**
	 * 根据Document对象创建元素节点
	 * 
	 * @param doc
	 * @param elementNode
	 * @param elementNodeAttributes
	 * @param elementNodeAttributeValues
	 * @return 新的属性值不为空，无值的Element对象
	 */
	public static Element createElement(Document doc, String elementNode, String[] elementNodeAttributes, Map elementNodeAttributeValues) {
		return createElement(doc, elementNode, elementNodeAttributes, elementNodeAttributeValues, "");
	}

	/**
	 * 根据Document对象创建元素节点
	 * 
	 * @param doc
	 * @param elementNode
	 * @param elementNodeAttributes
	 * @param textNode
	 * @return 新的属性值为空，有值的Element对象
	 */
	public static Element createElement(Document doc, String elementNode, String[] elementNodeAttributes, String textNode) {
		return createElement(doc, elementNode, elementNodeAttributes, new HashMap(), textNode);
	}

	/**
	 * 根据Document对象创建元素节点
	 * 
	 * @param doc Document对象
	 * @param elementNode 节点
	 * @param elementNodeAttributes  节点属性
	 * @param elementNodeAttributeValues 节点属性值
	 * @param textNode 文本节点值
	 * @return 根据条件返回新的Element对象
	 */
	public static Element createElement(Document doc, String elementNode, String[] elementNodeAttributes, Map elementNodeAttributeValues, String text) {
		Element el = doc.createElement(elementNode);
		if (elementNodeAttributes == null && "".equals(text)) {
			return el;
		}
		if (!"".equals(text)) {
			el.appendChild(createTextNode(doc, text));
		}
		if (elementNodeAttributes != null) {
			for (int i = 0; i < elementNodeAttributes.length; i++) {
				el.setAttribute(elementNodeAttributes[i], elementNodeAttributeValues.containsKey(elementNodeAttributes[i]) ? elementNodeAttributeValues.get(elementNodeAttributes[i]).toString() : "");
			}
		}
		return el;
	}

	/**
	 * 根据Document对象创建元素节点集
	 * 
	 * @param doc
	 * @param parentElementNode
	 * @param childElementsNode
	 * @param childElementNodeAttributes
	 * @param childNodeData
	 * @return (父 + 子)Element对象
	 * @throws DOMException
	 */
	public static Element createElement(Document doc, String parentElementNode, String[] childElementsNode, String[] childElementNodeAttributes, List childNodeData) {
		Element supEl = createElement(doc, parentElementNode);
		for (Iterator it = childNodeData.iterator(); it.hasNext();) {
			Map childElementValues = (Map) it.next();
			for (int i = 0; i < childElementsNode.length; i++) {
				Element subEl = createElement(doc, childElementsNode[i]);
				for (int j = 0; j < childElementNodeAttributes.length; j++) {
					subEl.setAttribute(childElementNodeAttributes[j], childElementValues.containsKey(childElementNodeAttributes[j]) ? childElementValues.get(childElementNodeAttributes[j]).toString() : "");
				}
				subEl.appendChild(createTextNode(doc, childElementValues.containsKey(childElementsNode[i]) ? childElementValues.get(childElementsNode[i]).toString() : ""));
				supEl.appendChild(subEl);
			}
		}
		return supEl;
	}

	/**
	 * 根据Document创建文本节点
	 * 
	 * @param text
	 * @return 新的Text节点对象
	 */
	public static Text createTextNode(Document document, String text) {
		return document.createTextNode(text);
	}

	/**
	 * 
	 * @param doc
	 * @param elementName
	 * @return
	 */
	public static String getElementValue(Document doc, String elementName) {
		try {
			NodeList nodelist = doc.getElementsByTagName(elementName);
			if (null != nodelist) {
				Element elem = (Element) nodelist.item(0);
				if (null != elem) {
					Node namechild = elem.getFirstChild();
					if (null != namechild) {
						return namechild.getNodeValue();
					}
				}
			}
		} catch (Exception e) {
			// error handling omitted
		}
		return null;
	}

	/**
	 * 
	 * @param node
	 * @return
	 */
	public static String getNodeValue(Node node) {
		StringBuffer buf = new StringBuffer();
		NodeList children = node.getChildNodes();
		for (int i = 0; i < children.getLength(); i++) {
			Node textChild = children.item(i);
			if (textChild.getNodeType() != Node.TEXT_NODE) {
				continue;
			}
			buf.append(textChild.getNodeValue());
		}
		return buf.toString();
	}

	/**
	 * 
	 * @param ele
	 * @param childEleName
	 * @return
	 */
	public static List getChildElementsByTagName(Element ele, String childEleName) {
		NodeList nl = ele.getChildNodes();
		List childEles = new ArrayList();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element && nodeNameEquals(node, childEleName)) {
				childEles.add(node);
			}
		}
		return childEles;
	}

	/**
	 * 
	 * @param ele
	 * @param childEleName
	 * @return
	 */
	public static Element getChildElementByTagName(Element ele, String childEleName) {
		NodeList nl = ele.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node node = nl.item(i);
			if (node instanceof Element && nodeNameEquals(node, childEleName)) {
				return (Element) node;
			}
		}
		return null;
	}

	/**
	 * 
	 * @param ele
	 * @param childEleName
	 * @return
	 */
	public static String getChildElementValueByTagName(Element ele, String childEleName) {
		Element child = getChildElementByTagName(ele, childEleName);
		return (child != null ? getTextValue(child) : null);
	}

	/**
	 * 
	 * @param node
	 * @param desiredName
	 * @return
	 */
	public static boolean nodeNameEquals(Node node, String desiredName) {
		Assert.notNull(node, "Node must not be null");
		Assert.notNull(desiredName, "Desired name must not be null");
		return (desiredName.equals(node.getNodeName()) || desiredName.equals(node.getLocalName()));
	}

	/**
	 * 
	 * @param valueEle
	 * @return
	 */
	public static String getTextValue(Element valueEle) {
		StringBuffer value = new StringBuffer();
		NodeList nl = valueEle.getChildNodes();
		for (int i = 0; i < nl.getLength(); i++) {
			Node item = nl.item(i);
			if ((item instanceof CharacterData && !(item instanceof Comment)) || item instanceof EntityReference) {
				value.append(item.getNodeValue());
			}
		}
		return value.toString();
	}
	
	//---------------------------------------------------------------------
	// 转换
	//---------------------------------------------------------------------
	
	/**
	 * 转换
	 * 
	 * @param source
	 * @param result
	 * @throws TransformerException 
	 */
	private static void doTransformer(Source source, Result result) throws TransformerException {
		Transformer transformer = newTransformer();
		transformer.setOutputProperty(OutputKeys.ENCODING, DEFAULT_ENCODING);
		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(source, result);
	}
	
	/**
	 * 构造转换工厂 --> 构造转换对象
	 * 
	 * @return A new instance of a Transformer.
	 */
	public static Transformer newTransformer() {
		try {
			return TransformerFactory.newInstance().newTransformer();
		} catch (TransformerConfigurationException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	/**
	 * 创建Source对象
	 * 
	 * @param o
	 * @return
	 */
	private static Source newSource(Object o) {
		if (o instanceof Document) {
			return new DOMSource((Document) o);
		}
		return null;
	}

	/**
	 * 创建Result对象
	 * 
	 * @param o
	 * @return
	 * @throws IOException 
	 */
	private static Result newResult(Object o) throws IOException {
		if (o instanceof StringWriter) {
			return new StreamResult((StringWriter) o);
		}
		if (o instanceof ByteArrayOutputStream) {
			return new StreamResult((ByteArrayOutputStream) o);
		}
		if (o instanceof String) {
			OutputStream out = newOutputStream((String) o);
			BufferedOutputStream bOut = new BufferedOutputStream(out);
			StreamResult sr = new StreamResult(bOut);
//			out.flush();
//			out.close();
//			bOut.flush();
//			bOut.close();
			return sr;
		}
		return null;
	}
	
	/**
	 * 根据指定路径生成FileOutputStream然后向上转型OutputStream
	 * 
	 * 类{@link FileOutputStream}功能：用于将信息写至文件。
	 * 
	 * @param path
	 * @return
	 */
	private static OutputStream newOutputStream(Object o) throws FileNotFoundException {
		if (o instanceof String) {
			return new FileOutputStream((String) o);
		}		
		return null;
	}
	
	/**
	 * 根据指定路径生成FileInputStream然后向上转型InputStream
	 * 
	 * 类{@link FileInputStream}功能：用于从文件中读取信息。
	 * 
	 * @param path
	 * @return
	 * @throws FileNotFoundException 
	 */
	private static InputStream newInputStream(Object o) throws FileNotFoundException {
		if (o instanceof String) {
			return new FileInputStream((String) o);
		}
		if (o instanceof File) {
			return new FileInputStream((File) o);
		}
		return null;
	}
	
	/**
	 * byte[] 转 InputStream
	 * 
	 * 类{@link ByteArrayInputStream}功能：将内存的缓冲区当作InputStream使用。
	 * 
	 * @param buf
	 * @return InputStream对象
	 */
	public static InputStream byteArrayToInputStream(byte[] buf) {
		return new ByteArrayInputStream(buf);
	}
	
	/**
	 * byte[] 转 Document
	 * 
	 * @param buf
	 * @return doc
	 */
	public static Document byteArrayToDocument(byte[] buf) {
		InputStream in = byteArrayToInputStream(buf);
		try {
			return newDocumentBuilder().parse(in);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * byte[] 转 String
	 * 
	 * @param buf
	 * @return
	 */
	public static String byteArrayToString(byte[] buf) {
		InputStream in = byteArrayToInputStream(buf);
		try {
			return inputStreamToString(in);
		} finally {
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * byte[]生成文件
	 * 
	 * @param buf 字节 
	 * @param absolutePath 文件路径
	 */
	public static void byteArrayToFile(byte[] buf, String absolutePath) {
		OutputStream out = null;
		try {
			out = newOutputStream(absolutePath);
			/**
			 * 用于写单个字节或字节数组。
			 */
			out.write(buf);
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(out != null) out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * InputStream 转 String
	 * 
	 * @param in
	 * @return
	 */
	public static String inputStreamToString(InputStream in) {
		InputStreamReader inReader = new InputStreamReader(in);
		BufferedReader bReader = new BufferedReader(inReader);
		StringWriter out = new StringWriter();
		try {
			char[] buffer = new char[BUFFER_SIZE];
			int bytesRead;
			while ((bytesRead = bReader.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inReader.close();
				bReader.close();
			} catch (IOException ex) {
				logger.warn("Could not close Reader", ex);
			}
			try {
				in.close();
			} catch (IOException ex) {
				logger.warn("Could not close InputStream", ex);
			}
			try {
				out.close();
			} catch (IOException ex) {
				logger.warn("Could not close Writer", ex);
			}
		}
		return out.toString();
	}
	
	/**
	 * InputStream 转 String
	 * 
	 * @param in
	 * @return
	 */
	public static String inputStreamToString_old(InputStream in) {
		InputStreamReader inReader = new InputStreamReader(in);
		BufferedReader bReader = new BufferedReader(inReader);
		
		/**
		 * jdk1.5
		 */
//		StringBuilder sb = new StringBuilder();
		StringBuffer sb = new StringBuffer();
		String s;
		try {
			
			while((s = bReader.readLine()) != null) {
				/**
				 * 字符串sb用来积累文件的全部内容(包括必须添加的换行符，因为readLine()已将他们删掉)。
				 */
				sb.append(s + "\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				inReader.close();
				bReader.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}
	
	/**
	 * InputSource 转 Document
	 * 
	 * @param in
	 * @return
	 */
	public static Document inputSourceToDocument(InputSource in) {
		try {
			return newDocumentBuilder().parse(in);
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * InputStream 转 byte[]
	 * 
	 * @param in
	 * @return
	 */
	public static byte[] inputStreamToByteArray(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream(BUFFER_SIZE);
		try {
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				logger.warn("Could not close InputStream", ex);
			}
			try {
				out.close();
			} catch (IOException ex) {
				logger.warn("Could not close OutputStream", ex);
			}
		}
		return out.toByteArray();
	}

	/**
	 * InputStream 转 byte[]
	 * 
	 * @param in
	 * @return
	 */
	public static byte[] inputStreamToByteArray_old(InputStream in) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		int h;
		try {
			/**
			 * read()：每次读入一个字节，并返回读入的字节，或者在遇到输入源结尾时返回-1。
			 */
			while ((h = in.read()) != -1) {
				out.write(h);
			}
			out.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				logger.warn("Could not close InputStream", ex);
			}
			try {
				out.close();
			} catch (IOException ex) {
				logger.warn("Could not close OutputStream", ex);
			}
		}
		return out.toByteArray();
	}

	/**
	 * 
	 * @param in
	 * @param out
	 * @return
	 * @throws IOException
	 */
	public static int inputStreamToOutputStream(InputStream in, OutputStream out) throws IOException {
		try {
			int byteCount = 0;
			byte[] buffer = new byte[BUFFER_SIZE];
			int bytesRead = -1;
			while ((bytesRead = in.read(buffer)) != -1) {
				out.write(buffer, 0, bytesRead);
				byteCount += bytesRead;
			}
			out.flush();
			return byteCount;
		} finally {
			try {
				in.close();
			} catch (IOException ex) {
				logger.warn("Could not close InputStream", ex);
			}
			try {
				out.close();
			} catch (IOException ex) {
				logger.warn("Could not close OutputStream", ex);
			}
		}
	}

	/**
	 * string 转 file
	 * @param s
	 */
	public static void stringToFile(String s, String absolutePath) {
		byteArrayToFile(s.getBytes(), absolutePath);
	}
	
	/**
	 * String 转 Document
	 * 
	 * @param s
	 * @return
	 */
	public static Document stringToDocument(String s) {
		try {
			return newDocumentBuilder().parse(new InputSource(new StringReader(s)));
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Document 转 byte[]
	 * 
	 * 类{@link ByteArrayOutputStream}功能：在内存中创建缓冲区。所有送往"流"的数据都要放置在此缓冲区。
	 * 
	 * @param doc
	 * @return byte[]
	 * @throws IOException 
	 */
	public static byte[] documentToByteArray(Document doc) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			doTransformer(newSource(doc), newResult(out));
			out.flush();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return out.toByteArray();
	}

	/**
	 * Document 转 xml文件
	 * 
	 * @param doc
	 * @param xmlFile
	 * @throws IOException 
	 */
	public static void documentToFile(Document doc, String xmlFile) {
		try {
			doTransformer(newSource(doc), newResult(xmlFile));
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Document 转 String
	 * 
	 * @param doc
	 * @return
	 * @throws IOException 
	 */
	public static String documentToString(Document doc) {
		StringWriter sw = new StringWriter();
		try {
			doTransformer(newSource(doc), newResult(sw));
			sw.flush();
		} catch (TransformerException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				sw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sw.getBuffer().toString();
	}

	/**
	 * byte[]读文件
	 * 
	 * @param absolutePath 文件路径
	 * @return byte[] 字节
	 */
	public static byte[] fileToByteArray(String absolutePath) {
		try {
			return inputStreamToByteArray(newInputStream(absolutePath));
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		return null;
	}
	/**
	 * char[]读文件
	 * 
	 * @param absolutePath 文件路径
	 * @return char[] 字符
	 */
	/*public static char[] fileToCharArray(String absolutePath) throws Exception {
		FileReader fReader = null;
		char[] c = null;
		try {
			fReader = new FileReader(absolutePath);
			fReader.read(c);
		} catch (IOException e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if(fReader != null)
				fReader.close();
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			}
		}
		return c;
	}*/

	public static String fileToString(String filename) throws Exception{
			BufferedReader bReader = null;
			try {
				bReader = new BufferedReader(new FileReader(filename));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
				throw e;
			}
			String s;
			/**
			 * jdk1.5
			 */
	//		StringBuilder sb = new StringBuilder();
			StringBuffer sb = new StringBuffer();
			try {
				while((s = bReader.readLine()) != null) {
					/**
					 * 字符串sb用来积累文件的全部内容(包括必须添加的换行符，因为readLine()已将他们删掉)。
					 */
					sb.append(s + "\n");
				}
			} catch (IOException e) {
				e.printStackTrace();
				throw e;
			} finally {
				try {
					bReader.close();
				} catch (IOException e) {
					e.printStackTrace();
					throw e;
				}
			}
			return sb.toString();
		}

	/**
	 * 
	 * @param in
	 * @param out
	 */
	public static void fileToFile(File in, File out) {
		try {
			inputStreamToOutputStream(new BufferedInputStream(new FileInputStream(in)), new BufferedOutputStream(new FileOutputStream(out)));
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * char[]生成文件
	 * 
	 * @param c 字符
	 * @param absolutePath 文件路径
	 */
	public static void charArrayToFile(char[] c, String absolutePath) {
		FileWriter fw = null;
		try {
			fw = new FileWriter(absolutePath);
			fw.write(c);
			fw.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(fw != null)
				fw.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * 压缩Document对象
	 * 
	 * @param doc
	 * @param entry
	 * @param zipName
	 * @throws IOException 
	 */
	public static void compress(Document doc, String entry, String zipName) {
		InputStream in = byteArrayToInputStream((documentToByteArray(doc)));
		ZipOutputStream zOut = null;
		try {
			zOut = new ZipOutputStream(newOutputStream(zipName));
			zOut.putNextEntry(new ZipEntry(entry));
			int h;
			while ((h = in.read()) != -1) {
				zOut.write(h);
			}
			zOut.flush();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if(in !=null)
				in.close();
				if(zOut !=null)
				zOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * @deprecated 压缩多个目录, 文件
	 * @param zipName 生成压缩包的名字
	 * @param absolutePaths 生成压缩包的路径
	 */
	public static void zip(String[] f, String zipName) {
		for (int i = 0; i < f.length; ++i) {
			zip(f[i], zipName);
		}
	}

	/**
	 * 压缩一个目录或一个文件
	 * 
	 * @param zipName
	 * @param absolutePath 文件路径
	 * @throws IOException 
	 */
	public static int zip(String absolutePath, String zipName) {
		ZipOutputStream zOut = null;
		try {
			zOut = new ZipOutputStream(new BufferedOutputStream(newOutputStream(zipName)));
			zip(new File(absolutePath), zOut);
			zOut.flush();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if(zOut != null)
				zOut.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	
	/**
	 * 压缩
	 * 
	 * @param zos
	 * @param file
	 * @throws IOException 
	 */
	private static void zip(File file, ZipOutputStream zOut) throws IOException {
		if (file.isDirectory()) {
			File[] files = file.listFiles();
			zOut.putNextEntry(new ZipEntry("/"));
			for (int i = 0; i < files.length; ++i) {
				zip(files[i], zOut);
			}
		} else {
			InputStream is = null;
			try {
				byte b[] = new byte[BUFFER_SIZE];
				is = newInputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is, BUFFER_SIZE);
				zOut.putNextEntry(new ZipEntry(file.getName()));
				int h;
				while ((h = bis.read(b, 0, BUFFER_SIZE)) != -1) {
					zOut.write(b, 0, h);
				}
			} finally {
				if(is != null)
				is.close();
			}
		}
	}

	/**
	 * 删除指定路径文件
	 * 
	 * @param file
	 */
	public static void deleteFile(String file) {
		new File(file).delete();		
	}
	
}
