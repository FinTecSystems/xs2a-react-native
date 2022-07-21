package com.xs2areactnative

import android.view.Choreographer
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.fragment.app.FragmentActivity
import com.facebook.react.bridge.*
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.ViewGroupManager
import com.facebook.react.uimanager.annotations.ReactProp
import com.facebook.react.uimanager.events.RCTEventEmitter
import com.fintecsystems.xs2awizard.components.XS2AWizardConfig
import com.fintecsystems.xs2awizard.components.XS2AWizardError
import com.fintecsystems.xs2awizard.components.XS2AWizardStep
import com.fintecsystems.xs2awizard.components.XS2AWizardLanguage
import com.fintecsystems.xs2awizard.components.theme.IXS2ATheme
import com.fintecsystems.xs2awizard.components.theme.styles.LogoVariation
import com.fintecsystems.xs2awizard.components.theme.support.SupportColor
import com.fintecsystems.xs2awizard.components.theme.support.SupportShape
import com.fintecsystems.xs2awizard.components.theme.support.XS2ASupportTheme
import com.fintecsystems.xs2awizard.wrappers.XS2AWizardFragment
import kotlin.math.roundToInt

class Xs2aReactNativeViewManager(private val reactContext: ReactContext) :
  ViewGroupManager<FrameLayout>() {
  private var sessionKey: String? = null
  private var currentStep: XS2AWizardStep? = null
  private var theme: IXS2ATheme? = null
  private var language: XS2AWizardLanguage? = null

  override fun getName() = "Xs2aReactNativeView"

  override fun getCommandsMap() = mutableMapOf(Pair(CREATE_COMMAND, CREATE_COMMAND_ID))

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
      MapBuilder.of("registrationName", EVENT_BACK_REGISTRATION_NAME)
    )

  override fun createViewInstance(reactContext: ThemedReactContext) = FrameLayout(reactContext)

  override fun receiveCommand(root: FrameLayout, commandId: Int, args: ReadableArray?) {
    val reactNativeViewId = args?.getInt(0)

    if (commandId == CREATE_COMMAND_ID) {
      createFragment(root, reactNativeViewId!!)
    }
  }

  private fun createFragment(root: FrameLayout, reactNativeViewId: Int) {
    val parentView = root.findViewById<ViewGroup>(reactNativeViewId)
    setupLayout(parentView)

    val xs2aWizard = XS2AWizardFragment(
      XS2AWizardConfig(
        sessionKey!!,
        onFinish = { onFinish(parentView, it) },
        onAbort = { onAbort(parentView) },
        onError = { onError(parentView, it) },
        onNetworkError = { onNetworkError(parentView) },
        onBack = { onBack(parentView) },
        onStep = ::onStep,
        theme = theme,
        language = language
      )
    )

    val activity = reactContext.currentActivity as FragmentActivity
    activity.supportFragmentManager
      .beginTransaction()
      .replace(reactNativeViewId, xs2aWizard, reactNativeViewId.toString())
      .commit()
  }

  private fun setupLayout(view: View) {
    Choreographer.getInstance().postFrameCallback(object : Choreographer.FrameCallback {
      override fun doFrame(p0: Long) {
        manuallyLayoutChildren(view)
        view.viewTreeObserver.dispatchOnGlobalLayout()

        Choreographer.getInstance().postFrameCallback(this)
      }
    })
  }

  private fun manuallyLayoutChildren(view: View) {
    view.measure(
      View.MeasureSpec.makeMeasureSpec(view.width, View.MeasureSpec.EXACTLY),
      View.MeasureSpec.makeMeasureSpec(view.height, View.MeasureSpec.EXACTLY)
    )

    view.layout(view.left, view.top, view.right, view.bottom)
  }

  @ReactProp(name = "wizardSessionKey")
  fun setSessionKey(view: View, _sessionKey: String) {
    sessionKey = _sessionKey
  }

  @ReactProp(name = "styleProvider")
  fun setTheme(view: View, styleProvider: ReadableMap? = null) {
    if (styleProvider == null) {
      theme = null

      return
    }

    styleProvider.toHashMap().apply {
      theme = XS2ASupportTheme(
        tintColor = SupportColor(getOrNull<String>("tintColor") ?: PRIMARY_COLOR),
        logoVariation = LogoVariation.valueOf(getOrNull<String>("logoVariation") ?: LOGO_VARIATION),
        backgroundColor = SupportColor(getOrNull<String>("backgroundColor") ?: WHITE),
        textColor = SupportColor(getOrNull<String>("textColor") ?: BLACK),
        inputBackgroundColor = SupportColor(
          getOrNull<String>("inputBackgroundColor") ?: BACKGROUND_INPUT
        ),
        inputShape = SupportShape(
          (getOrNull<Double>("inputBorderRadius") ?: SHAPE_SIZE).roundToInt(),
          SupportShape.ShapeType.ROUNDED
        ),
        inputTextColor = SupportColor(getOrNull<String>("inputTextColor") ?: BLACK),
        placeholderColor = SupportColor(getOrNull<String>("placeholderColor") ?: DARK_GREY),
        buttonShape = SupportShape(
          (getOrNull<Double>("buttonBorderRadius") ?: SHAPE_SIZE).roundToInt(),
          SupportShape.ShapeType.ROUNDED
        ),
        paragraphShape = SupportShape(
          (getOrNull<Double>("alertBorderRadius") ?: SHAPE_SIZE).roundToInt(),
          SupportShape.ShapeType.ROUNDED
        ),
        submitButtonStyle = toButtonStyle("submitButtonStyle", PRIMARY_COLOR, WHITE),
        redirectButtonStyle = toButtonStyle("submitButtonStyle", PRIMARY_COLOR, WHITE),
        backButtonStyle = toButtonStyle("backButtonStyle", DARK_GREY, WHITE),
        abortButtonStyle = toButtonStyle("abortButtonStyle", DARK_GREY, WHITE),
        restartButtonStyle = toButtonStyle("restartButtonStyle", DARK_GREY, WHITE),
        errorParagraphStyle = toParagraphStyle("errorStyle", BACKGROUND_ERROR, WHITE),
        infoParagraphStyle = toParagraphStyle("infoStyle", BACKGROUND_INFO, WHITE),
        warningParagraphStyle = toParagraphStyle("warningStyle", BACKGROUND_WARNING, BLACK),
      )
    }
  }

  @ReactProp(name = "language")
  fun setLanguage(view: View, _language: String) {
    language = XS2AWizardLanguage.valueOf(_language.uppercase())
  }

  private fun dispatchEvent(view: View, event: WritableMap, eventName: String) {
    reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(
      view.id,
      eventName,
      event
    )
  }

  private fun onFinish(view: View, credentials: String?) {
    Arguments.createMap().apply {
      putString("credentials", credentials)

      dispatchEvent(view, this, EVENT_SUCCESS)
    }
  }

  private fun onError(view: View, error: XS2AWizardError) {
    Arguments.createMap().apply {
      putString("errorCode", error.code)
      putBoolean("recoverable", error.recoverable)

      dispatchEvent(view, this, EVENT_ERROR)
    }
  }

  private fun onAbort(view: View) {
    dispatchEvent(view, Arguments.createMap(), EVENT_ABORT)
  }

  private fun onNetworkError(view: View) {
    dispatchEvent(view, Arguments.createMap(), EVENT_NETWORK_ERROR)
  }

  private fun onBack(view: View) {
    Arguments.createMap().apply {
      if (currentStep != null) putString("currentStep", currentStep!!.stepName)
      else putNull("currentStep")

      dispatchEvent(view, this, EVENT_BACK)
    }
  }

  private fun onStep(newStep: XS2AWizardStep?) {
    currentStep = newStep
  }

  companion object {
    private const val CREATE_COMMAND = "create"
    private const val CREATE_COMMAND_ID = 1

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
