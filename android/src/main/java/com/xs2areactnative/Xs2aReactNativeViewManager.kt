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
import com.fintecsystems.xs2awizard.XS2AWizard

class Xs2aReactNativeViewManager(private val reactContext: ReactContext) : ViewGroupManager<FrameLayout>() {
  private var sessionKey: String? = null

  override fun getName() = "Xs2aReactNativeView"

  override fun getCommandsMap() = mutableMapOf(Pair(CREATE_COMMAND, CREATE_COMMAND_ID))

  override fun getExportedCustomDirectEventTypeConstants(): Map<String, Map<String, String>> = MapBuilder.of(
    EVENT_SUCCESS,
    MapBuilder.of("registrationName", EVENT_SUCCESS_REGISTRATION_NAME),
    EVENT_ABORT,
    MapBuilder.of("registrationName", EVENT_ABORT_REGISTRATION_NAME),
    EVENT_ERROR,
    MapBuilder.of("registrationName", EVENT_ERROR_REGISTRATION_NAME)
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

    val xs2aWizard = XS2AWizard(XS2AWizard.XS2AWizardConfig(
      sessionKey!!,
      onFinish = { onFinish(parentView, it) },
      onAbort = { onAbort(parentView) },
      onError = { onError(parentView, it) }
    ))

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

  private fun dispatchEvent(view: View, event: WritableMap, eventName: String) {
    reactContext.getJSModule(RCTEventEmitter::class.java).receiveEvent(
      view.id,
      eventName,
      event
    )
  }

  private fun onFinish(view: View, credentials: String?) {
    val event = Arguments.createMap()
    event.putString("credentials", credentials)

    dispatchEvent(view, event, EVENT_SUCCESS)
  }

  private fun onError(view: View, error: XS2AWizard.XS2AWizardError) {
    val event = Arguments.createMap()
    event.putString("error", error.toString())

    dispatchEvent(view, event, EVENT_ERROR)
  }

  private fun onAbort(view: View) {
    dispatchEvent(view, Arguments.createMap(), EVENT_ABORT)
  }

  companion object {
    private const val CREATE_COMMAND = "create"
    private const val CREATE_COMMAND_ID = 1

    private const val EVENT_SUCCESS = "success"
    private const val EVENT_SUCCESS_REGISTRATION_NAME = "onSuccess"

    private const val EVENT_ABORT = "abort"
    private const val EVENT_ABORT_REGISTRATION_NAME = "onAbort"

    private const val EVENT_ERROR = "error"
    private const val EVENT_ERROR_REGISTRATION_NAME = "onError"
  }
}
