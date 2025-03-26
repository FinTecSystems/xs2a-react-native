import Foundation
import XS2AiOS

@objc(Xs2aReactNativeModule)
class Xs2aReactNativeModule: NSObject {
	@objc
	public func clearCredentials() {
		do {
			try XS2AiOS.clearKeychain()
		} catch _ {
			
		}
	}
	
	@objc static func requiresMainQueueSetup() -> Bool {
		return true
	}
}
