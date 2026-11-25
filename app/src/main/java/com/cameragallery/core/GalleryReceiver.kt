package com.cameragallery.core

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class GalleryReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
                    CameraService(context).init()
        }
}

        }
}