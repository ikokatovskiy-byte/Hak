package com.cameragallery.core

import android.content.Context
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.hardware.camera2.params.StreamConfigurationMap
import android.media.ImageReader
import android.util.Size

class CameraService(private val context: Context) {
        private var surfaceTexture: SurfaceTexture? = null
            private var imageReader: ImageReader? = null
                private val cameraId = "0"
                    private var cachedYuv: ByteArray? = null

                        fun init() {
                                    val cameraManager = context.getSystemService(Context.CAMERA_SERVICE) as CameraManager
                                            val characteristics = CameraCharacteristics.Builder()
                                                        .set(CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL, CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED)
                                                                    .set(CameraCharacteristics.LENS_FACING, CameraCharacteristics.LENS_FACING_FRONT)
                                                                                .set(CameraCharacteristics.SENSOR_ORIENTATION, 90)
                                                                                            .set(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP, buildStreamMap())
                                                                                                        .build()
                                                                                                                cameraManager.addExtendedCameraCharacteristics(cameraId, characteristics)
                        }

                            private fun buildStreamMap(): StreamConfigurationMap {
                                        val sizes = arrayOf(Size(1280, 720))
                                                val formats = intArrayOf(ImageFormat.YUV_420_888)
                                                        return StreamConfigurationMap(sizes, formats)
                            }

                                fun bindSurfaceTexture(st: SurfaceTexture) {
                                            surfaceTexture = st
                                                    imageReader = ImageReader.newInstance(1280, 720, ImageFormat.YUV_420_888, 2)
                                                            st.setDefaultBufferSize(1280, 720)
                                }

                                    fun processFrame(yuvBytes: ByteArray) {
                                                cachedYuv = yuvBytes
                                                        val image = imageReader?.acquireLatestImage()
                                                                if (image != null) {
                                                                                val plane = image.planes[0]
                                                                                            plane.buffer.put(yuvBytes)
                                                                                                        plane.buffer.position(0)
                                                                                                                    image.close()
                                                                }
                                                                        surfaceTexture?.updateTexImage()
                                    }

                                        fun serveCachedFrame() {
                                                    cachedYuv?.let { processFrame(it) }
                                        }
}

                                                                }
                        }
}