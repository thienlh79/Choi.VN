package com.vgg.sdk;

import java.io.IOException;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.google.gson.Gson;

import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

public class HttpRequest {
	private static Handler 		mHandler 	= new Handler(Looper.getMainLooper());
	private static HttpProgressListener mProgressListener;
	
	private static class HttpRequestInt<DATA, CALLBACK extends ActionCallback<DATA>> implements Runnable {
		String 			url;
		RequestBody 	body;
		CALLBACK 		callback;
		Class<DATA> 	cls;
		HttpProgressListener progressListener;
		
		HttpRequestInt(String url, RequestBody body, Class<DATA> cls, CALLBACK callback) {
			this.body 		= body;
			this.callback 	= callback;
			this.cls 		= cls;
			this.url 		= url;
		}
		
		HttpRequestInt(String url, RequestBody body, Class<DATA> cls, CALLBACK callback, HttpProgressListener progressListener) {
			this.body 		= body;
			this.callback 	= callback;
			this.cls 		= cls;
			this.url 		= url;
			this.progressListener = progressListener;
		}
		
		void postResult(final DATA result) {
			if (callback != null) {
				mHandler.post(new Runnable() {
					
					@Override
					public void run() {
						callback.onAction(result);
					}
				});
			}
		}

		@Override
		public void run() {
			Response response = new com.vgg.sdk.HttpRequest(progressListener).request(url, body);
			DATA result = getData(response, cls);
			if (result != null && cls != null && ApiObject.class.isAssignableFrom(cls)) {
				Log.e("Found Api Call", url);
			}
			postResult(result);
		}
		
	}

	@SuppressWarnings("unchecked")
	public static <T> T getData(Response response, Class<T> cls) {
		try {
			String bodyStr = response.body().string();
			Log.d("Response Body", " " + bodyStr);
			if (cls == null || cls.isAssignableFrom(String.class)) {
				return (T) bodyStr;
			}
			return new Gson().fromJson(bodyStr, cls);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public static void requestString(String url, RequestBody body, ActionCallback<String> callback) {
		//new Thread(new HttpRequest<String, SdkResultCallback<String>>(url, body, callback)).start();
		request(url, body, String.class, callback);
	}
	
	public static void requestApi(String url, RequestBody body, ActionCallback<ApiObject> callback) {
		//new Thread(new HttpRequest<SdkApiObject, SdkResultCallback<SdkApiObject>>(url, body, SdkApiObject.class, callback)).start();
		request(url, body, ApiObject.class, callback);
	}
	
	public static <T, C extends ActionCallback<T>> void request(String url, RequestBody body, Class<T> cls, C callback) {
		new Thread(new HttpRequestInt(url, body, cls, callback)).start();
	}

	public static <T, C extends ActionCallback<T>> void request(String url, RequestBody body, Class<T> cls, C callback, HttpProgressListener progressListener) {
		new Thread(new HttpRequestInt(url, body, cls, callback, progressListener)).start();
	}

	public static void setProgressListener(HttpProgressListener listener) {
		mProgressListener = listener;
	}
	
	private static class ProgressResponseBody extends ResponseBody {

	    private final ResponseBody responseBody;
	    private final HttpProgressListener progressListener;
	    private BufferedSource bufferedSource;

	    public ProgressResponseBody(ResponseBody responseBody, HttpProgressListener progressListener) {
	      this.responseBody = responseBody;
	      this.progressListener = progressListener;
	    }

	    @Override public MediaType contentType() {
	      return responseBody.contentType();
	    }

	    @Override public long contentLength() {
	      return responseBody.contentLength();
	    }

	    @Override public BufferedSource source() {
	      if (bufferedSource == null) {
	        bufferedSource = Okio.buffer(source(responseBody.source()));
	      }
	      return bufferedSource;
	    }

	    private Source source(Source source) {
	      return new ForwardingSource(source) {
	        long totalBytesRead = 0L;

	        @Override public long read(Buffer sink, long byteCount) throws IOException {
	        	final long bytesRead = super.read(sink, byteCount);
	        	// read() returns the number of bytes read, or -1 if this source is exhausted.
	        	totalBytesRead += bytesRead != -1 ? bytesRead : 0;
	        	mHandler.post(new Runnable() {
				
	        		@Override
	        		public void run() {
	        			progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
	        		}
	        	});
	          
	        	return bytesRead;
	        }
	      };
	    }
	  }

	public interface HttpProgressListener {
	    void update(long bytesRead, long contentLength, boolean done);
	}
	
	private OkHttpClient mClient;
	private HttpProgressListener progressListener;
	
	public HttpRequest() {
		this(null);
	}
	
	public HttpRequest(HttpProgressListener listener) {
		progressListener = listener;
		if (progressListener == null) {
			progressListener = new HttpProgressListener() {
				
				@Override
				public void update(long bytesRead, long contentLength, boolean done) {
					Log.i("HttpRequest", String.format("Total %d, read %d, done %b", bytesRead, contentLength, done));
					if (mProgressListener != null) {
						mProgressListener.update(bytesRead, contentLength, done);
					}
				}
			};
		}
		mClient = new OkHttpClient.Builder()
	        .addNetworkInterceptor(new Interceptor() {
	            @Override public Response intercept(Chain chain) throws IOException {
	              Response originalResponse = chain.proceed(chain.request());
	              return originalResponse.newBuilder()
	                  .body(new ProgressResponseBody(originalResponse.body(), progressListener))
	                  .build();
	            }
	        })
	        .build();
	}
	
	public HttpRequest(String url, RequestBody body, Callback callback) {
		this();
		request(url, body, callback);
	}
	
	public void request(String url, RequestBody body, Callback callback) {
		Request request;
		if (body != null) {
			request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		} else {
			request = new Request.Builder()
				.url(url)
				.build();
		}
		mClient.newCall(request).enqueue(callback);
	}
	
	public Response request(String url, RequestBody body) {
		Request request;
		if (body != null) {
			request = new Request.Builder()
				.url(url)
				.post(body)
				.build();
		} else {
			request = new Request.Builder()
				.url(url)
				.build();
		}
		try {
			return mClient.newCall(request).execute();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
