package com.git.gitsquare.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Validations {

	/*
	 * 
	 * Check Internet connection is available or not
	 * 
	 * return True if internet is connected
	 */
	public static boolean checkInternetConnection(Context context) {
		ConnectivityManager _connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		boolean _isConnected = false;
		NetworkInfo _activeNetwork = _connManager.getActiveNetworkInfo();
		if (_activeNetwork != null) {
			_isConnected = _activeNetwork.isConnectedOrConnecting();
		}

		return _isConnected;
	}


}
