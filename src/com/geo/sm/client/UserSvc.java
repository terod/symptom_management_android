/* 
 **
 ** Copyright 2014, Jules White
 **
 ** 
 */
package com.geo.sm.client;

import com.geo.sm.https.EasyHttpClient;
import com.geo.sm.oauth.SecuredRestBuilder;

import retrofit.RestAdapter.LogLevel;
import retrofit.client.ApacheClient;
import android.content.Context;
import android.content.Intent;

public class UserSvc {

	public static final String CLIENT_ID = "mobile";

	private static UserSvcApi videoSvc_;

	public static synchronized UserSvcApi getOrShowLogin(Context ctx) {
		if (videoSvc_ != null) {
			return videoSvc_;
		} else {
			Intent i = new Intent(ctx, LoginScreenActivity.class);
			ctx.startActivity(i);
			return null;
		}
	}

	public static synchronized UserSvcApi init(String server, String user,
			String pass) {

		videoSvc_ = new SecuredRestBuilder()
				.setLoginEndpoint(server + UserSvcApi.TOKEN_PATH)
				.setUsername(user).setPassword(pass).setClientId(CLIENT_ID)
				.setClient(new ApacheClient(new EasyHttpClient()))
				.setEndpoint(server).setLogLevel(LogLevel.FULL).build()
				.create(UserSvcApi.class);

		return videoSvc_;
	}
}
