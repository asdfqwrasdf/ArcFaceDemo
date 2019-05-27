package com.arcsoft.sdk_demo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.arcsoft.face.AgeInfo;
import com.arcsoft.face.Face3DAngle;
import com.arcsoft.face.FaceEngine;
import com.arcsoft.face.FaceFeature;
import com.arcsoft.face.FaceInfo;
import com.arcsoft.face.FaceSimilar;
import com.arcsoft.face.GenderInfo;
import com.arcsoft.face.VersionInfo;
import com.guo.android_extend.GLES2Render;
import com.guo.android_extend.java.AbsLoop;
import com.guo.android_extend.java.ExtByteArrayOutputStream;
import com.guo.android_extend.tools.CameraHelper;
import com.guo.android_extend.widget.CameraFrameData;
import com.guo.android_extend.widget.CameraGLSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView;
import com.guo.android_extend.widget.CameraSurfaceView.OnCameraListener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.arcsoft.face.FaceEngine.ASF_AGE;
import static com.arcsoft.face.FaceEngine.ASF_FACE3DANGLE;
import static com.arcsoft.face.FaceEngine.ASF_GENDER;

/**
 * Created by gqj3375 on 2017/4/28.
 */

public class DetecterActivity extends AppCompatActivity implements OnCameraListener, View.OnTouchListener, Camera.AutoFocusCallback, View.OnClickListener {
	private final String TAG = this.getClass().getSimpleName();

	private int mWidth, mHeight, mFormat;
	private CameraSurfaceView mSurfaceView;
	private CameraGLSurfaceView mGLSurfaceView;
	private Camera mCamera;

	FaceEngine engine = new FaceEngine();
	List<FaceInfo> result = new ArrayList<>();
	List<AgeInfo> ages = new ArrayList<>();
	List<GenderInfo> genders = new ArrayList<>();
	List<Face3DAngle> angles = new ArrayList<>();

	int mCameraID;
	int mCameraRotate;
	int mCameraMirror;
	byte[] mImageNV21 = null;
	FRAbsLoop mFRAbsLoop = null;
	FaceInfo mAFT_FSDKFace = null;
	Handler mHandler;
	boolean isPostted = false;

	Runnable hide = new Runnable() {
		@Override
		public void run() {
			mTextView.setAlpha(0.5f);
			mImageView.setImageAlpha(128);
			isPostted = false;
		}
	};

	class FRAbsLoop extends AbsLoop {

		FaceEngine engine = new FaceEngine();
		FaceFeature result = new FaceFeature();
		List<FaceDB.FaceRegist> mResgist = ((Application)DetecterActivity.this.getApplicationContext()).mFaceDB.mRegister;
		List<FaceInfo> face1 = new ArrayList<>();
		List<FaceInfo> face2 = new ArrayList<>();
		
		@Override
		public void setup() {
			VersionInfo version = new VersionInfo();
			int error = engine.active(DetecterActivity.this, FaceDB.appid, FaceDB.sdk_key);
			Log.d(TAG, "active = " + error);
			error = engine.init(DetecterActivity.this, FaceEngine.ASF_DETECT_MODE_VIDEO, FaceEngine.ASF_OP_0_HIGHER_EXT, 16, 1,
				FaceEngine.ASF_FACE_RECOGNITION|FaceEngine.ASF_AGE|FaceEngine.ASF_GENDER|FaceEngine.ASF_FACE3DANGLE);
			Log.d(TAG, "init = " + error);
			error = engine.getVersion(version);
			Log.d(TAG, "getVersion=" + version.toString() + "," + error); //(210, 178 - 478, 446), degree = 1　780, 2208 - 1942, 3370
		}

