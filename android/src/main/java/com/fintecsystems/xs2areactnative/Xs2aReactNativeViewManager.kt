package com.fintecsystems.xs2areactnative

import android.graphics.Color
import android.app.Activity
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.ViewModelProvider
import java.util.UUID
import java.util.WeakHashMap
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.lifecycle.ViewModelStoreOwner
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.fintecsystems.xs2awizard.XS2AWizard
import com.fintecsystems.xs2awizard.components.XS2AWizardCallbackListener
import com.fintecsystems.xs2awizard.components.XS2AWizardError
import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import com.fintecsystems.xs2awizard.components.XS2AWizardStep
import com.fintecsystems.xs2awizard.components.XS2AWizardViewModel
import com.fintecsystems.xs2awizard.components.theme.XS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.LogoVariation

class Xs2aReactNativeViewManager : SimpleViewManager<View>() {
  // The ViewManager is a singleton shared by all views, so state must be kept per view.
  private class ViewState {
    val currentStep = mutableStateOf<XS2AWizardStep?>(null)
    val sessionKey = mutableStateOf<String?>(null)
    val theme = mutableStateOf<XS2ATheme?>(null)
    val language = mutableStateOf<XS2AWizardLanguage?>(null)
    val viewModelKey = UUID.randomUUID().toString()
  }

  private val viewStates = WeakHashMap<View, ViewState>()

  private fun stateFor(view: View): ViewState = viewStates.getOrPut(view) { ViewState() }

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
    // The SDK casts the Compose context to (Fragment)Activity, so the ComposeView must be
    // created with the activity itself.
    val activity = requireNotNull(reactContext.reactApplicationContext.currentActivity) {
      "Xs2aReactNativeView requires a current Activity."
    }

