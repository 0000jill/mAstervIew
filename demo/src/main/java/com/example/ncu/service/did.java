package com.example.ncu.service;

import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// key: bmN1MTExMDJwcm9qZWN0dGVhbTEyQGdtYWlsLmNvbQ:K8RmRiUmeV6YQ51RO7xPb
public class did {
	
    // 生成影片 會回傳影片的網址
	public static String create_talk(String textToBeSent) {
		OkHttpClient client = new OkHttpClient();

		MediaType mediaType = MediaType.parse("application/json");
    
		JSONObject scriptObject = new JSONObject();
		scriptObject.put("type", "text");
		scriptObject.put("subtitles", "false");
		JSONObject providerObject = new JSONObject();
		providerObject.put("type", "microsoft");
		providerObject.put("voice_id", "zh-TW-YunJheNeural");
		scriptObject.put("provider", providerObject);
		scriptObject.put("ssml", "false");
		scriptObject.put("input", textToBeSent);
    
		JSONObject configObject = new JSONObject();
		configObject.put("fluent", "true");
		configObject.put("pad_audio", "0.3");
		configObject.put("stitch", "true");

		JSONObject requestBodyObject = new JSONObject();
		requestBodyObject.put("script", scriptObject);
		requestBodyObject.put("config", configObject);
		requestBodyObject.put("source_url", "https://clips-presenters.d-id.com/rian/image.png");

		String requestBodyString = requestBodyObject.toString();

		RequestBody body = RequestBody.create(mediaType, requestBodyString);
		Request request = new Request.Builder()
				.url("https://api.d-id.com/talks")
				.post(body)
				.addHeader("accept", "application/json")
				.addHeader("content-type", "application/json")
				.addHeader("authorization", "Basic Ym1OMU1URXhNREp3Y205cVpXTjBkR1ZoYlRFeVFHZHRZV2xzTG1OdmJROjZNQ09OdHNKTUo4TjNaZWU0QlFpbw==")
				.build();
		try {
			System.out.println("開始生成影片");
			Response response = client.newCall(request).execute();
			String talk = response.body().string();
			System.out.println("D-ID的回傳: "+response+talk); // response = Response{protocol=h2, code=201, message=, url=https://api.d-id.com/talks}
			int statusCode = response.code();

			if (statusCode == 500) {
				throw new RuntimeException("Internal Server Error");
			}
			else {
				String talkId = get_id(talk); // 從talk取出id
				return talkId;}  // 回傳talkId給generateVideo()
			} catch (IOException e) {
				// 在這裡處理 IOException 異常
				e.printStackTrace();
				System.out.println(e);
				return e.getMessage();
			}finally {
			    // 关闭OkHttpClient以确保资源正确释放
				client.dispatcher().executorService().shutdown();
			}

	}
	
	
	// 叫特定的影片 會回傳result_url給create_talk
	public static String get_talk(String videoId) {
		OkHttpClient client = new OkHttpClient();
			
		Request request = new Request.Builder()
				.url("https://api.d-id.com/talks/"+videoId)
				.get()
				.addHeader("accept", "application/json")
				.addHeader("authorization", "Basic Ym1OMU1URXhNREp3Y205cVpXTjBkR1ZoYlRFeVFHZHRZV2xzTG1OdmJROjZNQ09OdHNKTUo4TjNaZWU0QlFpbw==")
				.build();
		try {
			System.out.println("開始獲取result_url");
			Response response = client.newCall(request).execute();
			String geturl = response.body().string();
			System.out.println("D-ID的回傳: "+response+geturl);// response = Response{protocol=h2, code=200, message=, url=https://api.d-id.com/talks/tlk_8DpQmYJGUIhgJJt67doAy}
			// 檢查是否有 pending_url，如果有則等待一段時間再重新呼叫 API 或執行其他操作
	        if (geturl.contains("pending_url")) {
	            System.out.println("影片尚未生成完畢 先等待10秒 再進行下個步驟");
	            try {
	                TimeUnit.SECONDS.sleep(10); // 等待 10 秒
	            } catch (InterruptedException e) {
	                e.printStackTrace();
	            }

	            // 重新呼叫 API 或執行其他操作
	            System.out.println("再次呼叫get_talk");
	            return get_talk(videoId); // 重新呼叫 get_talk 方法
	        } else {
	            // 影片處理完成，可以繼續進行下一步操作
	            String escapedJsonString = geturl.replaceAll("/", "\\\\/"); // 因為geturl裡有// java會誤認為註解 要做轉義
	            return get_result(escapedJsonString); // 回傳 result_url 給 create_talk 
	        }

		} catch (IOException e) {
			// 在這裡處理 IOException 異常
			e.printStackTrace();
			System.out.println(e);
			return e.getMessage();
	    }finally {
		    // 关闭OkHttpClient以确保资源正确释放
			client.dispatcher().executorService().shutdown();
		}
	
	}
	
	// 只取出create_talk生成的id字串
	public static String get_id(String jsonString) {

		JSONObject jsonObject = new JSONObject(jsonString);
	    String id = jsonObject.getString("id");
	    System.out.println("取出的id: "+id);
	    return id;  // 回傳範例:tlk_8DpQmYJGUIhgJJt67doAy
	}
	// 只取出get_talk回傳的result_url字串
	public static String get_result(String jsonString) {
	    	
	    JSONObject jsonObject = new JSONObject(jsonString);
	    String result = jsonObject.getString("result_url");
	    System.out.println("取出的result_url: "+result);
	    return result;
	        
	}

	
	// 叫此帳號所有生成過的影片
	public static void get_talks() {
		OkHttpClient client = new OkHttpClient();
			
		Request request = new Request.Builder()
				  .url("https://api.d-id.com/talks")
				  .get()
				  .addHeader("accept", "application/json")
				  .addHeader("authorization", "Basic Ym1OMU1URXhNREp3Y205cVpXTjBkR1ZoYlRFeVFHZHRZV2xzTG1OdmJROjZNQ09OdHNKTUo4TjNaZWU0QlFpbw==")
				  .build();
		try {
			System.out.println("開始獲取result_url");
			Response response = client.newCall(request).execute();
			String geturl = response.body().string();
			System.out.println(response+geturl);// response = Response{protocol=h2, code=200, message=, url=https://api.d-id.com/talks/tlk_8DpQmYJGUIhgJJt67doAy}

		} catch (IOException e) {
			// 在這裡處理 IOException 異常
			e.printStackTrace();
			System.out.println(e);
	    }finally {
		    // 关闭OkHttpClient以确保资源正确释放
			client.dispatcher().executorService().shutdown();
		}
	
	}
	
	public static void main(String[] args) throws IOException {
		//String videoId = get_id("{\"id\":\"tlk_8DpQmYJGUIhgJJt67doAy\",\"created_at\":\"2023-08-03T09:14:45.806Z\",\r\n"
		//		+ "	    \"created_by\":\"auth0|643d47d5ae1dd5a7069cb1a9\",\"status\":\"created\",\"object\":\"talk\"}");
	    //get_talk(videoId); 
		//get_talks();
		//get_talk("tlk_P4fOJZXsEwLk2Zm-gK-vo");
		//get_actor();
	}
	
}
