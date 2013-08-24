package com.example.bouncingball;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * Author: pingpabar
 */
public class BouncingBallActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new BouncingBallView(this, null, 0));
	}
}
