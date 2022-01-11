package com.xs2areactnative

import com.fintecsystems.xs2awizard.components.theme.styles.ButtonStyle
import com.fintecsystems.xs2awizard.components.theme.styles.ParagraphStyle
import com.fintecsystems.xs2awizard.components.theme.support.SupportColor

internal inline fun <reified T> Map<String, Any>.getOrNull(key: String): T? =
  this.getOrDefault(key, null).let {
    if (it is T) it
    else null
  }

internal fun Map<String, Any>.toButtonStyle(
  key: String,
  defaultBackgroundColor: String,
  defaultTextColor: String
): ButtonStyle {
  this.getOrNull<Map<String, Any>>(key).apply {
    return ButtonStyle(
      SupportColor(this?.getOrNull<String>("backgroundColor") ?: defaultBackgroundColor),
      SupportColor(this?.getOrNull<String>("textColor") ?: defaultTextColor),
    )
  }
}

internal fun Map<String, Any>.toParagraphStyle(
  key: String,
  defaultBackgroundColor: String,
  defaultTextColor: String
): ParagraphStyle {
  this.getOrNull<Map<String, Any>>(key).apply {
    return ParagraphStyle(
      SupportColor(this?.getOrNull<String>("backgroundColor") ?: defaultBackgroundColor),
      SupportColor(this?.getOrNull<String>("textColor") ?: defaultTextColor),
    )
  }
}
