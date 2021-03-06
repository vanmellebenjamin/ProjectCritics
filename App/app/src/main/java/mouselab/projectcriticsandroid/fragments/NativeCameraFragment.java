/*
 * Copyright (c) 2014 Rex St. John on behalf of AirPair.com
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package mouselab.projectcriticsandroid.fragments;

import android.content.Context;
import android.hardware.Camera;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;


import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import mouselab.projectcriticsandroid.R;

/**
 * Take a picture directly from inside the app using this fragment.
 *
 * Reference: http://developer.android.com/training/camera/cameradirect.html
 * Reference: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
 * Reference: http://stackoverflow.com/questions/10913181/camera-preview-is-not-restarting
 *
 * Created by Rex St. John (on behalf of AirPair.com) on 3/4/14.
 */
public class NativeCameraFragment extends BaseFragment {
    public static final String ARG_OBJECT = "Camera";

    private boolean inPreview = false;
    private static int currentCameraId = currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
    // Native camera.
    private Camera mCamera;

    // View to display the camera output.
    private CameraPreview mPreview;

    // Reference to the containing view.
    private View mCameraView;

    /**
     * Default empty constructor.
     */
    public NativeCameraFragment(){
        super();
    }

    /**
     * Static factory method
     * @param sectionNumber
     * @return
     */
    public static NativeCameraFragment newInstance(int sectionNumber) {
        NativeCameraFragment fragment = new NativeCameraFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * OnCreateView fragment override
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_native_camera, container, false);

        // Create our Preview view and set it as the content of our activity.
        boolean opened = safeCameraOpenInView(view);

        if(opened == false){
            Log.d("CameraGuide", "Error, Camera failed to open");
            return view;
        }

