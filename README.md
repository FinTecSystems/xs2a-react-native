# xs2a-react-native

Integrate FinTecSystems' XS2A into your React Native App.

## Installation

```sh
npm install @fintecsystems/xs2a-react-native

cd ios/

pod install
```

## Usage

```jsx
import Xs2aReactNativeViewManager from "@fintecsystems/xs2a-react-native";

// ...

<Xs2aReactNativeViewManager
	style={{ flex: 1, width: '100%', height: '100%' }}
	wizardSessionKey="YOUR_WIZARD_SESSION_KEY"
	onSuccess={({ nativeEvent: { credentials } }) => {
		// e.g. redirect to success screen

		// 'credentials' is only provided for XS2A.API sessions
		// with sync_mode = shared
		console.log('Success');
	}}
	onAbort={() => {
		// e.g. redirect to abort screen
		console.log('Abort');
	}}
	onNetworkError={() => {
		// e.g. redirect to error screen
		console.log('Network error');
	}}
	// All styles are optional
	styleProvider={{
		font: 'Helvetica Neue',
		tintColor: '#ff0000',
		logoVariation: 'STANDARD',
		backgroundColor: '#ffffff',
		textColor: '#000000',
		inputBackgroundColor: '#059392',
		inputBorderRadius: 15,
		inputTextColor: '#000000',
		placeholderColor: '#dddddd',
		buttonBorderRadius: 9,
		submitButtonStyle: {
		textColor: '#000000',
		backgroundColor: '#dddddd',
		},
		backButtonStyle: {
		textColor: '#000000',
		backgroundColor: '#dddddd',
		},
		abortButtonStyle: {
		textColor: '#000000',
		backgroundColor: '#dddddd',
		},
		restartButtonStyle: {
		textColor: '#000000',
		backgroundColor: '#dddddd',
		},
		alertBorderRadius: 12,
		errorStyle: {
		textColor: '#000000',
		backgroundColor: '#dddddd',
		},
		warningStyle: {
		textColor: '#000000',
		backgroundColor: '#dddddd',
		},
		infoStyle: {
		textColor: '#000000',
		backgroundColor: '#dddddd',
		},
	}}
/>
```

### License

Please note that this mobile SDK is subject to the MIT license. MIT license does not apply to the logo of FinTecSystems GmbH, the terms of use and the privacy policy of FinTecSystems GmbH. The license terms of the logo of FinTecSystems GmbH, the terms of use and the privacy policy of FinTecSystems GmbH are included in the LICENSE as FTS LICENSE.