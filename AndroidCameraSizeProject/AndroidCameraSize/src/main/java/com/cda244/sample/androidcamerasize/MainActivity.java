package com.cda244.sample.androidcamerasize;

import android.app.Activity;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.widget.RelativeLayout;

public class MainActivity extends Activity
{

	private Camera mCamera;
	private SurfaceView mSurfaceView;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		mSurfaceView = (SurfaceView) findViewById(R.id.surface_view);

		SurfaceHolder holder = mSurfaceView.getHolder();
		//holder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		holder.addCallback(mSurfaceListener);
	}


	private SurfaceHolder.Callback mSurfaceListener = new SurfaceHolder.Callback()
	{
		@Override
		public void surfaceDestroyed(SurfaceHolder holder)
		{
			if (mCamera != null) {
				mCamera.stopPreview();
				mCamera.release();
				mCamera = null;
			}
		}


		@Override
		public void surfaceCreated(SurfaceHolder holder)
		{
			mCamera = Camera.open();
			try {
				mCamera.setPreviewDisplay(holder);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}


		@Override
		public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
		{
			mCamera.stopPreview();

			if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
				mCamera.setDisplayOrientation(90);
			}

			Camera.Parameters parameters = mCamera.getParameters();
			Camera.Size size = parameters.getSupportedPreviewSizes().get(0);
			parameters.setPreviewSize(size.width, size.height);

			setSurfaceSize(size.width, size.height);

			mCamera.setParameters(parameters);
			mCamera.startPreview();
		}


		private void setSurfaceSize( int previewW, int previewH )
		{
			float ratioW = (float) 400 / previewW;
			float ratioH = (float) 400 / previewH;
			float ratio = Math.max(ratioW, ratioH);

			int virtualW = Math.round(previewW * ratio);
			int virtualH = Math.round(previewH * ratio);

			RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) mSurfaceView.getLayoutParams();
			lp.width = virtualH;
			lp.height = virtualW;
			lp.leftMargin = (400 - lp.width);
			lp.topMargin = (400 - lp.height);
		}

	};

}
