package com.hebg3.wl.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Enumeration;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import android.content.Context;
import android.util.Log;

import com.gheng.exhibit.utils.Constant;
import com.gj.gaojiaohui.abconstant.GloableConfig;
import com.smartdot.wenbo.huiyi.R;

public class zHttpUrlConnectionLoginLoader {
	/**
	 * 建立连接的超时值
	 */
	private static final int TIMEOUT = 30 * 1000;
	private String url;
	private String content;

	public void postDataFromSelf(ClientParams params, zOnEntityLoadCompleteListener<Object> listener, Context context) {
		HttpURLConnection connection = null;
		OutputStream os = null;
		InputStream is = null;
		try {
			url = params.domain + params.url;
			connection = (HttpURLConnection) new URL(url).openConnection();
			connection.setDoInput(true);
			connection.setDoOutput(true);
			connection.setUseCaches(false);// 不使用缓存
			connection.setRequestMethod("POST");
			
			try {
				connection.setRequestProperty("Cookie", Constant.getCookie(context, GloableConfig.domain));
			} catch (Exception e) {
				e.printStackTrace();
			}// 请求头加入cookie信息

			connection.setRequestProperty("charset", "UTF-8");
			connection.setConnectTimeout(TIMEOUT);
			connection.setReadTimeout(TIMEOUT);

			connection.connect();// 打开链接
			os = connection.getOutputStream();
			os.write(params.params.getBytes("UTF-8"));
			os.flush();

			int code = connection.getResponseCode();// 注意，进行ssl验证后，只有服务器返回code200，才能执行connection.getInputStream()，否则报错
			String msg = connection.getResponseMessage();

			switch (code) {
			case HttpURLConnection.HTTP_OK:// code 200 开始获取服务器响应数据流
				// 数据传递成功
				Constant.setCookie(context, GloableConfig.domain, connection);
				
				// 此处cookie有值,就代表稳了,记住只有登录后才会返回cookie
				String cookie = Constant.getCookie(context, GloableConfig.domain);
				
				is = connection.getInputStream();
				content = convertStreamToString(is);
				if (content.equals("")) {
 					listener.onError();
				} else {
					listener.onEntityLoadComplete(content);
				}
				break;
			default:// ssl验证失败或请求超时
				// 网络异常
				if (listener != null) {
					listener.onError();
				}
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			// 网络异常
			if (listener != null) {
				listener.onError();
			}
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onError();
					}
				}
				os = null;
			}
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					e.printStackTrace();
					if (listener != null) {
						listener.onError();
					}
				}
				is = null;
			}
			if (connection != null) {
				connection.disconnect();
				connection = null;
			}
		}
	}

	private String convertStreamToString(InputStream is) {
		StringBuilder sb = new StringBuilder();
		String line = null;
		try {
			BufferedReader br = new BufferedReader(new InputStreamReader(is));
			while ((line = br.readLine()) != null) {
				sb.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				is.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return sb.toString();
	}

	private static TrustManager[] createTrustManagers(Context cont) throws KeyStoreException, NoSuchAlgorithmException, CertificateException, IOException {

		KeyStore trustStore = KeyStore.getInstance("BKS");

		// ===============lyy修改证书
		trustStore.load(cont.getResources().openRawResource(R.raw.server), "password".toCharArray());

		printKeystoreInfo(trustStore);// for debug

		TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
		trustManagerFactory.init(trustStore);

		return trustManagerFactory.getTrustManagers();
	}

	private static void printKeystoreInfo(KeyStore keystore) throws KeyStoreException {
		// System.out.println("Provider : " + keystore.getProvider().getName());
		// System.out.println("Type : " + keystore.getType());
		// System.out.println("Size : " + keystore.size());

		Enumeration en = keystore.aliases();
		while (en.hasMoreElements()) {
			// System.out.println("Alias: " + en.nextElement());
		}
	}

	/**
	 * @author 马晓勇 ssl验证使用
	 */
	class NullHostNameVerifier implements HostnameVerifier {

		@Override
		public boolean verify(String hostname, SSLSession session) {
			Log.i("RestUtilImpl", "Approving certificate for " + hostname);
			return true;
		}
	}
}
