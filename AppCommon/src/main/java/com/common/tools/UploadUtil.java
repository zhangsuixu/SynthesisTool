package com.common.tools;

import android.util.Log;
import android.widget.Toast;

import com.common.base.BaseApp;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.UUID;

/**
 * 上传工具类
 */
public class UploadUtil {
	private static final String TAG = "uploadFile";
	private static final int TIME_OUT = 100 * 1000; // 超时时间
	private static final String CHARSET = "utf-8"; // 设置编码

	/**
	 * android上传文件到服务器
	 * 
	 * @param file
	 *           需要上传的文件
	 * @param RequestURL
	 *          请求的rul
	 * @return
	 * 			返回响应的内容
	 */
	public String uploadFile(File file, String RequestURL) {
		String result = null;
		String BOUNDARY = UUID.randomUUID().toString(); // 边界标识 随机生成
		String PREFIX = "--", LINE_END = "\r\n";
		String CONTENT_TYPE = "multipart/form-data"; // 内容类型

		try {
			URL url = new URL(RequestURL);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setReadTimeout(TIME_OUT);
			conn.setConnectTimeout(TIME_OUT);
			conn.setDoInput(true); // 允许输入流
			conn.setDoOutput(true); // 允许输出流
			conn.setUseCaches(false); // 不允许使用缓存
			conn.setRequestMethod("POST"); // 请求方式
			conn.setRequestProperty("Charset", CHARSET); // 设置编码
			conn.setRequestProperty("connection", "keep-alive");
			conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY);

			if (file != null) {
				DataOutputStream dos = new DataOutputStream(conn.getOutputStream()); //当文件不为空，把文件包装并且上传
				dos.write(new StringBuffer()
						.append(PREFIX)
						.append(BOUNDARY)
						.append(LINE_END)
						.append("Content-Disposition: form-data; name=\"img\"; filename=\"" + file.getName() + "\"" + LINE_END) //这里重点注意： name里面的值为服务器端需要key 只有这个key 才可以得到对应的文件filename是文件的名字，包含后缀名的 比如:abc.png
						.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINE_END)
						.append(LINE_END)
						.toString()
						.getBytes());
				InputStream is = new FileInputStream(file);
				byte[] bytes = new byte[1024 * 1024];
				int len = 0;
				while ((len = is.read(bytes)) != -1) {
					dos.write(bytes, 0, len);
				}
				is.close();
				dos.write(LINE_END.getBytes());
				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
				dos.write(end_data);
				dos.flush();
				/**
				 * 获取响应码 200=成功 当响应成功，获取响应的流
				 */
				int res = conn.getResponseCode();
				if(res == 200){
					BaseApp.getHandler().post(new Runnable() {
						@Override
						public void run() {
							Toast.makeText(BaseApp.getContext(),"上传文件成功",Toast.LENGTH_LONG).show();
						}
					});
				}
				Log.e(TAG, "response code:" + res);
				Log.e(TAG, "request success");
				InputStream input = conn.getInputStream();
				StringBuffer sb1 = new StringBuffer();
				int ss;
				while ((ss = input.read()) != -1) {
					sb1.append((char) ss);
				}
				result = sb1.toString();
				result = new String(result.getBytes("iso8859-1"), "utf-8");
				Log.e(TAG, "result : " + result);

			}
		} catch (MalformedURLException e) {
			e.printStackTrace();
			return null;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
		return result;
	}
}