    // Fabric may measure a view before it is attached to a window, which crashes a ComposeView
    // ("Cannot locate windowRecomposer"). ComposeView is final, so defer creating it until
    // this wrapper is attached.
    return object : FrameLayout(activity) {
      override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (childCount == 0) {
          addView(
            createComposeView(reactContext, activity, this),
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
          )
        }
      }
    }
  }

  private fun createComposeView(
    reactContext: ThemedReactContext,
    activity: Activity,
    container: View
  ): ComposeView {
    return ComposeView(activity).apply {
      setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnDetachedFromWindowOrReleasedFromPool)

      val xs2aCallbackListener = object : XS2AWizardCallbackListener {
        override fun onAbort() {
          onAbort(reactContext, container)
        }

        override fun onBack() {
          onBack(reactContext, container)
        }

        override fun onError(xs2aWizardError: XS2AWizardError) {
          onError(reactContext, container, xs2aWizardError)
        }

        override fun onFinish(credentials: String?) {
          onFinish(reactContext, container, credentials)
        }

        override fun onNetworkError() {
          onNetworkError(reactContext, container)
        }

        override fun onStep(newStep: XS2AWizardStep) {
          this@Xs2aReactNativeViewManager.onStep(reactContext, container, newStep)
        }
      }

      val state = stateFor(container)

      setContent {
        // A dedicated ViewModel per view, so concurrent or consecutive wizard views never share
        // (or stop) each other's session.
        val viewModel = remember {
          ViewModelProvider(activity as ViewModelStoreOwner)[state.viewModelKey, XS2AWizardViewModel::class.java]
        }
        val sessionKey by state.sessionKey
        val theme by state.theme
        val language by state.language

        sessionKey?.let {
            XS2AWizard(
              sessionKey = it,
              callbackListener = xs2aCallbackListener,
              theme = theme,
              language = language,
              xs2aWizardViewModel = viewModel
            )
        }
      }
    }
  }

  @ReactProp(name = "wizardSessionKey")
  fun setSessionKey(view: View, sessionKey: String) {
    stateFor(view).sessionKey.value = sessionKey
  }

  @ReactProp(name = "styleProvider")
  fun setTheme(view: View, styleProvider: ReadableMap? = null) {
    if (styleProvider == null) {
      stateFor(view).theme.value = null

      return
    }

    // TODO: Add font configuration.

    styleProvider.apply {
      stateFor(view).theme.value = XS2ATheme(
        primaryColor = getXS2AColor("tintColor", PRIMARY_COLOR),
        onPrimaryColor = getXS2AColor("onTintColor", WHITE),
        errorColor = getXS2AColor("errorColor", ERROR_COLOR),
        logoVariation = LogoVariation.valueOf(getString("logoVariation") ?: LOGO_VARIATION),
        backgroundColor = getXS2AColor("backgroundColor", WHITE),
        textColor = getXS2AColor("textColor", BLACK),
        inputBackgroundColor = getXS2AColor("inputBackgroundColor", BACKGROUND_INPUT),
        inputShape = getXS2AShape("inputBorderRadius", SHAPE_SIZE),
        // inputType = getXS2AShape("inputType", SHAPE_SIZE), // TODO
        surfaceColor = getXS2AColor("surfaceColor", WHITE),
        onSurfaceColor = getXS2AColor("onSurfaceColor", BLACK),
        onSurfaceVariantColor = getXS2AColor("placeholderColor", DARK_GREY),
        loadingIndicatorBackgroundColor = getXS2AColor("loadingIndicatorBackgroundColor", BACKGROUND_TRANSLUCENT), // TODO
        // buttonShape = getXS2AShape("buttonBorderRadius", SHAPE_SIZE), // TODO
        // buttonSize = getXS2AShape("buttonSize", SHAPE_SIZE), // TODO
        // buttonHorizontalAlignment = getXS2AShape("buttonHorizontalAlignment", SHAPE_SIZE), // TODO
        paragraphShape = getXS2AShape("alertBorderRadius", SHAPE_SIZE),
        // paragraphPadding = getXS2AShape("paragraphPadding", SHAPE_SIZE), // TODO
        // paragraphMargin = getXS2AShape("paragraphMargin", SHAPE_SIZE), // TODO
        submitButtonStyle = getButtonStyle("submitButtonStyle", PRIMARY_COLOR, WHITE),
        redirectButtonStyle = getButtonStyle("submitButtonStyle", PRIMARY_COLOR, WHITE),
        backButtonStyle = getButtonStyle("backButtonStyle", DARK_GREY, WHITE),
        abortButtonStyle = getButtonStyle("abortButtonStyle", DARK_GREY, WHITE),
        restartButtonStyle = getButtonStyle("restartButtonStyle", DARK_GREY, WHITE),
        errorParagraphStyle = getParagraphStyle("errorStyle", BACKGROUND_ERROR, WHITE),
        infoParagraphStyle = getParagraphStyle("infoStyle", BACKGROUND_INFO, WHITE),
        warningParagraphStyle = getParagraphStyle("warningStyle", BACKGROUND_WARNING, BLACK),
        connectionStatusBannerBackgroundColor = getXS2AColor("connectionStatusBannerBackgroundColor", BACKGROUND_WARNING),
        connectionStatusBannerTextColor = getXS2AColor("connectionStatusBannerTextColor", BLACK),
      )
    }
  }

  @ReactProp(name = "language")
  fun setLanguage(view: View, language: String) {
    stateFor(view).language.value = XS2AWizardLanguage.valueOf(language.uppercase())
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
      val currentStep = stateFor(view).currentStep.value
      if (currentStep != null) putString("currentStep", currentStep.stepName)
      else putNull("currentStep")

      dispatchEvent(reactContext, view, this, EVENT_BACK)
    }
  }

  private fun onStep(reactContext: ThemedReactContext, view: View, newStep: XS2AWizardStep?) {
    stateFor(view).currentStep.value = newStep

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
    private const val ERROR_COLOR = "#DB271A"
    private const val BACKGROUND_INPUT = "#F5F5F5"
    private const val BACKGROUND_ERROR = "#DB271A"
    private const val BACKGROUND_INFO = "#0B809D"
    private const val BACKGROUND_WARNING = "#FEAE22"
    private const val BACKGROUND_TRANSLUCENT = "#AAFFFFFF"
    private const val WHITE = "#ffffff"
    private const val BLACK = "#000000"
    private const val DARK_GREY = "#808080"
    private const val LOGO_VARIATION = "STANDARD"
    private const val SHAPE_SIZE = 4.0
  }
}