        return view;
    }

    /**
     * Recommended "safe" way to open the camera.
     * @param view
     * @return
     */
    private boolean safeCameraOpenInView(View view) {
        boolean qOpened = false;
        releaseCameraAndPreview();
        mCamera = getCameraInstance();
        mCameraView = view;
        qOpened = (mCamera != null);

        if(qOpened == true) {
            mPreview = new CameraPreview(getActivity().getBaseContext(), mCamera,view);
            FrameLayout preview = (FrameLayout) view.findViewById(R.id.camera_preview);
            preview.addView(mPreview);
            mPreview.startCameraPreview();
        }
        return qOpened;
    }

    /**
     * Recommended "safe" way to open the camera.
     * @param view
     * @return
     */
    private boolean switchSafeCameraOpenInView(View view) {
        boolean qOpened = false;
        releaseCameraAndPreview();
        mCamera = getCameraInstance();
        mCameraView = view;
        qOpened = (mCamera != null);

        if(qOpened == true){
            mPreview.setCamera(mCamera);
            mPreview.startCameraPreview();
        }
        return qOpened;
    }

    /**
     * Safe method for getting a camera instance.
     * @return
     */
    public static Camera getCameraInstance(){
        Camera c = null;
        try {
            c = Camera.open(currentCameraId); // attempt to get a Camera instance
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return c; // returns null if camera is unavailable
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releaseCameraAndPreview();
    }

    /**
     * Clear any existing preview / camera.
     */
    private void releaseCameraAndPreview() {

        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
        if(mPreview != null){
            mPreview.destroyDrawingCache();
            mPreview.mCamera = null;
        }
    }

    /**
     * Surface on which the camera projects it's capture results. This is derived both from Google's docs and the
     * excellent StackOverflow answer provided below.
     *
     * Reference / Credit: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
     */
    class CameraPreview extends SurfaceView implements SurfaceHolder.Callback {

        private boolean recording = false;

        // SurfaceHolder
        private SurfaceHolder mHolder;

        // Our Camera.
        private Camera mCamera;

        // Parent Context.
        private Context mContext;

        // Camera Sizing (For rotation, orientation changes)
        private Camera.Size mPreviewSize;

        // List of supported preview sizes
        private List<Camera.Size> mSupportedPreviewSizes;

        // View holding this camera.
        private View mCameraView;

        private MediaRecorder recorder;

        private View view;

        public CameraPreview(Context context, Camera camera, View cameraView) {
            super(context);
            this.view = cameraView;
            // Capture the context
            mCameraView = cameraView;
            mContext = context;
            setCamera(camera);

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder = getHolder();
            mHolder.addCallback(this);
            mHolder.setKeepScreenOn(true);
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            // setup switch buttons
            final ImageButton switchCameraButton = (ImageButton) view.findViewById(R.id.swich_camera);

            // Trap the capture button.
            ImageButton captureButton = (ImageButton) view.findViewById(R.id.button_capture);
            captureButton.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (recording) {
                                recorder.stop();
                                recording = false;

                                // restart camera preview
                                switchSafeCameraOpenInView(view);
                                initRecorder();
                                // if the device has ONE camera, we hide this button
                                if(Camera.getNumberOfCameras() == 1){
                                    switchCameraButton.setVisibility(View.INVISIBLE);
                                } else  {
                                    switchCameraButton.setVisibility(View.GONE);
                                }
                            } else {
                                // stop the preview
                                releaseCameraAndPreview();
                                switchCameraButton.setVisibility(View.INVISIBLE);
                                // start the record
                                recording = true;
                                prepareRecorder();
                                recorder.start();
                            }
                        }
                    }
            );



            // if the device has ONE camera, we hide this button
            if(Camera.getNumberOfCameras() == 1){
                switchCameraButton.setVisibility(View.INVISIBLE);
            }

            // we allow the button to swith between front and back camera
            else {
                switchCameraButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //swap the id of the camera to be used
                        if (currentCameraId == Camera.CameraInfo.CAMERA_FACING_BACK) {
                            currentCameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
                        } else {
                            currentCameraId = Camera.CameraInfo.CAMERA_FACING_BACK;
                        }
                        switchSafeCameraOpenInView(view);
                    }
                });
            }

            // Set recorder
            initRecorder();
        }

        /**
         * Begin the preview of the camera input.
         */
        public void startCameraPreview()
        {
            try{
                mCamera.setPreviewDisplay(mHolder);
                mCamera.startPreview();
                mCamera.setDisplayOrientation(90);
                inPreview = true;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        /**
         * Begin the preview of the camera input.
         */
        public void reStartCameraPreview()
        {
            try {
                mCamera.release();

                setCamera(getCameraInstance());
                startCameraPreview();
            }
            catch(Exception e){
                e.printStackTrace();
            }
        }

        /**
         * Extract supported preview and flash modes from the camera.
         * @param camera
         */
        private void setCamera(Camera camera)
        {
            // Source: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
            mCamera = camera;
            mSupportedPreviewSizes = mCamera.getParameters().getSupportedPreviewSizes();

            requestLayout();
        }

        /**
         * The Surface has been created, now tell the camera where to draw the preview.
         * @param holder
         */
        public void surfaceCreated(SurfaceHolder holder) {
            try {
                mCamera.setPreviewDisplay(holder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Dispose of the camera preview.
         * @param holder
         */
        public void surfaceDestroyed(SurfaceHolder holder) {
            if (recording) {
                recorder.stop();
                recording = false;
            }
            recorder.release();
            // finish();
            if (mCamera != null){
                mCamera.stopPreview();
            }
        }

        /**
         * React to surface changed events
         * @param holder
         * @param format
         * @param w
         * @param h
         */
        public void surfaceChanged(SurfaceHolder holder, int format, int w, int h) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.

            if (mHolder.getSurface() == null){
                // preview surface does not exist
                return;
            }

            // stop preview before making changes
            try {
                Camera.Parameters parameters = mCamera.getParameters();

                // Set the auto-focus mode to "continuous"
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);

                // Preview size must exist.
                if(mPreviewSize != null) {
                    Camera.Size previewSize = mPreviewSize;
                    parameters.setPreviewSize(previewSize.width, previewSize.height);
                }

                mCamera.setParameters(parameters);
                mCamera.startPreview();
            } catch (Exception e){
                e.printStackTrace();
            }
        }

        /**
         * Calculate the measurements of the layout
         * @param widthMeasureSpec
         * @param heightMeasureSpec
         */
        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec)
        {
            // Source: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
            final int width = resolveSize(getSuggestedMinimumWidth(), widthMeasureSpec);
            final int height = resolveSize(getSuggestedMinimumHeight(), heightMeasureSpec);
            setMeasuredDimension(width, height);

            if (mSupportedPreviewSizes != null){
                mPreviewSize = getOptimalPreviewSize(mSupportedPreviewSizes, width, height);
            }
        }

        /**
         * Update the layout based on rotation and orientation changes.
         * @param changed
         * @param left
         * @param top
         * @param right
         * @param bottom
         */
        @Override
        protected void onLayout(boolean changed, int left, int top, int right, int bottom)
        {
            // Source: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
            if (changed) {
                final int width = right - left;
                final int height = bottom - top;

                int previewWidth = width;
                int previewHeight = height;

                final int scaledChildHeight = previewHeight * width / previewWidth;
                mCameraView.layout(0, height - scaledChildHeight, width, height);
            }
        }

        /**
         *
         * @param sizes
         * @param width
         * @param height
         * @return
         */
        private Camera.Size getOptimalPreviewSize(List<Camera.Size> sizes, int width, int height)
        {
            // Source: http://stackoverflow.com/questions/7942378/android-camera-will-not-work-startpreview-fails
            Camera.Size optimalSize = null;

            final double ASPECT_TOLERANCE = 0.1;
            double targetRatio = (double) height / width;

            // Try to find a size match which suits the whole screen minus the menu on the left.
            for (Camera.Size size : sizes){

                if (size.height != width) continue;
                double ratio = (double) size.width / size.height;
                if (ratio <= targetRatio + ASPECT_TOLERANCE && ratio >= targetRatio - ASPECT_TOLERANCE){
                    optimalSize = size;
                }
            }

            // If we cannot find the one that matches the aspect ratio, ignore the requirement.
            if (optimalSize == null) {
                // TODO : Backup in case we don't get a size.
            }

            return optimalSize;
        }

        private void initRecorder() {
            recorder = new MediaRecorder();
            recorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);
            //recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            recorder.setVideoEncoder(MediaRecorder.VideoEncoder.MPEG_4_SP);

            recorder.setOrientationHint(90);
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());

            File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_PICTURES), "Critics");
            recorder.setOutputFile(mediaStorageDir.getPath() + File.separator +
                    "videocapture_" + timeStamp + ".mp4");
        }

        private void prepareRecorder() {
            recorder.setPreviewDisplay(mHolder.getSurface());
            try {
                recorder.prepare();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                //finish();
            } catch (IOException e) {
                e.printStackTrace();
                //finish();
            }
        }

        private void finish() {
            recorder.stop();
            recorder.release();
        }
    }

}