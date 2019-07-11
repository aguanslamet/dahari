package com.ypyproductions.restaurants;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ypyproductions.abtractclass.fragment.IDBFragmentConstants;
import com.ypyproductions.restaurants.constanst.IMyRestaurantConstants;
import com.ypyproductions.utils.ApplicationUtils;
import com.ypyproductions.utils.DBLog;
import com.ypyproductions.utils.StringUtils;

/**
 * 
 *
 * @author:YPY Productions
 * @Skype: baopfiev_k50
 * @Mobile : +84 983 028 786
 * @Email: dotrungbao@gmail.com
 * @Website: www.ypyproductions.com
 * @Project:MyRestaurant
 * @Date:Jan 31, 2015 
 *
 */
public class ShowUrlActivity extends DBFragmentActivity implements IMyRestaurantConstants
	, IDBFragmentConstants {

	public static final String TAG = ShowUrlActivity.class.getSimpleName();

	private ProgressBar mProgressBar;
	private WebView mWebViewShowPage;

	private String mUrl;

	private String mNameHeader;

	private AdView adView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.setContentView(R.layout.layout_show_url);

		Intent args = getIntent();
		if (args != null) {
			mUrl = args.getStringExtra(KEY_URL);
			mNameHeader = args.getStringExtra(KEY_HEADER);
			DBLog.d(TAG, "===========>url=" + mUrl);
		}
		TextView mTvHeader = (TextView) findViewById(R.id.tv_header);
		if (!StringUtils.isEmptyString(mNameHeader)) {
			mTvHeader.setText(mNameHeader);
		}

		this.mProgressBar = (ProgressBar) findViewById(R.id.progressBar1);
		this.mProgressBar.setVisibility(View.VISIBLE);
		this.mWebViewShowPage = (WebView) findViewById(R.id.webview);
		this.mWebViewShowPage.getSettings().setJavaScriptEnabled(true);
		this.mWebViewShowPage.setWebViewClient(new WebViewClient() {
			@Override
			public void onPageFinished(WebView view, String url) {
				super.onPageFinished(view, url);
				mProgressBar.setVisibility(View.GONE);
			}
		});
		findViewById(R.id.btn_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		this.mWebViewShowPage.loadUrl(mUrl);
		this.setUpLayoutAdmob();
	}
	
	private void setUpLayoutAdmob() {
		RelativeLayout layout = (RelativeLayout) findViewById(R.id.layout_ad);
		if (ApplicationUtils.isOnline(this)) {
			if(SHOW_ADVERTISEMENT){
				adView = new AdView(this);
				adView.setAdUnitId(ADMOB_ID_BANNER);
				adView.setAdSize(AdSize.BANNER);
				
				layout.addView(adView);
				AdRequest mAdRequest = new AdRequest.Builder().build();
				adView.loadAd(mAdRequest);
			}
			else{
				layout.setVisibility(View.GONE);
			}
		}
		else {
			layout.setVisibility(View.GONE);
		}
	}
	

	@Override
	public void onDetachedFromWindow() {
		super.onDetachedFromWindow();
		if (mWebViewShowPage != null) {
			mWebViewShowPage.destroy();
		}
	}


	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mWebViewShowPage.canGoBack()) {
				mWebViewShowPage.goBack();
			}
			else {
				finish();
			}
			return true;
		}
		return super.onKeyDown(keyCode, event);
	}
}
