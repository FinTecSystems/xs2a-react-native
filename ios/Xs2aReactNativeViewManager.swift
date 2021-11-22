import XS2AiOS

@objc(Xs2aReactNativeViewManager)
class Xs2aReactNativeViewManager: RCTViewManager {

	override func view() -> (Xs2aReactNativeView) {
		return Xs2aReactNativeView()
	}
}

class Xs2aReactNativeView: UIView {
	@objc var onSuccess: RCTDirectEventBlock?
	@objc var onAbort: RCTDirectEventBlock?
	@objc var onNetworkError: RCTDirectEventBlock?

	var xs2aViewController: XS2AViewController?
	var xs2aConfig: XS2AiOS.Configuration?
	
	@objc
	var wizardSessionKey: String = "" {
		didSet {
			xs2aConfig = XS2AiOS.Configuration(wizardSessionKey:
			wizardSessionKey)
			setNeedsLayout()
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
		
		xs2aConfig = XS2AiOS.Configuration(wizardSessionKey: wizardSessionKey)
		XS2AiOS.configure(withConfig: xs2aConfig!, withStyle: XS2AiOS.StyleProvider())

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
