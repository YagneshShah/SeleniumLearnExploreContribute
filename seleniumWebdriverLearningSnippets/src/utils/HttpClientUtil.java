/*
 * Date: September 1st 2014
 * Author: Yagnesh Shah   
 * Twitter handle: @YagneshHShah
 * Organization: Moolya Software Testing Pvt Ltd
 * License Type: MIT
 */

package utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;
import java.net.URI;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.apache.commons.io.IOUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.AllowAllHostnameVerifier;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

public class HttpClientUtil {

	private HttpClientUtil() {
	}

	public static class ByteResponse {
		private int statusCode;
		private byte[] content;

		public ByteResponse(int statusCode, byte[] content) {
			super();
			this.statusCode = statusCode;
			if (content != null) {
				this.content = Arrays.copyOf(content, content.length);
			}
		}

		public int getStatusCode() {
			return statusCode;
		}

		public byte[] getContent() {
			return content;
		}
	}

	public static class StringResponse {
		private int statusCode;
		private String content;

		public StringResponse(int statusCode) {
			this(statusCode, null);
		}

		public StringResponse(int statusCode, String content) {
			super();
			this.statusCode = statusCode;
			this.content = content;
		}

		public int getStatusCode() {
			return statusCode;
		}

		public String getContent() {
			return content;
		}
	}

