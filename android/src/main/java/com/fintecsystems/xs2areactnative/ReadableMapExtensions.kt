package com.fintecsystems.xs2areactnative

import com.facebook.react.bridge.ReadableMap
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AColor
import com.fintecsystems.xs2awizard.components.theme.interop.XS2AShape
import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle
import com.fintecsystems.xs2awizard.components.theme.styles.ParagraphStyle
import kotlin.math.roundToInt

internal fun ReadableMap.getXS2AColor(key: String, defaultColorString: String) =
  XS2AColor(getString(key) ?: defaultColorString)

internal fun ReadableMap.getButtonStyle(
  key: String,
  defaultBackgroundColor: String,
  defaultTextColor: String
): ButtonStyle {
  this.getMap(key).apply {
    return ButtonStyle(
      this?.getXS2AColor("backgroundColor", defaultBackgroundColor) ?: XS2AColor(
        defaultBackgroundColor
      ),
      this?.getXS2AColor("textColor", defaultTextColor) ?: XS2AColor(defaultTextColor),
    )
  }
}

internal fun ReadableMap.getParagraphStyle(
  key: String,
  defaultBackgroundColor: String,
  defaultTextColor: String
): ParagraphStyle {
  this.getMap(key).apply {
    return ParagraphStyle(
      this?.getXS2AColor("backgroundColor", defaultBackgroundColor) ?: XS2AColor(
        defaultBackgroundColor
      ),
      this?.getXS2AColor("textColor", defaultTextColor) ?: XS2AColor(defaultTextColor),
    )
  }
}

internal fun ReadableMap.getXS2AShape(
  key: String,
  defaultShapeSize: Double,
): XS2AShape {
  val shapeSize = if (hasKey(key)) getDouble(key)
  else defaultShapeSize

  return XS2AShape(
    shapeSize.roundToInt(),
    XS2AShape.ShapeType.ROUNDED
  )
}
