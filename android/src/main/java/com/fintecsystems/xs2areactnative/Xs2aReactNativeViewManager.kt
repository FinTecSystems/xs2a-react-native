package com.fintecsystems.xs2areactnative

import android.graphics.Color
import android.view.View
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.XS2AWizardCallbackListener
import com.fintecsystems.xs2awizard.components.XS2AWizardError
import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import com.fintecsystems.xs2awizard.components.XS2AWizardStep
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.LogoVariation

class Xs2aReactNativeViewManager : SimpleViewManager<View>() {
  private val _currentStep = mutableStateOf<XS2AWizardStep?>(null)
  private val _sessionKey = mutableStateOf<String?>(null)
  private val _theme = mutableStateOf<XS2ATheme?>(null)
  private val _language = mutableStateOf<XS2AWizardLanguage?>(null)

  override fun getName() = "Xs2aReactNativeView"

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Map<String, String>> =
    MapBuilder.of(
      EVENT_SUCCESS,
      MapBuilder.of("registrationName", EVENT_SUCCESS_REGISTRATION_NAME),
      EVENT_ABORT,
      MapBuilder.of("registrationName", EVENT_ABORT_REGISTRATION_NAME),
      EVENT_ERROR,
      MapBuilder.of("registrationName", EVENT_ERROR_REGISTRATION_NAME),
      EVENT_NETWORK_ERROR,
      MapBuilder.of("registrationName", EVENT_NETWORK_ERROR_REGISTRATION_NAME),
      EVENT_BACK,
      MapBuilder.of("registrationName", EVENT_BACK_REGISTRATION_NAME),
      EVENT_STEP,
      MapBuilder.of("registrationName", EVENT_STEP_REGISTRATION_NAME)
    )

  override fun createViewInstance(reactContext: ThemedReactContext): View {
    return ComposeView(reactContext.reactApplicationContext.currentActivity!!).apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)

      val xs2aCallbackListener = object : XS2AWizardCallbackListener {
        override fun onAbort() {
          onAbort(reactContext, this@apply)
        }

        override fun onBack() {
          onBack(reactContext, this@apply)
        }

        override fun onError(xs2aWizardError: XS2AWizardError) {
          onError(reactContext, this@apply, xs2aWizardError)
        }

        override fun onFinish(credentials: String?) {
          onFinish(reactContext, this@apply, credentials)
        }

        override fun onNetworkError() {
          onNetworkError(reactContext, this@apply)
        }

        override fun onStep(newStep: XS2AWizardStep) {
          this@Xs2aReactNativeViewManager.onStep(reactContext, this@apply, newStep)
        }
      }