		@Override
		public void loop() {
			if (mImageNV21 != null) {
				final int rotate = mCameraRotate;
				long time = System.currentTimeMillis();
				int error = engine.extractFaceFeature(mImageNV21, mWidth, mHeight, FaceEngine.CP_PAF_NV21, mAFT_FSDKFace, result);
				Log.d(TAG, "AFR_FSDK_ExtractFRFeature cost :" + (System.currentTimeMillis() - time) + "ms,err=" + error);
				Log.d(TAG, "Face=" + result.getFeatureData()[0] + "," + result.getFeatureData()[1] + "," + result.getFeatureData()[2] + "," + error);
				FaceSimilar score = new FaceSimilar();
				float max = 0.0f;
				String name = null;
				for (FaceDB.FaceRegist fr : mResgist) {
					for (FaceFeature face : fr.mFaceList.values()) {
						error = engine.compareFaceFeature(result, face, score);
						Log.d(TAG,  "Score:" + score.getScore() + ", AFR_FSDK_FacePairMatching=" + error);
						if (max < score.getScore()) {
							max = score.getScore();
							name = fr.mName;
						}
					}
				}

				//age & gender
				face1.clear();
				face1.add(new FaceInfo(mAFT_FSDKFace));
				int error1 = engine.process(mImageNV21, mWidth, mHeight, FaceEngine.CP_PAF_NV21, face1, ASF_AGE|ASF_GENDER);
				Log.d(TAG, "process:" + error1);
				error1 = engine.getAge(ages);
				Log.d(TAG, "getAge:" + error1);
				int error2 = engine.getGender(genders);
				Log.d(TAG, "getGender:" + error2);
				final String age, gender, live;
				if (error1 == 0) {
					age = ages.get(0).getAge() == 0 ? "年龄未知" : ages.get(0).getAge() + "岁";
				} else {
					age = "age error:"+error;
				}
				if (error2 == 0) {
					gender = genders.get(0).getGender() == -1 ? "性别未知" : (genders.get(0).getGender() == 0 ? "男" : "女");
				} else {
					gender = "gender error:"+error2;
				}
				//3D FACE
				face1.clear();
				face1.add(new FaceInfo(mAFT_FSDKFace));
				int error3 = engine.process(mImageNV21, mWidth, mHeight, FaceEngine.CP_PAF_NV21, face1, ASF_FACE3DANGLE);
				Log.d(TAG, "process:" + error3);
				int error4 = engine.getFace3DAngle(angles);
				Log.d(TAG, "getFace3DAngle:" + error4);
				//Log.d(TAG, "age:" + ages.get(0).getAge() + ",gender:" + genders.get(0).getGender());
				final String angle;
				if (error4 == 0) {
					angle = angles.isEmpty() ? "角度未知" : "Roll="+ angles.get(0).getRoll()+ ",\r\nYaw=" + angles.get(0).getYaw()
										+ ",\r\nPitch="+angles.get(0).getPitch() + " \r\n" ;
				} else {
					angle = "Face3DAngle error:"+error4;
				}

				Rect corp = new Rect();
				corp.left = Math.max(0, mAFT_FSDKFace.getRect().left);
				corp.top = Math.max(0, mAFT_FSDKFace.getRect().top);
				corp.right = Math.min(mWidth, mAFT_FSDKFace.getRect().right);
				corp.bottom = Math.min(mHeight, mAFT_FSDKFace.getRect().bottom);
				//crop
				byte[] data = mImageNV21;
				YuvImage yuv = new YuvImage(data, ImageFormat.NV21, mWidth, mHeight, null);
				ExtByteArrayOutputStream ops = new ExtByteArrayOutputStream();
				yuv.compressToJpeg(corp, 80, ops);
				final Bitmap bmp = BitmapFactory.decodeByteArray(ops.getByteArray(), 0, ops.getByteArray().length);
				try {
					ops.close();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if (max > 0.6f) {
					//fr success.
					final float max_score = max;
					Log.d(TAG, "fit Score:" + max + ", NAME:" + name);
					final String mNameShow = name;
					mHandler.removeCallbacks(hide);
					mHandler.post(new Runnable() {
						@Override
						public void run() {
							mTextView.setAlpha(1.0f);
							mTextView.setText(mNameShow);
							mTextView.setTextColor(Color.RED);
							mTextView1.setVisibility(View.VISIBLE);
							mTextView1.setText("置信度：" + (float)((int)(max_score * 1000)) / 1000.0);
							mTextView1.setTextColor(Color.RED);
							mTextView2.setVisibility(View.VISIBLE);
							mTextView2.setText("人脸角度:" + angle);
							mTextView2.setTextColor(Color.RED);
							mImageView.setRotation(rotate);
							mImageView.setScaleY(-mCameraMirror);
							mImageView.setImageAlpha(255);
							mImageView.setImageBitmap(bmp);
						}
					});
				} else {
					final String mNameShow = "未识别";
					DetecterActivity.this.runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mTextView.setAlpha(1.0f);
							mTextView1.setVisibility(View.VISIBLE);
							mTextView1.setText( gender + "," + age);
							mTextView1.setTextColor(Color.RED);
							mTextView2.setVisibility(View.VISIBLE);
							mTextView2.setText("人脸角度:" + angle);
							mTextView2.setTextColor(Color.RED);
							mTextView.setText(mNameShow);
							mTextView.setTextColor(Color.RED);
							mImageView.setImageAlpha(255);
							mImageView.setRotation(rotate);
							mImageView.setScaleY(-mCameraMirror);
							mImageView.setImageBitmap(bmp);
						}
					});
				}
				mImageNV21 = null;
			}

		}

		@Override
		public void over() {
			int error = engine.unInit();
			Log.d(TAG, "unInit : " + error);
		}
	}

	private TextView mTextView;
	private TextView mTextView1;
	private TextView mTextView2;
	private ImageView mImageView;
	private ImageButton mImageButton;

	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);

		mCameraID = getIntent().getIntExtra("Camera", 0) == 0 ? Camera.CameraInfo.CAMERA_FACING_BACK : Camera.CameraInfo.CAMERA_FACING_FRONT;
		mCameraRotate = getIntent().getIntExtra("Camera", 0) == 0 ? 90 : 270;
		mCameraMirror = getIntent().getIntExtra("Camera", 0) == 0 ? GLES2Render.MIRROR_NONE : GLES2Render.MIRROR_X;
		mWidth = 1280;
		mHeight = 960;
		mFormat = ImageFormat.NV21;
		mHandler = new Handler();

		setContentView(R.layout.activity_camera);
		mGLSurfaceView = (CameraGLSurfaceView) findViewById(R.id.glsurfaceView);
		mGLSurfaceView.setOnTouchListener(this);
		mSurfaceView = (CameraSurfaceView) findViewById(R.id.surfaceView);
		mSurfaceView.setOnCameraListener(this);
		mSurfaceView.setupGLSurafceView(mGLSurfaceView, true, mCameraMirror, mCameraRotate);
		mSurfaceView.debug_print_fps(true, false);

		//snap
		mTextView = (TextView) findViewById(R.id.textView);
		mTextView.setText("");
		mTextView1 = (TextView) findViewById(R.id.textView1);
		mTextView1.setText("");
		mTextView2 = (TextView) findViewById(R.id.textView2);
		mTextView2.setText("");

		mImageView = (ImageView) findViewById(R.id.imageView);
		mImageButton = (ImageButton) findViewById(R.id.imageButton);
		mImageButton.setOnClickListener(this);

		int err = engine.active(DetecterActivity.this, FaceDB.appid, FaceDB.sdk_key);
		Log.d(TAG, "active =" + err);
		err = engine.init(DetecterActivity.this, FaceEngine.ASF_DETECT_MODE_VIDEO, FaceEngine.ASF_OP_0_HIGHER_EXT, 16, 1, FaceEngine.ASF_FACE_DETECT );
		Log.d(TAG, "init =" + err);

		mFRAbsLoop = new FRAbsLoop();
		mFRAbsLoop.start();
	}

	/* (non-Javadoc)
	 * @see android.app.Activity#onDestroy()
	 */
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		mFRAbsLoop.shutdown();
		int err = engine.unInit();
		Log.d(TAG, "unInit =" + err);
	}

	@Override
	public Camera setupCamera() {
		// TODO Auto-generated method stub
		mCamera = Camera.open(mCameraID);
		try {
			Camera.Parameters parameters = mCamera.getParameters();
			parameters.setPreviewSize(mWidth, mHeight);
			parameters.setPreviewFormat(mFormat);

			for( Camera.Size size : parameters.getSupportedPreviewSizes()) {
				Log.d(TAG, "SIZE:" + size.width + "x" + size.height);
			}
			for( Integer format : parameters.getSupportedPreviewFormats()) {
				Log.d(TAG, "FORMAT:" + format);
			}

			List<int[]> fps = parameters.getSupportedPreviewFpsRange();
			for(int[] count : fps) {
				Log.d(TAG, "T:");
				for (int data : count) {
					Log.d(TAG, "V=" + data);
				}
			}
			//parameters.setPreviewFpsRange(15000, 30000);
			//parameters.setExposureCompensation(parameters.getMaxExposureCompensation());
			//parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
			//parameters.setAntibanding(Camera.Parameters.ANTIBANDING_AUTO);
			//parmeters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
			//parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
			//parameters.setColorEffect(Camera.Parameters.EFFECT_NONE);
			mCamera.setParameters(parameters);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (mCamera != null) {
			mWidth = mCamera.getParameters().getPreviewSize().width;
			mHeight = mCamera.getParameters().getPreviewSize().height;
		}
		return mCamera;
	}

	@Override
	public void setupChanged(int format, int width, int height) {

	}

	@Override
	public boolean startPreviewImmediately() {
		return true;
	}

	@Override
	public Object onPreview(byte[] data, int width, int height, int format, long timestamp) {
		int err = engine.detectFaces(data, width, height, FaceEngine.CP_PAF_NV21, result);
		Log.d(TAG, "AFT_FSDK_FaceFeatureDetect =" + err);
		Log.d(TAG, "Face=" + result.size());
		for (FaceInfo face : result) {
			Log.d(TAG, "Face:" + face.toString());
		}
		if (mImageNV21 == null) {
			if (!result.isEmpty()) {
				mAFT_FSDKFace = result.get(0).clone();
				mImageNV21 = data.clone();
			} else {
				if (!isPostted) {
					mHandler.removeCallbacks(hide);
					mHandler.postDelayed(hide, 2000);
					isPostted = true;
				}
			}
		}
		//copy rects
		Rect[] rects = new Rect[result.size()];
		for (int i = 0; i < result.size(); i++) {
			rects[i] = new Rect(result.get(i).getRect());
		}
		//clear result.
		result.clear();
		//return the rects for render.
		return rects;
	}

	@Override
	public void onBeforeRender(CameraFrameData data) {

	}

	@Override
	public void onAfterRender(CameraFrameData data) {
		mGLSurfaceView.getGLES2Render().draw_rect((Rect[])data.getParams(), Color.GREEN, 2);
	}

	@Override
	public boolean onTouch(View v, MotionEvent event) {
		CameraHelper.touchFocus(mCamera, event, v, this);
		return false;
	}

	@Override
	public void onAutoFocus(boolean success, Camera camera) {
		if (success) {
			Log.d(TAG, "Camera Focus SUCCESS!");
		}
	}

	@Override
	public void onClick(View view) {
		if (view.getId() == R.id.imageButton) {
			if (mCameraID == Camera.CameraInfo.CAMERA_FACING_BACK) {
				mCameraID = Camera.CameraInfo.CAMERA_FACING_FRONT;
				mCameraRotate = 270;
				mCameraMirror = GLES2Render.MIRROR_X;
			} else {
				mCameraID = Camera.CameraInfo.CAMERA_FACING_BACK;
				mCameraRotate = 90;
				mCameraMirror = GLES2Render.MIRROR_NONE;
			}
			mSurfaceView.resetCamera();
			mGLSurfaceView.setRenderConfig(mCameraRotate, mCameraMirror);
			mGLSurfaceView.getGLES2Render().setViewDisplay(mCameraMirror, mCameraRotate);
		}
	}

}
