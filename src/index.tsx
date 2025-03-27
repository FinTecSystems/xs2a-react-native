import {
  requireNativeComponent,
  UIManager,
  NativeModules,
  Platform,
  type ViewStyle,
} from 'react-native';

const LINKING_ERROR =
  `The package '@fintecsystems/xs2a-react-native' doesn't seem to be linked. Make sure: \n\n` +
  Platform.select({ ios: "- You have run 'pod install'\n", default: '' }) +
  '- You rebuilt the app after installing the package\n' +
  '- You are not using Expo Go\n';

type ButtonStyle = {
  textColor: String;
  backgroundColor: String;
};

type AlertStyle = {
  textColor: String;
  backgroundColor: String;
};

interface SuccessCallback {
  nativeEvent: {
    credentials: string | undefined;
  };
}

interface BackButtonTappedCallback {
  nativeEvent: {
    currentStep: string | undefined;
  };
}

interface SessionErrorCallback {
  nativeEvent: {
    errorCode: string | undefined;
    recoverable: boolean;
  };
}

interface OnStepCallback {
  nativeEvent: {
    newStep: string | undefined;
  };
}

type Xs2aReactNativeProps = {
  wizardSessionKey: string;
  language?: 'de' | 'en' | 'fr' | 'es' | 'it';
  onSuccess: (arg0: SuccessCallback) => void;
  onAbort: Function;
  onNetworkError: Function;
  onSessionError: (arg0: SessionErrorCallback) => void;
  onBackButtonTapped: (arg0: BackButtonTappedCallback) => void;
  onStep: (arg0: OnStepCallback) => void;
  styleProvider?: {
    font?: String;
    tintColor?: String;
    logoVariation?: 'STANDARD' | 'WHITE' | 'BLACK';
    backgroundColor?: String;
    textColor?: String;
    inputBackgroundColor?: String;
    inputBorderRadius?: Number;
    inputTextColor?: String;
    placeholderColor?: String;
    buttonBorderRadius?: Number;
    submitButtonStyle?: ButtonStyle;
    backButtonStyle?: ButtonStyle;
    abortButtonStyle?: ButtonStyle;
    restartButtonStyle?: ButtonStyle;
    alertBorderRadius?: Number;
    errorStyle?: AlertStyle;
    warningStyle?: AlertStyle;
    infoStyle?: AlertStyle;
  };
  style: ViewStyle;
};

const ComponentName = 'Xs2aReactNativeView';

export const Xs2aReactNativeViewManager =
  UIManager.getViewManagerConfig(ComponentName) != null
    ? requireNativeComponent<Xs2aReactNativeProps>(ComponentName)
    : () => {
        throw new Error(LINKING_ERROR);
      };

const Xs2aReactNativeModule = NativeModules.Xs2aReactNativeModule
  ? NativeModules.Xs2aReactNativeModule
  : new Proxy(
      {},
      {
        get() {
          throw new Error(LINKING_ERROR);
        },
      }
    );

export function clearCredentials(): void {
  return Xs2aReactNativeModule.clearCredentials();
}

export default Xs2aReactNativeViewManager;
