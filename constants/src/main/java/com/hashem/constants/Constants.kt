package com.hashem.constants

object Constants {
    init {
        System.loadLibrary("constants")
    }

    /**
     * A native method that is implemented by the 'constants' native library,
     * which is packaged with this application.
     */
    external fun getApiKey(): String
}