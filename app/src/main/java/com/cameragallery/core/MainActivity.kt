package com.cameragallery.core

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts

class MainActivity : Activity() {
            private val galleryPicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
                    uri?.let { loadAndProcess(it) }
                        }
                            private val cameraService by lazy { CameraService(this) }

                                override fun onCreate(savedInstanceState: Bundle?) {
                                                super.onCreate(savedInstanceState)
                                                        setContentView(R.layout.activity_main)
                                                                cameraService.init()
                                                                        findViewById<Button>(R.id.pick_button).setOnClickListener {
                                                                                            galleryPicker.launch("image/*")
                                                                        }
                                }

                                    private fun loadAndProcess(uri: Uri) {
                                                val stream = contentResolver.openInputStream(uri) ?: return
                                                        val bitmap = BitmapFactory.decodeStream(stream)
                                                                stream.close()
                                                                        val yuv = bitmapToYuv420(bitmap)
                                                                                cameraService.processFrame(yuv)
                                    }

                                        private fun bitmapToYuv420(bitmap: Bitmap): ByteArray {
                                                        val width = bitmap.width
                                                                val height = bitmap.height
                                                                        val ySize = width * height
                                                                                val uvSize = (width / 2) * (height / 2)
                                                                                        val out = ByteArray(ySize + uvSize * 2)
                                                                                                val pixels = IntArray(width * height)
                                                                                                        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
                                                                                                                var yi = 0
                                                                                                                        for (y in 0 until height) {
                                                                                                                                            for (x in 0 until width) {
                                                                                                                                                                val p = pixels[y * width + x]
                                                                                                                                                                                val r = (p shr 16) and 0xFF
                                                                                                                                                                                                val g = (p shr 8) and 0xFF
                                                                                                                                                                                                                val b = p and 0xFF
                                                                                                                                                                                                                                val yVal = ((66 * r + 129 * g + 25 * b + 128) shr 8) + 16
                                                                                                                                                                                                                                                out[yi++] = yVal.coerceIn(0, 255).toByte()
                                                                                                                                            }
                                                                                                                        }
                                                                                                                                yi = ySize
                                                                                                                                        for (y in 0 until height step 2) {
                                                                                                                                                            for (x in 0 until width step 2) {
                                                                                                                                                                                val p1 = pixels[y * width + x]
                                                                                                                                                                                                val p2 = pixels[y * width + x + 1]
                                                                                                                                                                                                                val p3 = pixels[(y + 1) * width + x]
                                                                                                                                                                                                                                val p4 = pixels[(y + 1) * width + x + 1]
                                                                                                                                                                                                                                                val r = (((p1 shr 16) and 0xFF) + ((p2 shr 16) and 0xFF) + ((p3 shr 16) and 0xFF) + ((p4 shr 16) and 0xFF)) / 4
                                                                                                                                                                                                                                                                val g = (((p1 shr 8) and 0xFF) + ((p2 shr 8) and 0xFF) + ((p3 shr 8) and 0xFF) + ((p4 shr 8) and 0xFF)) / 4
                                                                                                                                                                                                                                                                                val b = ((p1 and 0xFF) + (p2 and 0xFF) + (p3 and 0xFF) + (p4 and 0xFF)) / 4
                                                                                                                                                                                                                                                                                                val uVal = ((-38 * r - 74 * g + 112 * b + 128) shr 8) + 128
                                                                                                                                                                                                                                                                                                                val vVal = ((112 * r - 94 * g - 18 * b + 128) shr 8) + 128
                                                                                                                                                                                                                                                                                                                                out[yi++] = uVal.coerceIn(0, 255).toByte()
                                                                                                                                                                                                                                                                                                                                                out[yi++] = vVal.coerceIn(0, 255).toByte()
                                                                                                                                                            }
                                                                                                                                        }
                                                                                                                                                return out
                                        }
}

                                                                                                                                            }
                                                                        }
                                }
}