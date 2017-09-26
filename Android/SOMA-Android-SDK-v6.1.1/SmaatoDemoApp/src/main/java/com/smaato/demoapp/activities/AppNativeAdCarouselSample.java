package com.smaato.demoapp.activities;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.smaato.demoapp.R;
import com.smaato.demoapp.utils.Constants;
import com.smaato.soma.AdDownloaderInterface;
import com.smaato.soma.AdListenerInterface;
import com.smaato.soma.ErrorCode;
import com.smaato.soma.ReceivedBannerInterface;
import com.smaato.soma.exception.AdReceiveFailed;
import com.smaato.soma.nativead.NativeAd;

public class AppNativeAdCarouselSample extends Activity implements OnClickListener
		 {
	NativeAd nativeAd;
	RelativeLayout nativeRelativeLayout;
	RelativeLayout nativeRelativeLayout2;
	NativeAd nativeAd2;

	Button reloadBanner;

	static String TAG= AppNativeAdCarouselSample.class.getSimpleName();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.app_native_ad_carousel_sample);
		reloadBanner = (Button) findViewById(R.id.load_ad );
		reloadBanner.setOnClickListener(this);
		nativeAd = new NativeAd(getApplicationContext());
		nativeRelativeLayout = (RelativeLayout) findViewById(R.id.nativeAdLayout);

		SharedPreferences prefs = this.getSharedPreferences(Constants.COM_SMAATO_DEMOAPP,
				Context.MODE_PRIVATE);
		nativeAd.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		nativeAd.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));
		nativeAd.setMainLayout(nativeRelativeLayout);

		nativeAd2 = new NativeAd(getApplicationContext());
		nativeRelativeLayout2 = (RelativeLayout) findViewById(R.id.nativeAdLayout2);
		nativeAd2.setAdListener(new AdListenerInterface() {
			@Override
			public void onReceiveAd(AdDownloaderInterface sender, ReceivedBannerInterface receivedBanner) throws AdReceiveFailed {
				if (receivedBanner.getErrorCode() != ErrorCode.NO_ERROR) {
					Toast.makeText(getApplicationContext(), "2nd Req failed" + receivedBanner.getErrorMessage(), Toast.LENGTH_SHORT).show();
				}
			}
		});


		nativeAd2.getAdSettings().setPublisherId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.PUBLISHER_ID, "0")));
		nativeAd2.getAdSettings().setAdspaceId(Integer.parseInt(prefs.getString(Constants.COM_SMAATO_DEMOAPP + Constants.AD_SPACE_ID, "0")));
		nativeAd2.setMainLayout(nativeRelativeLayout2);

		requestNewAd();
		requestNewSecondAd();

	}

	@Override
	public void onClick(View v) {
		requestNewAd();
	}

	public void requestNewAd() {
		nativeAd.asyncLoadNativeType(NativeAd.NativeType.CAROUSEL, nativeAdTypeListener);
	}


	NativeAd.NativeAdTypeListener nativeAdTypeListener = new NativeAd.NativeAdTypeListener() {
		@Override
		public void onError(ErrorCode errorCode, String errorMessage) {
			Log.e(TAG, "" + errorCode + " " + errorMessage);
		}

		@Override
		public void onAdResponse(ViewGroup nativeView) {
			if (nativeView != null) {

				if(nativeAd.getTextView()!=null){
					nativeAd.getTextView().setTextColor(Color.BLUE);
					nativeAd.getTextView().setTextSize(10);
				}

				if(nativeAd.getTitleView()!=null){
					nativeAd.getTitleView().setTextColor(Color.BLUE);
				}

				// IMPORTANT step to be invoked after attaching ad response to Screen.
				// IMPORTANT FOR IMPRESSIONS COUNTS
				nativeAd.bindAdResponse(nativeView);
			}
		}
	};

	public void requestNewSecondAd() {

		nativeAd2.asyncLoadNativeType(NativeAd.NativeType.CAROUSEL, new NativeAd.NativeAdTypeListener() {
			@Override
			public void onError(ErrorCode errorCode, String errorMessage) {
				Log.e(TAG, "" + errorCode + " " + errorMessage);
			}

			@Override
			public void onAdResponse(ViewGroup nativeView) {
				if (nativeView != null) {

					if(nativeAd2.getTextView()!=null){
						nativeAd2.getTextView().setTextColor(Color.BLUE);
						nativeAd2.getTextView().setTextSize(10);
					}

					if(nativeAd2.getTitleView()!=null){
						nativeAd2.getTitleView().setTextColor(Color.BLUE);
					}


					// IMPORTANT step to be invoked after attaching ad response to Screen.
					// IMPORTANT FOR IMPRESSIONS COUNTS
					nativeAd2.bindAdResponse(nativeView);
				}
			}
		});

	}

}