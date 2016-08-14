package cn.jayli.tools.general.marksix;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MarkSix {
	static String SendGet(String url) {
		// 定义一个字符串用来存储网页内容
		String result = "";
		// 定义一个缓冲字符输入流
		BufferedReader in = null;
		try {
			// 将string转成url对象
			URL realUrl = new URL(url);
			// 初始化一个链接到那个url的连接
			URLConnection connection = realUrl.openConnection();
			// 开始实际的连接
			connection.connect();
			// 初始化 BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(
					connection.getInputStream(), "GB2312"));
			// 用来临时存储抓取到的每一行的数据
			String line;
			while ((line = in.readLine()) != null) {
				// 遍历抓取到的每一行并将其存储到result里面
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送GET请求出现异常！" + e);
			e.printStackTrace();
		}
		// 使用finally来关闭输入流
		finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}
		return result;
	}

	static boolean createNewFile(String filePath) {
		boolean isSuccess = true;
		// 如有则将"\\"转为"/",没有则不产生任何变化
		String filePathTurn = filePath.replaceAll("\\\\", "/");
		// 先过滤掉文件名
		int index = filePathTurn.lastIndexOf("/");
		String dir = filePathTurn.substring(0, index);
		// 再创建文件夹
		File fileDir = new File(dir);
		isSuccess = fileDir.mkdirs();
		// 创建文件
		File file = new File(filePathTurn);
		try {
			isSuccess = file.createNewFile();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		}
		return isSuccess;
	}
	
	static boolean writeIntoFile(String content, String filePath,
			boolean isAppend) {
		boolean isSuccess = true;
		// 先过滤掉文件名
		int index = filePath.lastIndexOf(File.separatorChar);
		String dir = filePath.substring(0, index);
		// 创建除文件的路径
		File fileDir = new File(dir);
		fileDir.mkdirs();
		// 再创建路径下的文件
		File file = null;
		try {
			file = new File(filePath);
			file.createNewFile();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		}
		// 写入文件
		FileWriter fileWriter = null;
		try {
			fileWriter = new FileWriter(file, isAppend);
			System.out.println("文件流的编码格式："+fileWriter.getEncoding());
			fileWriter.write(content);
			fileWriter.flush();
		} catch (IOException e) {
			isSuccess = false;
			e.printStackTrace();
		} finally {
			try {
				if (fileWriter != null)
					fileWriter.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		return isSuccess;
	}
	
	 /**
	  * 字符串编码转换的实现方法
	  * @param str  待转换编码的字符串
	  * @param newCharset 目标编码
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	static String changeCharset(String str, String newCharset)
	   throws UnsupportedEncodingException {
	  if (str != null) {
	   //用默认字符编码解码字符串。
	   byte[] bs = str.getBytes();
	   //用新的字符编码生成字符串
	   return new String(bs, newCharset);
	  }
	  return null;
	 }
	 /**
	  * 字符串编码转换的实现方法
	  * @param str  待转换编码的字符串
	  * @param oldCharset 原编码
	  * @param newCharset 目标编码
	  * @return
	  * @throws UnsupportedEncodingException
	  */
	 static String changeCharset(String str, String oldCharset, String newCharset)
	   throws UnsupportedEncodingException {
	  if (str != null) {
	   //用旧的字符编码解码字符串。解码可能会出现异常。
	   byte[] bs = str.getBytes(oldCharset);
	   //用新的字符编码生成字符串
	   return new String(bs, newCharset);
	  }
	  return null;
	 }
	
	public static void main(String[] args) {
		// 定义即将访问的链接
		String url = "http://www.227226.com/rlist/20150103211817.htm";
		// 访问链接并获取页面内容
		String content = MarkSix.SendGet(url);
		// 根据正则匹配指定内容
		Pattern pattern;
		Matcher matcher;
		String marksix;
		// 匹配标题
		pattern = Pattern.compile("<!--msg-->(.+?)<!--endmsg-->");
		matcher = pattern.matcher(content);
		if (matcher.find()) {
			marksix = matcher.group(1);
		}else{
			return;
		}
		if(marksix.indexOf("<bR>") > 0){
			marksix = marksix.replace("<bR>"," \n");
		}else{
			marksix = marksix.replace("<br>"," \n");
		}
		String[] rets = marksix.split(" \n");
		// 打印结果
		for(String s:rets){
			System.out.println(s);
		}
		// 写入本地
		try {
			MarkSix.writeIntoFile(MarkSix.changeCharset(marksix,"GB2312","GBK"),"d:\\marksix.txt", false);
			System.out.println("记录成功写入文件marksix.txt");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
	}
	
}

