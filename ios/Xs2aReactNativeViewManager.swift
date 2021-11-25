import XS2AiOS

@objc(Xs2aReactNativeViewManager)
class Xs2aReactNativeViewManager: RCTViewManager {
	override func view() -> (Xs2aReactNativeView) {
		return Xs2aReactNativeView()
	}
	
	override static func requiresMainQueueSetup() -> Bool {
		return true
	}
	
	override func constantsToExport() -> [AnyHashable : Any]! {
		return [:]
	}
}

class Xs2aReactNativeView: UIView {
	@objc var onSuccess: RCTDirectEventBlock?
	@objc var onAbort: RCTDirectEventBlock?
	@objc var onNetworkError: RCTDirectEventBlock?

	var xs2aViewController: XS2AViewController?
	var xs2aConfig: XS2AiOS.Configuration?
	var style: XS2AiOS.StyleProvider?
	
	@objc
	var wizardSessionKey: String = "" {
		didSet {
			xs2aConfig = XS2AiOS.Configuration(wizardSessionKey: wizardSessionKey)
			setNeedsLayout()
		}
	}
	
	@objc
	var styleProvider: NSDictionary = [:] {
		didSet {
			var font: FontName = .systemDefault
			var tintColor: UIColor = UIColor(red: 0.11, green: 0.45, blue: 0.72, alpha: 1.0)
			var logoVariation: XS2AiOS.StyleProvider.LogoVariation = .standard
			var backgroundColor: UIColor = .white
			var textColor: UIColor = UIColor(red: 0.27, green: 0.27, blue: 0.28, alpha: 1.0)
			var inputBackgroundColor: UIColor = UIColor(red: 0.91, green: 0.95, blue: 0.97, alpha: 1.0)
			var inputBorderRadius: CGFloat = 6
			var inputTextColor: UIColor = UIColor(red: 0.27, green: 0.27, blue: 0.28, alpha: 1.0)
			var placeholderColor: UIColor = .systemGray
			var buttonBorderRadius: CGFloat = 6
			var submitButtonStyle = XS2AiOS.ButtonStyle(textColor: .white, backgroundColor: UIColor(red: 0.11, green: 0.45, blue: 0.72, alpha: 1.0))
			var backButtonStyle = XS2AiOS.ButtonStyle(textColor: .white, backgroundColor: .systemGray)
			var abortButtonStyle = XS2AiOS.ButtonStyle(textColor: .white, backgroundColor: .systemGray)
			var restartButtonStyle = XS2AiOS.ButtonStyle(textColor: .white, backgroundColor: .systemGray)
			var alertBorderRadius: CGFloat = 6
			var errorStyle = XS2AiOS.AlertStyle(textColor: .white, backgroundColor: .systemRed)
			var warningStyle = XS2AiOS.AlertStyle(textColor: .black, backgroundColor: .systemOrange)
			var infoStyle = XS2AiOS.AlertStyle(textColor: .white, backgroundColor: UIColor(red: 0.11, green: 0.45, blue: 0.72, alpha: 1.0))
																   
			if let providedFont = styleProvider["font"] as? String {
				font = .custom(providedFont)
			}
			
			if let providedTintColor = styleProvider["tintColor"] as? String {
				if let hexColor = UIColor(hex: providedTintColor) {
					tintColor = hexColor
				}
			}
			
			if let providedLogoVariation = styleProvider["logoVariation"] as? String {
				if (providedLogoVariation == "BLACK") {
					logoVariation = .black
				} else if (providedLogoVariation == "WHITE") {
					logoVariation = .white
				}
			}
			
			if let providedBackgroundColor = styleProvider["backgroundColor"] as? String {
				if let hexColor = UIColor(hex: providedBackgroundColor) {
					backgroundColor = hexColor
				}
			}
			
			if let providedTextColor = styleProvider["textColor"] as? String {
				if let hexColor = UIColor(hex: providedTextColor) {
					textColor = hexColor
				}
			}
			
			if let providedInputBackgroundColor = styleProvider["inputBackgroundColor"] as? String {
				if let hexColor = UIColor(hex: providedInputBackgroundColor) {
					inputBackgroundColor = hexColor
				}
			}
			
			if let providedInputBorderRadius = styleProvider["inputBorderRadius"] as? CGFloat {
				inputBorderRadius = providedInputBorderRadius
			}
			
			if let providedInputTextColor = styleProvider["inputTextColor"] as? String {
				if let hexColor = UIColor(hex: providedInputTextColor) {
					inputTextColor = hexColor
				}
			}
			
			if let providedPlaceholderColor = styleProvider["placeholderColor"] as? String {
				if let hexColor = UIColor(hex: providedPlaceholderColor) {
					placeholderColor = hexColor
				}
			}
			
			if let providedButtonBorderRadius = styleProvider["buttonBorderRadius"] as? CGFloat {
				buttonBorderRadius = providedButtonBorderRadius
			}
			
			if let providedSubmitButtonStyle = styleProvider["submitButtonStyle"] as? NSDictionary {
				var backgroundColor = UIColor(red: 0.11, green: 0.45, blue: 0.72, alpha: 1.0)
				var textColor: UIColor = .white
				
				if let providedBackgroundColor = providedSubmitButtonStyle["backgroundColor"] as? String {
					if let hexColor = UIColor(hex: providedBackgroundColor) {
						backgroundColor = hexColor
					}
				}
				
				if let providedTextColor = providedSubmitButtonStyle["textColor"] as? String {
					if let hexColor = UIColor(hex: providedTextColor) {
						textColor = hexColor
					}
				}
				
				submitButtonStyle = XS2AiOS.ButtonStyle(textColor: textColor, backgroundColor: backgroundColor)
			}
			
			if let providedBackButtonStyle = styleProvider["backButtonStyle"] as? NSDictionary {
				var backgroundColor: UIColor = .systemGray
				var textColor: UIColor = .white
				
				if let providedBackgroundColor = providedBackButtonStyle["backgroundColor"] as? String {
					if let hexColor = UIColor(hex: providedBackgroundColor) {
						backgroundColor = hexColor
					}
				}
				
				if let providedTextColor = providedBackButtonStyle["textColor"] as? String {
					if let hexColor = UIColor(hex: providedTextColor) {
						textColor = hexColor
					}
				}
				
				backButtonStyle = XS2AiOS.ButtonStyle(textColor: textColor, backgroundColor: backgroundColor)
			}
			
			if let providedAbortButtonStyle = styleProvider["abortButtonStyle"] as? NSDictionary {
				var backgroundColor: UIColor = .systemGray
				var textColor: UIColor = .white
				
				if let providedBackgroundColor = providedAbortButtonStyle["backgroundColor"] as? String {
					if let hexColor = UIColor(hex: providedBackgroundColor) {
						backgroundColor = hexColor
					}
				}
				
				if let providedTextColor = providedAbortButtonStyle["textColor"] as? String {
					if let hexColor = UIColor(hex: providedTextColor) {
						textColor = hexColor
					}
				}
				
				abortButtonStyle = XS2AiOS.ButtonStyle(textColor: textColor, backgroundColor: backgroundColor)
			}
			
			if let providedRestartButtonStyle = styleProvider["restartButtonStyle"] as? NSDictionary {
				var backgroundColor: UIColor = .systemGray
				var textColor: UIColor = .white
				
				if let providedBackgroundColor = providedRestartButtonStyle["backgroundColor"] as? String {
					if let hexColor = UIColor(hex: providedBackgroundColor) {
						backgroundColor = hexColor
					}
				}
				
				if let providedTextColor = providedRestartButtonStyle["textColor"] as? String {
					if let hexColor = UIColor(hex: providedTextColor) {
						textColor = hexColor
					}
				}
				
				restartButtonStyle = XS2AiOS.ButtonStyle(textColor: textColor, backgroundColor: backgroundColor)
			}
			
			if let providedAlertBorderRadius = styleProvider["alertBorderRadius"] as? CGFloat {
				alertBorderRadius = providedAlertBorderRadius
			}
			
			if let providedErrorStyle = styleProvider["errorStyle"] as? NSDictionary {
				var backgroundColor: UIColor = .systemRed
				var textColor: UIColor = .white
				
				if let providedBackgroundColor = providedErrorStyle["backgroundColor"] as? String {
					if let hexColor = UIColor(hex: providedBackgroundColor) {
						backgroundColor = hexColor
					}
				}
				
				if let providedTextColor = providedErrorStyle["textColor"] as? String {
					if let hexColor = UIColor(hex: providedTextColor) {
						textColor = hexColor
					}
				}
				
				errorStyle = XS2AiOS.AlertStyle(textColor: textColor, backgroundColor: backgroundColor)
			}
			
			if let providedWarningStyle = styleProvider["warningStyle"] as? NSDictionary {
				var backgroundColor: UIColor = .systemOrange
				var textColor: UIColor = .black
				
				if let providedBackgroundColor = providedWarningStyle["backgroundColor"] as? String {
					if let hexColor = UIColor(hex: providedBackgroundColor) {
						backgroundColor = hexColor
					}
				}
				
				if let providedTextColor = providedWarningStyle["textColor"] as? String {
					if let hexColor = UIColor(hex: providedTextColor) {
						textColor = hexColor
					}
				}
				
				warningStyle = XS2AiOS.AlertStyle(textColor: textColor, backgroundColor: backgroundColor)
			}
			
			if let providedInfoStyle = styleProvider["infoStyle"] as? NSDictionary {
				var backgroundColor: UIColor = UIColor(red: 0.11, green: 0.45, blue: 0.72, alpha: 1.0)
				var textColor: UIColor = .white
				
				if let providedBackgroundColor = providedInfoStyle["backgroundColor"] as? String {
					if let hexColor = UIColor(hex: providedBackgroundColor) {
						backgroundColor = hexColor
					}
				}
				
				if let providedTextColor = providedInfoStyle["textColor"] as? String {
					if let hexColor = UIColor(hex: providedTextColor) {
						textColor = hexColor
					}
				}
				
				infoStyle = XS2AiOS.AlertStyle(textColor: textColor, backgroundColor: backgroundColor)
			}

			style = XS2AiOS.StyleProvider(
				font: font,
				tintColor: tintColor,
				logoVariation: logoVariation,
				backgroundColor: backgroundColor,
				textColor: textColor,
				inputBackgroundColor: inputBackgroundColor,
				inputBorderRadius: inputBorderRadius,
				inputTextColor: inputTextColor,
				placeholderColor: placeholderColor,
				buttonBorderRadius: buttonBorderRadius,
				submitButtonStyle: submitButtonStyle,
				backButtonStyle: backButtonStyle,
				abortButtonStyle: abortButtonStyle,
				restartButtonStyle: restartButtonStyle,
				alertBorderRadius: alertBorderRadius,
				errorStyle: errorStyle,
				warningStyle: warningStyle,
				infoStyle: infoStyle
			)
		}
	}
	
