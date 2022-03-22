package com.xs2areactnative

import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.bridge.ReactContextBaseJavaModule
import com.facebook.react.bridge.ReactMethod
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel

class Xs2aReactNativeModule(context: ReactApplicationContext) : ReactContextBaseJavaModule(context) {
  override fun getName() = "Xs2aReactNativeModule"

  @ReactMethod
  fun clearCredentials() {
    XS2AWizardViewModel.clearCredentials(reactApplicationContext)
  }
}
