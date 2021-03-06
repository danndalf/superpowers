package com.adaburrows.superpowers;

import java.io.IOException;

import android.util.Log;
import android.content.Context;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.SurfaceHolder;
import android.view.SurfaceView;


class CameraView extends SurfaceView implements SurfaceHolder.Callback {

  // Member vars
  private static final String TAG = "CameraView";

  SurfaceHolder mHolder;
  Camera mCamera;
  AudioSynth mAudioSynth;

  // Constructor
  CameraView(Context context, Camera camera, AudioSynth synth) {
    super(context);

    mCamera = camera;
    mAudioSynth = synth;
    mHolder = getHolder();
    mHolder.addCallback(this);
    mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
  }

  // When our surface is created (the application starts or becomes active)
  public void surfaceCreated(SurfaceHolder holder) {
    // Kick it off
    startPreview(holder);
  }

  // When we go out of sight
  public void surfaceDestroyed(SurfaceHolder holder) {
    // We share the camera; but it's released in the activity
    stopPreview(holder);
  }

  // On a resize
  public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
    // Let's reset everything
    stopPreview(holder);
    startPreview(holder);
  }

  private void startPreview(SurfaceHolder holder) {
    // Bail if we don't have a surface or camera
    if (mHolder.getSurface() == null || mCamera == null) {
      // preview surface does not exist
      return;
    }
    try {
      mCamera.setPreviewDisplay(holder);
      mCamera.setPreviewCallbackWithBuffer(mAudioSynth);
      mCamera.startPreview();
    }
    catch (IOException exception) {
      Log.d(TAG, "Error setting camera preview: " + exception.getMessage());
    }
  }

  private void stopPreview(SurfaceHolder holder) {
    // Bail if we don't have a surface or camera
    if (mHolder.getSurface() == null || mCamera == null) {
      // preview surface does not exist
      return;
    }
    try {
      mCamera.stopPreview();
    } catch (Exception exception) {
      Log.d(TAG, "Error stopping camera preview: " + exception.getMessage());
    }

  }

}