	override init(frame: CGRect) {
		super.init(frame: frame)
	}

	required init?(coder aDecoder: NSCoder) { fatalError() }

	override func layoutSubviews() {
		super.layoutSubviews()

		if xs2aViewController == nil {
			embed()
		} else {
			xs2aViewController?.view.frame = bounds
		}
	}

	private func embed() {
		guard let parentVC = parentViewController else {
			return
		}
		
		XS2AiOS.configure(
			withConfig: XS2AiOS.Configuration(wizardSessionKey: wizardSessionKey),
			withStyle: style ?? XS2AiOS.StyleProvider()
		)

		let vc = XS2AViewController { result in
			switch result {
			case .success(.finish):
				self.onSuccess?([:])
				break
			case .success(.finishWithCredentials(let credentials)):
				self.onSuccess?(["credentials": credentials])
				break
			case .failure(let error):
				switch error {
				case .userAborted:
					self.onAbort?([:])
					break
				case .networkError:
					self.onNetworkError?([:])
					break
				}
			}
		}

		parentVC.addChild(vc)
		addSubview(vc.view)
		vc.view.frame = CGRect(x: 0, y: 0, width: UIScreen.main.bounds.width, height: UIScreen.main.bounds.height)
		vc.didMove(toParent: parentVC)
		self.xs2aViewController = vc
	}
}

