package com.example.bouncingball;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * Author: pingpabar
 */
class BouncingBallView extends SurfaceView implements SurfaceHolder.Callback {
	// El atributo bbThread se utiliza para hacer referencia al hilo que controla la animaci�n
	private BouncingBallAnimationThread bbThread = null;

	// Atributos a la clase SurfaceView
	// Estos atributos definen las propiedades de la pelota - su posici�n actual (inicialmente en
	//  el centro de la pantalla), su direcci�n de movimiento (puedes experimentar probando
	//  diferentes valores), su tama�o y su color.
	private int xPosition = getWidth()/2;
	private int yPosition = getHeight()/2;
	private int xDirection = 20;
	private int yDirection = 40;
	private static int radius = 20;
	private static int ballColor = Color.RED;
	
	public BouncingBallView(Context ctx, AttributeSet attrs, int defStyle) {
		super(ctx, attrs, defStyle);

		// El constructor incluye un registro del escuchador de evento.
		getHolder().addCallback(this);
	}

	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);

		// Este m�todo define cada paso de la animaci�n. En primer lugar, se borra la pantalla
		//  pintando de negro y luego muestra la bola como un c�rculo en su ubicaci�n actual.
		Paint paint = new Paint();
		paint.setColor(Color.BLACK);
		canvas.drawRect(0, 0, getWidth(), getHeight(), paint);
		paint.setColor(ballColor);
		canvas.drawCircle(xPosition, yPosition, radius, paint);
	}

	/**
	 * El m�todo surfaceCreated() genera e inicia el hilo de control (a menos que ya exista) cuando la
	 *  superficie a la vista es creada
	 */
	public void surfaceCreated(SurfaceHolder holder) {
		if (bbThread != null)
			return;

		bbThread = new BouncingBallAnimationThread(getHolder());
		bbThread.start();
	}

	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
	}

	/**
	 * Detendr� el hilo cuando la superficie se destruye.
	 */
	public void surfaceDestroyed(SurfaceHolder holder) {
		bbThread.stop = true;
	}
	
	/**
	 * El usuario puede parar la pelota al tocar la pantalla. Un segundo toque en la pantalla pone
	 *  el bal�n en movimiento otra vez - a partir de su posici�n actual en la direcci�n del punto
	 *  de contacto.
	 *
	 * Recuerda que tocar una vista provoca la ejecuci�n del m�todo de la vista onTouchEvent(). La
	 *  pelota debe ser detenida, simplemente definiendo xDirectiony yDirection a cero, es decir,
	 *  el hilo debe seguir corriendo pero temporalmente no mover la pelota. Luego, la pelota se
	 *  reinicia asignando nuevos valores a las dos variables. Estas acciones s�lo deben llevarse
	 *  a cabo si el evento es de tipo MotionEvent.ACTION_DOWN.
	 */
	public boolean onTouchEvent (MotionEvent event) {
		 if (event.getAction() != MotionEvent.ACTION_DOWN) return false;

		 if (xDirection!=0 || yDirection!=0)
		    xDirection = yDirection = 0;
		  else {
		    xDirection = (int) event.getX() - xPosition;
		    yDirection = (int) event.getY() - yPosition;
		  }
	
		 return true;
	}

	
	/**
	 * Esto define las acciones del hilo animaci�n. El constructor inicializa el SurfaceHolder
	 *  con el valor del par�metro que viene de surfaceCreated() en BouncingBallView. Ser�
	 *  necesario para acceder a la SurfaceView. El bucle en el m�todo run() se ejecuta hasta que
	 *  es detenido por surfaceDestroyed() en BouncingBallView. En primer lugar, calcula la nueva
	 *  posici�n de la bola movi�ndola en la direcci�n dada por xDirection e yDirection. Las
	 *  declaraciones if hacen que rebote la pelota cuando se alcanza uno de los bordes de la
	 *  pantalla. Despu�s de estos c�lculos, el hilo se apodera del Canvas para dibujar y llama
	 *  al onDraw() de la vista para obtener una nueva salida. La llamada final a
	 *  unlockCanvasAndPost() hace que el nuevo gr�fico aparece en la pantalla.
	 */
	@SuppressLint("WrongCall")
	private class BouncingBallAnimationThread extends Thread {
		public boolean stop = false;
		private SurfaceHolder surfaceHolder;

		public BouncingBallAnimationThread(SurfaceHolder surfaceHolder) {
			this.surfaceHolder = surfaceHolder;
		}

		public void run() {
			while (!stop) {
				xPosition += xDirection;
				yPosition += yDirection;

				if (xPosition < 0) {
					xDirection = -xDirection;
					xPosition = radius;
				}

				if (xPosition > getWidth() - radius) {
					xDirection = -xDirection;
					xPosition = getWidth() - radius;
				}

				if (yPosition < 0) {
					yDirection = -yDirection;
					yPosition = radius;
				}

				if (yPosition > getHeight() - radius) {
					yDirection = -yDirection;
					yPosition = getHeight() - radius - 1;
				}

				Canvas c = null;

				try {
					c = surfaceHolder.lockCanvas(null);

					synchronized (surfaceHolder) {
						onDraw(c);
						//draw(c);
					}
				} finally {
					if (c != null)
						surfaceHolder.unlockCanvasAndPost(c);
				}
			}
		}
	}
}
