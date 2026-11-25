package com.cameragallery.core

import android.app.Application

class CameraGalleryApp : Application() {
        override fun onCreate() {
                    super.onCreate()
                            CameraService(this).init()
        }
}

        }
}