	public static byte[] post(String uri, byte[] data) {

		HttpClient client = new DefaultHttpClient();
		enableHttpsTunnelIfRequired(uri, client);

		HttpPost post = new HttpPost(uri);
		post.setEntity(new ByteArrayEntity(data));

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(post);
			return IOUtils.toByteArray(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static ByteResponse post2(String uri, byte[] data) {
		HttpClient client = new DefaultHttpClient();
		enableHttpsTunnelIfRequired(uri, client);
		HttpPost post = new HttpPost(uri);
		post.setEntity(new ByteArrayEntity(data));

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(post);
			return new ByteResponse(httpResponse.getStatusLine().getStatusCode(),
					IOUtils.toByteArray(httpResponse.getEntity().getContent()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static StringResponse post2(String uri, Map<String, String> headers, String data) {
		HttpClient client = new DefaultHttpClient();
		enableHttpsTunnelIfRequired(uri, client);

		HttpResponse httpResponse;
		try {
			HttpPost post = new HttpPost(uri);
			for (Map.Entry<String, String> header : headers.entrySet()) {
				post.setHeader(header.getKey(), header.getValue());
			}
			post.setEntity(new StringEntity(data, "UTF-8"));

			httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				return new StringResponse(httpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(httpResponse.getEntity()));
			}
			return new StringResponse(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static StringResponse post3(String uri, Map<String, String> headers, String data,String userName,String password) {
		DefaultHttpClient client = new DefaultHttpClient();
		
		HttpResponse httpResponse;
		try {
			enableHttpsTunnelIfRequired(uri, client);
			HttpPost post = new HttpPost(uri);
			for (Map.Entry<String, String> header : headers.entrySet()) {
				post.setHeader(header.getKey(), header.getValue());
			}

		URI uriObject = new URI(uri);
			client.getCredentialsProvider().setCredentials(new AuthScope(new HttpHost(uriObject.getHost())),
					new UsernamePasswordCredentials(userName, password));
			if(data!=null){
				post.setEntity(new StringEntity(data));
			}
			httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				return new StringResponse(httpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(httpResponse.getEntity()));
			}
			return new StringResponse(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static StringResponse post2(String uri,Map<String, String> headers, Map<String, String> data) {

		HttpClient client = new DefaultHttpClient();
		HttpResponse httpResponse;
		try {
			enableHttpsTunnelIfRequired(uri, client);
			HttpPost post = new HttpPost(uri);
			for (Map.Entry<String, String> header : headers.entrySet()) {
				post.setHeader(header.getKey(), header.getValue());
			}
			List<NameValuePair> nvps = new ArrayList<NameValuePair>();
			for (String paramName : data.keySet()) {
				nvps.add(new BasicNameValuePair(paramName, data.get(paramName)));
			}
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
			httpResponse = client.execute(post);
			return new StringResponse(httpResponse.getStatusLine()
					.getStatusCode(), EntityUtils.toString(httpResponse
					.getEntity()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static StringResponse post(String uri, String data) {

		HttpClient client = new DefaultHttpClient();
		enableHttpsTunnelIfRequired(uri, client);
		HttpPost post = new HttpPost(uri);

		// add the params to post request
		try {
			post.setEntity(new StringEntity(data));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"Error while adding post parameters to request :: ", e);
		}

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(post);
			return new StringResponse(httpResponse.getStatusLine()
					.getStatusCode(), EntityUtils.toString(httpResponse
					.getEntity()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	
	public static StringResponse post(String uri, String data, int connectionTimeout, int socketTimeout) 
			throws ConnectTimeoutException,SocketTimeoutException {

		HttpClient client = new DefaultHttpClient();
		HttpParams params = client.getParams();
		HttpConnectionParams.setConnectionTimeout(params,connectionTimeout);
		HttpConnectionParams.setSoTimeout(params,socketTimeout);
		enableHttpsTunnelIfRequired(uri, client);
		HttpPost post = new HttpPost(uri);

		// add the params to post request
		try {
			post.setEntity(new StringEntity(data));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"Error while adding post parameters to request :: ", e);
		}

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(post);
			return new StringResponse(httpResponse.getStatusLine()
					.getStatusCode(), EntityUtils.toString(httpResponse
					.getEntity()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static StringResponse post(String uri, Map<String, String> data) {

		HttpClient client = new DefaultHttpClient();
		enableHttpsTunnelIfRequired(uri, client);
		HttpPost post = new HttpPost(uri);

		List<NameValuePair> nvps = new ArrayList<NameValuePair>();
		for (String paramName : data.keySet()) {
			nvps.add(new BasicNameValuePair(paramName, data.get(paramName)));
		}

		// add the params to post request
		try {
			post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
		} catch (UnsupportedEncodingException e) {
			throw new RuntimeException(
					"Error while adding post parameters to request :: ", e);
		}

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(post);
			return new StringResponse(httpResponse.getStatusLine()
					.getStatusCode(), EntityUtils.toString(httpResponse
					.getEntity()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
//	public static StringResponse post(String uri, Map<String, Object> data) {
//		HttpClient client = new DefaultHttpClient();
//		HttpPost post = new HttpPost(uri);
//
//		HttpParams params = new BasicHttpParams();
//
//		for (String paramName : data.keySet()) {
//			Object value = data.get(paramName);
//			params.setParameter(paramName, value);
//		}
//
//		post.setParams(params);
//		HttpResponse httpResponse;
//		try {
//			httpResponse = client.execute(post);
//			return new StringResponse(httpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(httpResponse.getEntity()));
//		} catch (Exception e) {
//			throw new RuntimeException(e);
//		}
//	}

	/**
	 * enable an https channel for secure communications
	 * 
	 * @param client
	 */
	public static void enableHttpsTunnelIfRequired(String url, HttpClient client) {

		// enable https tunneling only for https urls
		if (!url.toLowerCase().startsWith("https"))
			return;

		X509TrustManager xtm = new X509TrustManager() {

			@Override
			public void checkClientTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				return;
			}

			@Override
			public void checkServerTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
				return;
			}

			@Override
			public X509Certificate[] getAcceptedIssuers() {
				return null;
			}
		};
		TrustManager mytm[] = { xtm };
		HostnameVerifier hv = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession sslSession) {
				return true;
			}
		};
		SSLContext ctx = null;
		try {
			ctx = SSLContext.getInstance("SSL");
			ctx.init(null, mytm, null);
			final org.apache.http.conn.ssl.SSLSocketFactory factory = new org.apache.http.conn.ssl.SSLSocketFactory(ctx,
					new AllowAllHostnameVerifier());
			final Scheme https = new Scheme("https", 443, factory);
			final SchemeRegistry schemeRegistry = client.getConnectionManager().getSchemeRegistry();
			schemeRegistry.register(https);

		} catch (Exception e) {
			throw new RuntimeException("Error during setting up of HTTPS channel ::", e);
		}
	}

	public static StringResponse delete(String uri, Map<String, Object> data) {
		HttpClient client = new DefaultHttpClient();
		HttpDelete delete = new HttpDelete(uri);

		HttpParams params = new BasicHttpParams();

		for (String paramName : data.keySet()) {
			Object value = data.get(paramName);
			params.setParameter(paramName, value);
		}
		delete.setParams(params);

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(delete);
			return new StringResponse(httpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(httpResponse.getEntity()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] get(String uri) {
		HttpClient client = new DefaultHttpClient();
		enableHttpsTunnelIfRequired(uri, client);
		HttpGet get = new HttpGet(uri);

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(get);
			return IOUtils.toByteArray(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * Gets the contents of URL that is protected by HTTP Basic Auth
	 * 
	 * @param uri
	 *            URI to fetch
	 * @param userName
	 *            HTTP Basic Auth - User Name
	 * @param password
	 *            HTTP Basic Auth - Password
	 * @return
	 */
	public static byte[] get(String uri, String userName, String password) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();

			URI uriObject = new URI(uri);
			client.getCredentialsProvider().setCredentials(new AuthScope(new HttpHost(uriObject.getHost())),
					new UsernamePasswordCredentials(userName, password));

			enableHttpsTunnelIfRequired(uri, client);
			HttpGet get = new HttpGet(uri);

			HttpResponse httpResponse;
			httpResponse = client.execute(get);
			return IOUtils.toByteArray(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static StringResponse get2(String uri,Map<String, String> headers, String userName, String password) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			enableHttpsTunnelIfRequired(uri, client);
			URI uriObject = new URI(uri);
			client.getCredentialsProvider().setCredentials(new AuthScope(new HttpHost(uriObject.getHost())),
					new UsernamePasswordCredentials(userName, password));

			
			HttpGet get = new HttpGet(uri);
			for (Map.Entry<String, String> header : headers.entrySet()) {
				get.setHeader(header.getKey(), header.getValue());
			}
			HttpResponse httpResponse;
			httpResponse = client.execute(get);
			return new StringResponse(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static StringResponse get3(String uri, Map<String, String> headers, String userName, String password) {
		try {
			DefaultHttpClient client = new DefaultHttpClient();
			enableHttpsTunnelIfRequired(uri, client);
			URI uriObject = new URI(uri);
			client.getCredentialsProvider().setCredentials(new AuthScope(new HttpHost(uriObject.getHost())),
					new UsernamePasswordCredentials(userName, password));

			
			HttpGet get = new HttpGet(uri);
			for (Map.Entry<String, String> header : headers.entrySet()) {
				get.setHeader(header.getKey(), header.getValue());
			}
			HttpResponse httpResponse;
			httpResponse = client.execute(get);
			
			return new StringResponse(httpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(httpResponse.getEntity()));
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	public static StringResponse get2(String uri) {
		HttpClient client = new DefaultHttpClient();
		enableHttpsTunnelIfRequired(uri, client);
		HttpGet get = new HttpGet(uri);

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(get);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				return new StringResponse(httpResponse.getStatusLine().getStatusCode(), EntityUtils.toString(httpResponse.getEntity()));
			}
			return new StringResponse(httpResponse.getStatusLine().getStatusCode());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] get(String uri, Map<String, Object> params) {
		HttpClient client = new DefaultHttpClient();
		HttpGet get = new HttpGet(uri);
		HttpParams parameters = new BasicHttpParams();
		for (String key : params.keySet()) {
			parameters.setParameter(key, params.get(key));
		}

		get.setParams(parameters);
		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(get);
			return IOUtils.toByteArray(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static byte[] post(String uri) {
		HttpClient client = new DefaultHttpClient();
		HttpPost post = new HttpPost(uri);

		HttpResponse httpResponse;
		try {
			httpResponse = client.execute(post);
			return IOUtils.toByteArray(httpResponse.getEntity().getContent());
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	public static void main(String[] args) {
		StringResponse res = get2("http://www.junk.com/fdc/fjhsjk");
		System.out.println(res.getStatusCode());
	}
	
//	public static HttpResponse sendRequest(String requestType, String body, String url, String...headers) throws Exception 
//	{
//	    try {
//
//	        HttpGet getRequest = null;
//	        HttpPost postRequest;
//	        HttpPut putRequest;
//	        HttpDelete delRequest;
//	        HttpResponse response = null;
//	        HttpClient client = new DefaultHttpClient();
//
//	        // Collecting Headers
//	        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
//	        for (String arg : headers) {
//
//	//Considering that you are applying header name and values in String format like this "Header1,Value1"
//
//	            nvps.add(new BasicNameValuePair(arg.split(",")[0], arg
//	                    .split(",")[1]));
//	        }
//	        System.out.println("Total Headers Supplied " + nvps.size());
//
//	        if (requestType.equalsIgnoreCase("GET")) {
//	            getRequest = new HttpGet(url);
//	            for (NameValuePair h : nvps) {
//	                getRequest.addHeader(h.getName(), h.getValue());
//	            }
//	            response = client.execute(getRequest);
//	        }
//
//	        if (requestType.equalsIgnoreCase("POST")) {
//	            postRequest = new HttpPost(url);
//	            for (NameValuePair h : nvps) {
//	                postRequest.addHeader(h.getName(), h.getValue());       
//	            }
//
//	            StringEntity requestEntity = new StringEntity(body,"UTF-8");
//	            postRequest.setEntity(requestEntity);
//	            response = client.execute(postRequest);
//	        }
//
//	        if (requestType.equalsIgnoreCase("PUT")) {
//	            putRequest = new HttpPut(url);
//	            for (NameValuePair h : nvps) {
//	                putRequest.addHeader(h.getName(), h.getValue());
//	            }
//	            StringEntity requestEntity = new StringEntity(body,"UTF-8");
//	            putRequest.setEntity(requestEntity);
//	            response = client.execute(putRequest);
//	        }
//
//	        if (requestType.equalsIgnoreCase("DELETE")) {
//	            delRequest = new HttpDelete(url);
//	            for (NameValuePair h : nvps) {
//	                delRequest.addHeader(h.getName(), h.getValue());
//	            }
//	            response = client.execute(delRequest);
//	        }
//
//	        return response;
//
//	    } catch (Exception e) {
//	        e.printStackTrace();
//	        throw e;
//	    }
//	}
	


}