      setContent {
        val sessionKey by this@Xs2aReactNativeViewManager._sessionKey
        val theme by this@Xs2aReactNativeViewManager._theme
        val language by this@Xs2aReactNativeViewManager._language

        sessionKey?.let {
          XS2AWizard(
            sessionKey = it,
            callbackListener = xs2aCallbackListener,
            theme = theme,
            language = language
          )
        }
      }
    }
  }

  @ReactProp(name = "wizardSessionKey")
  fun setSessionKey(view: View, sessionKey: String) {
    _sessionKey.value = sessionKey
  }

  @ReactProp(name = "styleProvider")
  fun setTheme(view: View, styleProvider: ReadableMap? = null) {
    if (styleProvider == null) {
      _theme.value = null

      return
    }

    styleProvider.apply {
      _theme.value = XS2ATheme(
        tintColor = getXS2AColor("tintColor", PRIMARY_COLOR),
        logoVariation = LogoVariation.valueOf(getString("logoVariation") ?: LOGO_VARIATION),
        backgroundColor = getXS2AColor("backgroundColor", WHITE),
        textColor = getXS2AColor("textColor", BLACK),
        inputBackgroundColor = getXS2AColor("inputBackgroundColor", BACKGROUND_INPUT),
        inputShape = getXS2AShape("inputBorderRadius", SHAPE_SIZE),
        inputTextColor = getXS2AColor("inputTextColor", BLACK),
        placeholderColor = getXS2AColor("placeholderColor", DARK_GREY),
        buttonShape = getXS2AShape("buttonBorderRadius", SHAPE_SIZE),
        paragraphShape = getXS2AShape("alertBorderRadius", SHAPE_SIZE),
        submitButtonStyle = getButtonStyle("submitButtonStyle", PRIMARY_COLOR, WHITE),
        redirectButtonStyle = getButtonStyle("submitButtonStyle", PRIMARY_COLOR, WHITE),
        backButtonStyle = getButtonStyle("backButtonStyle", DARK_GREY, WHITE),
        abortButtonStyle = getButtonStyle("abortButtonStyle", DARK_GREY, WHITE),
        restartButtonStyle = getButtonStyle("restartButtonStyle", DARK_GREY, WHITE),
        errorParagraphStyle = getParagraphStyle("errorStyle", BACKGROUND_ERROR, WHITE),
        infoParagraphStyle = getParagraphStyle("infoStyle", BACKGROUND_INFO, WHITE),
        warningParagraphStyle = getParagraphStyle("warningStyle", BACKGROUND_WARNING, BLACK),
      )
    }
  }

  @ReactProp(name = "language")
  fun setLanguage(view: View, language: String) {
    _language.value = XS2AWizardLanguage.valueOf(language.uppercase())
  }

  private fun dispatchEvent(reactContext: ThemedReactContext, view: View, event: WritableMap, eventName: String) {
    reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(
      view.id,
      eventName,
      event
    )
  }

  private fun onFinish(reactContext: ThemedReactContext, view: View, credentials: String?) {
    Arguments.createMap().apply {
      putString("credentials", credentials)

      dispatchEvent(reactContext, view, this, EVENT_SUCCESS)
    }
  }

  private fun onError(reactContext: ThemedReactContext, view: View, error: XS2AWizardError) {
    Arguments.createMap().apply {
      putString("errorCode", error.code)
      putBoolean("recoverable", error.recoverable)

      dispatchEvent(reactContext, view, this, EVENT_ERROR)
    }
  }

  private fun onAbort(reactContext: ThemedReactContext, view: View) {
    dispatchEvent(reactContext, view, Arguments.createMap(), EVENT_ABORT)
  }

  private fun onNetworkError(reactContext: ThemedReactContext, view: View) {
    dispatchEvent(reactContext, view, Arguments.createMap(), EVENT_NETWORK_ERROR)
  }

  private fun onBack(reactContext: ThemedReactContext, view: View) {
    Arguments.createMap().apply {
      if (_currentStep.value != null) putString("currentStep", _currentStep.value!!.stepName)
      else putNull("currentStep")

      dispatchEvent(reactContext, view, this, EVENT_BACK)
    }
  }

  private fun onStep(reactContext: ThemedReactContext, view: View, newStep: XS2AWizardStep?) {
    _currentStep.value = newStep

    Arguments.createMap().apply {
      putString("newStep", newStep?.stepName)

      dispatchEvent(reactContext, view, this, EVENT_STEP)
    }
  }

  companion object {
    private const val EVENT_SUCCESS = "success"
    private const val EVENT_SUCCESS_REGISTRATION_NAME = "onSuccess"

    private const val EVENT_ABORT = "abort"
    private const val EVENT_ABORT_REGISTRATION_NAME = "onAbort"

    private const val EVENT_ERROR = "error"
    private const val EVENT_ERROR_REGISTRATION_NAME = "onSessionError"

    private const val EVENT_NETWORK_ERROR = "networkError"
    private const val EVENT_NETWORK_ERROR_REGISTRATION_NAME = "onNetworkError"

    private const val EVENT_BACK = "back"
    private const val EVENT_BACK_REGISTRATION_NAME = "onBackButtonTapped"

    private const val EVENT_STEP = "step"
    private const val EVENT_STEP_REGISTRATION_NAME = "onStep"

    private const val PRIMARY_COLOR = "#427783"
    private const val BACKGROUND_INPUT = "#14000000"
    private const val BACKGROUND_ERROR = "#EA544A"
    private const val BACKGROUND_INFO = "#0E9EC2"
    private const val BACKGROUND_WARNING = "#FEAE22"
    private const val WHITE = "#ffffff"
    private const val BLACK = "#000000"
    private const val DARK_GREY = "#808080"
    private const val LOGO_VARIATION = "STANDARD"
    private const val SHAPE_SIZE = 4.0
  }
}