extension UIView {
	var parentViewController: UIViewController? {
		var parentResponder: UIResponder? = self
		while parentResponder != nil {
			parentResponder = parentResponder!.next
			if let viewController = parentResponder as? UIViewController {
				return viewController
			}
		}
		return nil
	}
}


/**
 Taken from https://www.hackingwithswift.com/example-code/uicolor/how-to-convert-a-hex-color-to-a-uicolor
 */
extension UIColor {
	public convenience init?(hex: String) {
		let r, g, b, a: CGFloat

		if hex.hasPrefix("#") {
			let start = hex.index(hex.startIndex, offsetBy: 1)
			var hexColor = String(hex[start...])
			
			if hexColor.count == 6 {
				hexColor += "ff"
			}

			if hexColor.count == 8 {
				let scanner = Scanner(string: hexColor)
				var hexNumber: UInt64 = 0

				if scanner.scanHexInt64(&hexNumber) {
					r = CGFloat((hexNumber & 0xff000000) >> 24) / 255
					g = CGFloat((hexNumber & 0x00ff0000) >> 16) / 255
					b = CGFloat((hexNumber & 0x0000ff00) >> 8) / 255
					a = CGFloat(hexNumber & 0x000000ff) / 255

					self.init(red: r, green: g, blue: b, alpha: a)
					return
				}
			}
		}

		return nil
	}
}
