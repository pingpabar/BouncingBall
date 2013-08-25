package com.example.bouncingball;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

/**
 * Pelota que rebota por la pantalla y actúa al pulsarla.
 * 
 * Author: pingpabar
 */
public class BouncingBallActivity extends Activity {
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(new BouncingBallView(this, null, 0));
	}
}
