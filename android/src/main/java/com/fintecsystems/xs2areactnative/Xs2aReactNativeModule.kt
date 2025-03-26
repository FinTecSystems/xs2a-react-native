package com.fintecsystems.xs2areactnative

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.facebook.react.bridge.Promise
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel

class Xs2aReactNativeModule(reactContext: ReactApplicationContext) :
  ReactContextBaseJavaModule(reactContext) {

  override fun getName(): String {
    return NAME
  }

  @ReactMethod
  fun clearCredentials() {
    XS2AWizardViewModel.clearCredentials(reactApplicationContext)
  }

  companion object {
    const val NAME = "Xs2aReactNativeModule"
  }
}
