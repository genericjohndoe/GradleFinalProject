package com.udacity.gradle.builditbigger.Camera;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CameraMetadata;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.util.Size;
import android.util.SparseIntArray;
import android.view.Surface;
import android.view.TextureView;
import android.widget.Toast;

import com.udacity.gradle.builditbigger.NewPost.VisualMediaPost.VisualMediaPostFragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * class enables camera functionality
 */

public class LifeCycleCamera implements LifecycleObserver, ActivityCompat.OnRequestPermissionsResultCallback {
  //photo only variables
  private static final int REQUEST_CAMERA_PERMISSION = 1;
  private static final int STATE_PREVIEW = 0;
  private static final int STATE_WAITING_LOCK = 1;
  private static final int STATE_WAITING_PRECAPTURE = 2;
  private static final int STATE_WAITING_NON_PRECAPTURE = 3;
  private static final int STATE_PICTURE_TAKEN = 4;
  private static final int MAX_PREVIEW_WIDTH = 1920;
  private static final int MAX_PREVIEW_HEIGHT = 1080;
  private ImageReader mImageReader;
  private final ImageReader.OnImageAvailableListener mOnImageAvailableListener
          = new ImageReader.OnImageAvailableListener() {

    @Override
    public void onImageAvailable(ImageReader reader) {
      mBackgroundHandler.post(new ImageSaver(reader.acquireNextImage(), mFile));
    }

  };
  private CaptureRequest mPreviewRequest; //CaptureRequest.Builder.build() called in v
  private int mState = STATE_PREVIEW;
  private boolean mFlashSupported;
  private CameraCaptureSession.CaptureCallback mCaptureCallback
          = new CameraCaptureSession.CaptureCallback() {

    private void process(CaptureResult result) {
      switch (mState) {
        case STATE_PREVIEW: {
          // We have nothing to do when the camera preview is working normally.
          break;
        }
        case STATE_WAITING_LOCK: {
          Integer afState = result.get(CaptureResult.CONTROL_AF_STATE);
          if (afState == null) {
            captureStillPicture();
          } else if (CaptureResult.CONTROL_AF_STATE_FOCUSED_LOCKED == afState ||
                  CaptureResult.CONTROL_AF_STATE_NOT_FOCUSED_LOCKED == afState) {
            // CONTROL_AE_STATE can be null on some devices
            Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
            if (aeState == null ||
                    aeState == CaptureResult.CONTROL_AE_STATE_CONVERGED) {
              mState = STATE_PICTURE_TAKEN;
              captureStillPicture();
            } else {
              runPrecaptureSequence();
            }
          }
          break;
        }
        case STATE_WAITING_PRECAPTURE: {
          // CONTROL_AE_STATE can be null on some devices
          Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
          if (aeState == null ||
                  aeState == CaptureResult.CONTROL_AE_STATE_PRECAPTURE ||
                  aeState == CaptureRequest.CONTROL_AE_STATE_FLASH_REQUIRED) {
            mState = STATE_WAITING_NON_PRECAPTURE;
          }
          break;
        }
        case STATE_WAITING_NON_PRECAPTURE: {
          // CONTROL_AE_STATE can be null on some devices
          Integer aeState = result.get(CaptureResult.CONTROL_AE_STATE);
          if (aeState == null || aeState != CaptureResult.CONTROL_AE_STATE_PRECAPTURE) {
            mState = STATE_PICTURE_TAKEN;
            captureStillPicture();
          }
          break;
        }
      }
    }

    @Override
    public void onCaptureProgressed(@NonNull CameraCaptureSession session,
                                    @NonNull CaptureRequest request,
                                    @NonNull CaptureResult partialResult) {
      process(partialResult);
    }

    @Override
    public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                   @NonNull CaptureRequest request,
                                   @NonNull TotalCaptureResult result) {
      process(result);
    }

  };
  //end of photo only variables

  //variables shared between photo and video mode
  private static final SparseIntArray ORIENTATIONS = new SparseIntArray();

  static {
    ORIENTATIONS.append(Surface.ROTATION_0, 90);
    ORIENTATIONS.append(Surface.ROTATION_90, 0);
    ORIENTATIONS.append(Surface.ROTATION_180, 270);
    ORIENTATIONS.append(Surface.ROTATION_270, 180);
  }

  private String mCameraId;
  public static final String CAMERA_FRONT = "1";
  public static final String CAMERA_BACK = "0";
  private boolean isBackCamera = true;
  private String[] mCameraIds;
  private AutoFitTextureView mTextureView;
  private CameraCaptureSession mCaptureSession;
  private CameraDevice mCameraDevice;
  private Size mPreviewSize;
  private HandlerThread mBackgroundThread;
  private Handler mBackgroundHandler;
  private File mFile;
  private CaptureRequest.Builder mPreviewRequestBuilder;
  private Semaphore mCameraOpenCloseLock = new Semaphore(1);
  private int mSensorOrientation; //is of type Integer in video class

  static class CompareSizesByArea implements Comparator<Size> {

    @Override
    public int compare(Size lhs, Size rhs) {
      return Long.signum((long) lhs.getWidth() * lhs.getHeight() -
              (long) rhs.getWidth() * rhs.getHeight());
    }
  }

  private final TextureView.SurfaceTextureListener mSurfaceTextureListener
          = new TextureView.SurfaceTextureListener() {

    @Override
    public void onSurfaceTextureAvailable(SurfaceTexture texture, int width, int height) {
      if (mode == PHOTO) {
        openCameraForPhoto(width, height, isBackCamera ? CAMERA_BACK : CAMERA_FRONT);
      } else {
        openCameraForVideo(width, height, isBackCamera ? CAMERA_BACK : CAMERA_FRONT);
      }
    }

    @Override
    public void onSurfaceTextureSizeChanged(SurfaceTexture texture, int width, int height) {
      configureTransform(width, height);
    }

    @Override
    public boolean onSurfaceTextureDestroyed(SurfaceTexture texture) {
      return true;
    }

    @Override
    public void onSurfaceTextureUpdated(SurfaceTexture texture) {
    }

  };
  private final CameraDevice.StateCallback mStateCallback = new CameraDevice.StateCallback() {

    @Override
    public void onOpened(@NonNull CameraDevice cameraDevice) {
      // This method is called when the camera is opened.  We start camera preview here.
      mCameraOpenCloseLock.release();
      mCameraDevice = cameraDevice;
      if (mode == PHOTO) {
        createCameraPreviewSession();
      } else {
        startVideoPreview();
      }
    }

    @Override
    public void onDisconnected(@NonNull CameraDevice cameraDevice) {
      mCameraOpenCloseLock.release();
      cameraDevice.close();
      mCameraDevice = null;
    }

    @Override
    public void onError(@NonNull CameraDevice cameraDevice, int error) {
      mCameraOpenCloseLock.release();
      cameraDevice.close();
      mCameraDevice = null;
      if (null != fragment.getActivity()) {
        fragment.getActivity().finish();
      }
    }

  };
  private static Fragment fragment;
  private int mode;
  //end of variables shared between photo and video mode

  //variables only for video mode
  private static final int SENSOR_ORIENTATION_DEFAULT_DEGREES = 90;
  private static final int SENSOR_ORIENTATION_INVERSE_DEGREES = 270;
  private static final int REQUEST_VIDEO_PERMISSIONS = 2;
  private static final String[] VIDEO_PERMISSIONS = {
          Manifest.permission.CAMERA,
          Manifest.permission.RECORD_AUDIO,
  };
  private static final SparseIntArray INVERSE_ORIENTATIONS = new SparseIntArray();

  static {
    INVERSE_ORIENTATIONS.append(Surface.ROTATION_0, 270);
    INVERSE_ORIENTATIONS.append(Surface.ROTATION_90, 180);
    INVERSE_ORIENTATIONS.append(Surface.ROTATION_180, 90);
    INVERSE_ORIENTATIONS.append(Surface.ROTATION_270, 0);
  }

  private Size mVideoSize;
  private MediaRecorder mMediaRecorder;
  private boolean mIsRecordingVideo;
  private String mNextVideoAbsolutePath;
  //end of video mode variables

  //constructor
  public static final int PHOTO = 0;
  public static final int VIDEO = 1;
  public static final int GIF = 2;

  public LifeCycleCamera(Fragment fragment, AutoFitTextureView aftv, int mode) {
    this.fragment = fragment;
    mTextureView = aftv;
    this.fragment.getLifecycle().addObserver(this);
    this.mode = mode;
  }

  private void showToast(final String text) {
    if (fragment.getActivity() != null) {
      fragment.getActivity().runOnUiThread(new Runnable() {
        @Override
        public void run() {
          Toast.makeText(fragment.getActivity(), text, Toast.LENGTH_SHORT).show();
        }
      });
    }
  }

  private static Size chooseOptimalSizeForPhoto(Size[] choices, int textureViewWidth,
                                                int textureViewHeight, int maxWidth, int maxHeight, Size aspectRatio) {

    // Collect the supported resolutions that are at least as big as the preview Surface
    List<Size> bigEnough = new ArrayList<>();
    // Collect the supported resolutions that are smaller than the preview Surface
    List<Size> notBigEnough = new ArrayList<>();
    int w = aspectRatio.getWidth();
    int h = aspectRatio.getHeight();
    for (Size option : choices) {
      if (option.getWidth() <= maxWidth && option.getHeight() <= maxHeight &&
              option.getHeight() == option.getWidth() * h / w) {
        if (option.getWidth() >= textureViewWidth &&
                option.getHeight() >= textureViewHeight) {
          bigEnough.add(option);
        } else {
          notBigEnough.add(option);
        }
      }
    }

    // Pick the smallest of those big enough. If there is no one big enough, pick the
    // largest of those not big enough.
    if (bigEnough.size() > 0) {
      return Collections.min(bigEnough, new CompareSizesByArea());
    } else if (notBigEnough.size() > 0) {
      return Collections.max(notBigEnough, new CompareSizesByArea());
    } else {
      //Log.e(TAG, "Couldn't find any suitable preview size");
      return choices[0];
    }
  }

  private static Size chooseOptimalSizeForVideo(Size[] choices, int width, int height, Size aspectRatio) {
    // Collect the supported resolutions that are at least as big as the preview Surface
    List<Size> bigEnough = new ArrayList<>();
    int w = aspectRatio.getWidth();
    int h = aspectRatio.getHeight();
    for (Size option : choices) {
      if (option.getHeight() == option.getWidth() * h / w &&
              option.getWidth() >= width && option.getHeight() >= height) {
        bigEnough.add(option);
      }
    }
    // Pick the smallest of those, assuming we found any
    if (bigEnough.size() > 0) {
      return Collections.min(bigEnough, new CompareSizesByArea());
    } else {
      //Log.e(TAG, "Couldn't find any suitable preview size");
      return choices[0];
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
  public void onActivityCreated(LifecycleOwner lifecycleOwner) {
    if (mode == PHOTO) {
      //mFile = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Camera/" + getCurrentDateAndTime() + ".jpg");
      try {
        mFile = File.createTempFile("temp file", ".png", fragment.getActivity().getCacheDir());
      } catch (IOException e) {
        Log.d("error", e.toString());
      }
    }

  }

  @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
  public void onResume(LifecycleOwner lifecycleOwner) {
    startBackgroundThread();
    if (mTextureView.isAvailable()) {
      if (mode == PHOTO) {
        openCameraForPhoto(mTextureView.getWidth(), mTextureView.getHeight(), isBackCamera ? CAMERA_BACK : CAMERA_FRONT);
      } else {
        openCameraForVideo(mTextureView.getWidth(), mTextureView.getHeight(), isBackCamera ? CAMERA_BACK : CAMERA_FRONT);
      }
    } else {
      mTextureView.setSurfaceTextureListener(mSurfaceTextureListener);
    }
  }

  @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
  public void onPause(LifecycleOwner lifecycleOwner) {
    closeCamera();
    stopBackgroundThread();
  }

  private void requestCameraPermission() {
    if (fragment.shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
      //new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
    } else {
      fragment.requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
    }
  }

  private boolean shouldShowRequestPermissionRationale(String[] permissions) {
    for (String permission : permissions) {
      if (fragment.shouldShowRequestPermissionRationale(permission)) {
        return true;
      }
    }
    return false;
  }

  private void requestVideoPermissions() {
    if (shouldShowRequestPermissionRationale(VIDEO_PERMISSIONS)) {
      //new ConfirmationDialog().show(getChildFragmentManager(), FRAGMENT_DIALOG);
    } else {
      fragment.requestPermissions(VIDEO_PERMISSIONS, REQUEST_VIDEO_PERMISSIONS);
    }
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                         @NonNull int[] grantResults) {
    if (requestCode == REQUEST_CAMERA_PERMISSION) {
      if (grantResults.length != 1 || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
               /* ErrorDialog.newInstance(getString(R.string.request_permission))
                        .show(getChildFragmentManager(), FRAGMENT_DIALOG);*/
      }
    } else if (requestCode == REQUEST_VIDEO_PERMISSIONS) {
      if (grantResults.length == VIDEO_PERMISSIONS.length) {
        for (int result : grantResults) {
          if (result != PackageManager.PERMISSION_GRANTED) {
//                        ErrorDialog.newInstance(getString(R.string.permission_request))
//                                .show(getChildFragmentManager(), FRAGMENT_DIALOG);
            break;
          }
        }
      }
    } else {
      fragment.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
  }

  private boolean hasPermissionsGranted(String[] permissions) {
    for (String permission : permissions) {
      if (ActivityCompat.checkSelfPermission(fragment.getActivity(), permission)
              != PackageManager.PERMISSION_GRANTED) {
        return false;
      }
    }
    return true;
  }

  @SuppressWarnings("SuspiciousNameCombination")
  private void setUpCameraOutputs(int width, int height, String cameraId) {

    CameraManager manager = (CameraManager) fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);
    try {
        CameraCharacteristics characteristics
                = manager.getCameraCharacteristics(cameraId);

        // We don't use a front facing camera in this sample.
//        Integer facing = characteristics.get(CameraCharacteristics.LENS_FACING);
//        if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
//          continue;
//        }

        StreamConfigurationMap map = characteristics.get(
                CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
        if (map == null) {
          return;
        }

        // For still image captures, we use the largest available size.
        Size largest = Collections.max(
                Arrays.asList(map.getOutputSizes(ImageFormat.JPEG)),
                new CompareSizesByArea());
        mImageReader = ImageReader.newInstance(largest.getWidth(), largest.getHeight(),
                ImageFormat.JPEG, /*maxImages*/2);
        mImageReader.setOnImageAvailableListener(
                mOnImageAvailableListener, mBackgroundHandler);

        // Find out if we need to swap dimension to get the preview size relative to sensor
        // coordinate.
        int displayRotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
        //noinspection ConstantConditions
        mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
        boolean swappedDimensions = false;
        switch (displayRotation) {
          case Surface.ROTATION_0:
          case Surface.ROTATION_180:
            if (mSensorOrientation == 90 || mSensorOrientation == 270) {
              swappedDimensions = true;
            }
            break;
          case Surface.ROTATION_90:
          case Surface.ROTATION_270:
            if (mSensorOrientation == 0 || mSensorOrientation == 180) {
              swappedDimensions = true;
            }
            break;
          default:
            //Log.e(TAG, "Display rotation is invalid: " + displayRotation);
        }

        Point displaySize = new Point();
        fragment.getActivity().getWindowManager().getDefaultDisplay().getSize(displaySize);
        int rotatedPreviewWidth = width;
        int rotatedPreviewHeight = height;
        int maxPreviewWidth = displaySize.x;
        int maxPreviewHeight = displaySize.y;

        if (swappedDimensions) {
          rotatedPreviewWidth = height;
          rotatedPreviewHeight = width;
          maxPreviewWidth = displaySize.y;
          maxPreviewHeight = displaySize.x;
        }

        if (maxPreviewWidth > MAX_PREVIEW_WIDTH) {
          maxPreviewWidth = MAX_PREVIEW_WIDTH;
        }

        if (maxPreviewHeight > MAX_PREVIEW_HEIGHT) {
          maxPreviewHeight = MAX_PREVIEW_HEIGHT;
        }

        // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
        // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
        // garbage capture data.
        mPreviewSize = chooseOptimalSizeForPhoto(map.getOutputSizes(SurfaceTexture.class),
                rotatedPreviewWidth, rotatedPreviewHeight, maxPreviewWidth,
                maxPreviewHeight, largest);

        // We fit the aspect ratio of TextureView to the size of preview we picked.
        int orientation = fragment.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
          mTextureView.setAspectRatio(
                  mPreviewSize.getWidth(), mPreviewSize.getHeight());
        } else {
          mTextureView.setAspectRatio(
                  mPreviewSize.getHeight(), mPreviewSize.getWidth());
        }

        // Check if the flash is supported.
        Boolean available = characteristics.get(CameraCharacteristics.FLASH_INFO_AVAILABLE);
        mFlashSupported = available == null ? false : available;

        mCameraId = cameraId;
        return;
    } catch (CameraAccessException e) {
      e.printStackTrace();
    } catch (NullPointerException e) {
      // Currently an NPE is thrown when the Camera2API is used but not supported on the
      // device this code runs.
      //ErrorDialog.newInstance(getString(R.string.camera_error))
      //        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
    }
  }

  private void openCameraForPhoto(int width, int height, String cameraID) {
    if (ContextCompat.checkSelfPermission(fragment.getActivity(), Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {
      requestCameraPermission();
      return;
    }
    setUpCameraOutputs(width, height, cameraID);
    configureTransform(width, height);

    CameraManager manager = (CameraManager) fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);
    try {
      if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
        throw new RuntimeException("Time out waiting to lock camera opening.");
      }
      manager.openCamera(mCameraId, mStateCallback, mBackgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    } catch (InterruptedException e) {
      throw new RuntimeException("Interrupted while trying to lock camera opening.", e);
    }
  }

  private void openCameraForVideo(int width, int height, String cameraId) {
    if (!hasPermissionsGranted(VIDEO_PERMISSIONS)) {
      requestVideoPermissions();
      return;
    }

    if (null == fragment.getActivity() || fragment.getActivity().isFinishing()) {
      return;
    }
    CameraManager manager = (CameraManager) fragment.getActivity().getSystemService(Context.CAMERA_SERVICE);
    try {
      //Log.d(TAG, "tryAcquire");
      if (!mCameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
        throw new RuntimeException("Time out waiting to lock camera opening.");
      }
      mCameraId = cameraId;

      // Choose the sizes for camera preview and video recording
      CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCameraId);
      StreamConfigurationMap map = characteristics
              .get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);
      mSensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION);
      if (map == null) {
        throw new RuntimeException("Cannot get available preview/video sizes");
      }
      mVideoSize = chooseVideoSize(map.getOutputSizes(MediaRecorder.class));
      mPreviewSize = chooseOptimalSizeForVideo(map.getOutputSizes(SurfaceTexture.class),
              width, height, mVideoSize);

      int orientation = fragment.getActivity().getResources().getConfiguration().orientation;
      if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
        mTextureView.setAspectRatio(mPreviewSize.getWidth(), mPreviewSize.getHeight());
      } else {
        mTextureView.setAspectRatio(mPreviewSize.getHeight(), mPreviewSize.getWidth());
      }
      configureTransform(width, height);
      mMediaRecorder = new MediaRecorder();
      manager.openCamera(mCameraId, mStateCallback, null);
    } catch (CameraAccessException e) {
      Toast.makeText(fragment.getActivity(), "Cannot access the camera.", Toast.LENGTH_SHORT).show();
      fragment.getActivity().finish();
    } catch (NullPointerException e) {
      // Currently an NPE is thrown when the Camera2API is used but not supported on the
      // device this code runs.
      //ErrorDialog.newInstance(getString(R.string.camera_error))
      //        .show(getChildFragmentManager(), FRAGMENT_DIALOG);
    } catch (InterruptedException e) {
      throw new RuntimeException("Interrupted while trying to lock camera opening.");
    } catch (SecurityException e) {

    }
  }

  private void closeCamera() {
    try {
      mCameraOpenCloseLock.acquire();
      if (null != mCaptureSession) {
        mCaptureSession.close();
        mCaptureSession = null;
      }
      if (null != mCameraDevice) {
        mCameraDevice.close();
        mCameraDevice = null;
      }
      if (null != mImageReader) {
        mImageReader.close();
        mImageReader = null;
      }
      if (null != mMediaRecorder) {
        mMediaRecorder.release();
        mMediaRecorder = null;
      }
    } catch (InterruptedException e) {
      throw new RuntimeException("Interrupted while trying to lock camera closing.", e);
    } finally {
      mCameraOpenCloseLock.release();
    }
  }

  private void startBackgroundThread() {
    mBackgroundThread = new HandlerThread("CameraBackground");
    mBackgroundThread.start();
    mBackgroundHandler = new Handler(mBackgroundThread.getLooper());
  }

  private void stopBackgroundThread() {
    mBackgroundThread.quitSafely();
    try {
      mBackgroundThread.join();
      mBackgroundThread = null;
      mBackgroundHandler = null;
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private void createCameraPreviewSession() {
    try {
      SurfaceTexture texture = mTextureView.getSurfaceTexture();
      assert texture != null;

      // We configure the size of default buffer to be the size of camera preview we want.
      texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());

      // This is the output Surface we need to start preview.
      Surface surface = new Surface(texture);

      // We set up a CaptureRequest.Builder with the output Surface.
      mPreviewRequestBuilder
              = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
      mPreviewRequestBuilder.addTarget(surface);

      // Here, we create a CameraCaptureSession for camera preview.
      mCameraDevice.createCaptureSession(Arrays.asList(surface, mImageReader.getSurface()),
              new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
                  // The camera is already closed
                  if (null == mCameraDevice) {
                    return;
                  }

                  // When the session is ready, we start displaying the preview.
                  mCaptureSession = cameraCaptureSession;
                  try {
                    // Auto focus should be continuous for camera preview.
                    mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
                    // Flash is automatically enabled when necessary.
                    setAutoFlash(mPreviewRequestBuilder);

                    // Finally, we start displaying the camera preview.
                    mPreviewRequest = mPreviewRequestBuilder.build();
                    mCaptureSession.setRepeatingRequest(mPreviewRequest,
                            mCaptureCallback, mBackgroundHandler);
                  } catch (CameraAccessException e) {
                    e.printStackTrace();
                  }
                }

                @Override
                public void onConfigureFailed(
                        @NonNull CameraCaptureSession cameraCaptureSession) {
                  showToast("Failed");
                }
              }, null
      );
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void configureTransform(int viewWidth, int viewHeight) {

    if (null == mTextureView || null == mPreviewSize || null == fragment.getActivity()) {
      return;
    }
    int rotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
    Matrix matrix = new Matrix();
    RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
    RectF bufferRect = new RectF(0, 0, mPreviewSize.getHeight(), mPreviewSize.getWidth());
    float centerX = viewRect.centerX();
    float centerY = viewRect.centerY();
    if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
      bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
      matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
      float scale = Math.max(
              (float) viewHeight / mPreviewSize.getHeight(),
              (float) viewWidth / mPreviewSize.getWidth());
      matrix.postScale(scale, scale, centerX, centerY);
      matrix.postRotate(90 * (rotation - 2), centerX, centerY);
    } else if (Surface.ROTATION_180 == rotation && mode == PHOTO) {
      matrix.postRotate(180, centerX, centerY);
    }
    mTextureView.setTransform(matrix);
  }

  public void takePicture() {
    lockFocus();
  }

  private void lockFocus() {
    try {
      // This is how to tell the camera to lock focus.
      mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
              CameraMetadata.CONTROL_AF_TRIGGER_START);
      // Tell #mCaptureCallback to wait for the lock.
      mState = STATE_WAITING_LOCK;
      mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
              mBackgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void runPrecaptureSequence() {
    try {
      // This is how to tell the camera to trigger.
      mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER,
              CaptureRequest.CONTROL_AE_PRECAPTURE_TRIGGER_START);
      // Tell #mCaptureCallback to wait for the precapture sequence to be set.
      mState = STATE_WAITING_PRECAPTURE;
      mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
              mBackgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void captureStillPicture() {
    try {
      ;
      if (null == fragment.getActivity() || null == mCameraDevice) {
        return;
      }
      // This is the CaptureRequest.Builder that we use to take a picture.
      final CaptureRequest.Builder captureBuilder =
              mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE);
      captureBuilder.addTarget(mImageReader.getSurface());

      // Use the same AE and AF modes as the preview.
      captureBuilder.set(CaptureRequest.CONTROL_AF_MODE,
              CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE);
      setAutoFlash(captureBuilder);

      // Orientation
      int rotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
      captureBuilder.set(CaptureRequest.JPEG_ORIENTATION, getOrientation(rotation));

      CameraCaptureSession.CaptureCallback CaptureCallback
              = new CameraCaptureSession.CaptureCallback() {

        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session,
                                       @NonNull CaptureRequest request,
                                       @NonNull TotalCaptureResult result) {
          showToast("Saved: " + mFile);
          //Log.d(TAG, mFile.toString());
          unlockFocus();
        }
      };

      mCaptureSession.stopRepeating();
      mCaptureSession.abortCaptures();
      mCaptureSession.capture(captureBuilder.build(), CaptureCallback, null);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private int getOrientation(int rotation) {
    // Sensor orientation is 90 for most devices, or 270 for some devices (eg. Nexus 5X)
    // We have to take that into account and rotate JPEG properly.
    // For devices with orientation of 90, we simply return our mapping from ORIENTATIONS.
    // For devices with orientation of 270, we need to rotate the JPEG 180 degrees.
    return (ORIENTATIONS.get(rotation) + mSensorOrientation + 270) % 360;
  }

  private void unlockFocus() {
    try {
      // Reset the auto-focus trigger
      mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER,
              CameraMetadata.CONTROL_AF_TRIGGER_CANCEL);
      setAutoFlash(mPreviewRequestBuilder);
      mCaptureSession.capture(mPreviewRequestBuilder.build(), mCaptureCallback,
              mBackgroundHandler);
      // After this, the camera will go back to the normal state of preview.
      mState = STATE_PREVIEW;
      mCaptureSession.setRepeatingRequest(mPreviewRequest, mCaptureCallback,
              mBackgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void setAutoFlash(CaptureRequest.Builder requestBuilder) {
    if (mFlashSupported) {
      requestBuilder.set(CaptureRequest.CONTROL_AE_MODE,
              CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH);
    }
  }

  private static class ImageSaver implements Runnable {

    /**
     * The JPEG image
     */
    private final Image mImage;
    /**
     * The file we save the image into.
     */
    private final File mFile;

    ImageSaver(Image image, File file) {
      mImage = image;
      mFile = file;
    }

    @Override
    public void run() {
      ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
      byte[] bytes = new byte[buffer.remaining()];
      buffer.get(bytes);
      FileOutputStream output = null;
      try {
        output = new FileOutputStream(mFile);
        output.write(bytes);
      } catch (IOException e) {
        e.printStackTrace();
      } finally {
        mImage.close();
        if (null != output) {
          try {
            output.close();
            ((VisualMediaPostFragment) fragment).moveFile(mFile);
            makeFileAvailible(mFile);
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

  }

  private void setUpMediaRecorder() throws IOException {
    if (null == fragment.getActivity()) {
      return;
    }
    mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
    mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.SURFACE);
    mMediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
    if (mNextVideoAbsolutePath == null || mNextVideoAbsolutePath.isEmpty()) {
      mNextVideoAbsolutePath = getVideoFilePath(fragment.getActivity());
    }
    mMediaRecorder.setOutputFile(mNextVideoAbsolutePath);
    mMediaRecorder.setVideoEncodingBitRate(10000000);
    mMediaRecorder.setVideoFrameRate(30);
    mMediaRecorder.setVideoSize(mVideoSize.getWidth(), mVideoSize.getHeight());
    mMediaRecorder.setVideoEncoder(MediaRecorder.VideoEncoder.H264);
    mMediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
    int rotation = fragment.getActivity().getWindowManager().getDefaultDisplay().getRotation();
    switch (mSensorOrientation) {
      case SENSOR_ORIENTATION_DEFAULT_DEGREES:
        mMediaRecorder.setOrientationHint(ORIENTATIONS.get(rotation));
        break;
      case SENSOR_ORIENTATION_INVERSE_DEGREES:
        mMediaRecorder.setOrientationHint(INVERSE_ORIENTATIONS.get(rotation));
        break;
    }
    mMediaRecorder.prepare();
  }

  private String getVideoFilePath(Context context) {
    try {
      if (mode == VIDEO) {
        mFile = File.createTempFile("temp file", ".mp4", fragment.getActivity().getCacheDir());
      }else {
        mFile = File.createTempFile("temp file", ".mp4", fragment.getActivity().getCacheDir());
      }
    } catch (IOException e) {
      Log.d("error", e.toString());
    }
    makeFileAvailible(mFile);
    return mFile.toString();
  }

  public void startRecordingVideo() {
    if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
      return;
    }
    try {
      closePreviewSession();
      setUpMediaRecorder();
      SurfaceTexture texture = mTextureView.getSurfaceTexture();
      assert texture != null;
      texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
      mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_RECORD);
      List<Surface> surfaces = new ArrayList<>();

      // Set up Surface for the camera preview
      Surface previewSurface = new Surface(texture);
      surfaces.add(previewSurface);
      mPreviewRequestBuilder.addTarget(previewSurface);

      // Set up Surface for the MediaRecorder
      Surface recorderSurface = mMediaRecorder.getSurface();
      surfaces.add(recorderSurface);
      mPreviewRequestBuilder.addTarget(recorderSurface);

      // Start a capture session
      // Once the session starts, we can update the UI and start recording
      mCameraDevice.createCaptureSession(surfaces, new CameraCaptureSession.StateCallback() {

        @Override
        public void onConfigured(@NonNull CameraCaptureSession cameraCaptureSession) {
          mCaptureSession = cameraCaptureSession;
          updatePreview();
          fragment.getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
              // UI
              //mButtonVideo.setText(R.string.stop);
              mIsRecordingVideo = true;

              // Start recording
              mMediaRecorder.start();
            }
          });
        }

        @Override
        public void onConfigureFailed(@NonNull CameraCaptureSession cameraCaptureSession) {
          if (null != fragment.getActivity()) {
            Toast.makeText(fragment.getActivity(), "Failed", Toast.LENGTH_SHORT).show();
          }
        }
      }, mBackgroundHandler);
    } catch (CameraAccessException | IOException e) {
      e.printStackTrace();
    }

  }

  private void closePreviewSession() {
    if (mCaptureSession != null) {
      mCaptureSession.close();
      mCaptureSession = null;
    }
  }

  public void stopRecordingVideo() {
    // UI
    mIsRecordingVideo = false;
    //mButtonVideo.setText(R.string.record);
    // Stop recording
    mMediaRecorder.stop();
    mMediaRecorder.reset();


    if (null != fragment.getActivity()) {
      Toast.makeText(fragment.getActivity(), "Video saved: " + mNextVideoAbsolutePath,
              Toast.LENGTH_SHORT).show();
      //Log.d(TAG, "Video saved: " + mNextVideoAbsolutePath);
    }
    mNextVideoAbsolutePath = null;
    startVideoPreview();
    getFilePath();
  }

  private void startVideoPreview() {
    if (null == mCameraDevice || !mTextureView.isAvailable() || null == mPreviewSize) {
      return;
    }
    try {
      closePreviewSession();
      SurfaceTexture texture = mTextureView.getSurfaceTexture();
      assert texture != null;
      texture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
      mPreviewRequestBuilder = mCameraDevice.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);

      Surface previewSurface = new Surface(texture);
      mPreviewRequestBuilder.addTarget(previewSurface);

      mCameraDevice.createCaptureSession(Collections.singletonList(previewSurface),
              new CameraCaptureSession.StateCallback() {

                @Override
                public void onConfigured(@NonNull CameraCaptureSession session) {
                  mCaptureSession = session;
                  updatePreview();
                }

                @Override
                public void onConfigureFailed(@NonNull CameraCaptureSession session) {
                  if (null != fragment.getActivity()) {
                    Toast.makeText(fragment.getActivity(), "Failed", Toast.LENGTH_SHORT).show();
                  }
                }
              }, mBackgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void updatePreview() {
    if (null == mCameraDevice) {
      return;
    }
    try {
      setUpCaptureRequestBuilder(mPreviewRequestBuilder);
      HandlerThread thread = new HandlerThread("CameraPreview");
      thread.start();
      mCaptureSession.setRepeatingRequest(mPreviewRequestBuilder.build(), null, mBackgroundHandler);
    } catch (CameraAccessException e) {
      e.printStackTrace();
    }
  }

  private void setUpCaptureRequestBuilder(CaptureRequest.Builder builder) {
    builder.set(CaptureRequest.CONTROL_MODE, CameraMetadata.CONTROL_MODE_AUTO);
  }

  private static Size chooseVideoSize(Size[] choices) {
    for (Size size : choices) {
      if (size.getWidth() == size.getHeight() * 4 / 3 && size.getWidth() <= 1080) {
        return size;
      }
    }
    //Log.e(TAG, "Couldn't find any suitable video size");
    return choices[choices.length - 1];
  }

  private String getCurrentDateAndTime() {
    Calendar c = Calendar.getInstance();
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    String formattedDate = df.format(c.getTime());
    return formattedDate;
  }

  public static void makeFileAvailible(File file) {
    MediaScannerConnection.scanFile(fragment.getActivity(), new String[]{file.toString()}
            , null, (String path, Uri uri) ->{}
    );
  }

  public void getFilePath() {
    if (mode == VIDEO) {
      ((VisualMediaPostFragment) fragment).moveFile(mFile);
    } else if (mode == GIF){
      ((VisualMediaPostFragment) fragment).moveFile(mFile);
    }
  }

  public void switchCamera(){
    isBackCamera = !isBackCamera;
    onPause(fragment);
    onResume(fragment);
  }

  public int getMode(){
    return mode;
  }

  public void setMode(int mode){
    this.mode = mode;
    onResume(fragment);
  }

}
