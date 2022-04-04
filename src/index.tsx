import {
  findNodeHandle,
  Platform,
  requireNativeComponent,
  NativeModules,
  UIManager,
  ViewStyle,
} from 'react-native';
import * as React from 'react';

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

type Xs2aReactNativeProps = {
  wizardSessionKey: string;
  onSuccess: (arg0: SuccessCallback) => void;
  onAbort: Function;
  onNetworkError: Function;
  onSessionError: (arg0: SessionErrorCallback) => void;
  onBackButtonTapped: (arg0: BackButtonTappedCallback) => void;
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

const NativeViewManager = requireNativeComponent<Xs2aReactNativeProps>(
  'Xs2aReactNativeView'
);

const NativeModule = NativeModules.Xs2aReactNativeModule;

const AndroidView = (props: Xs2aReactNativeProps) => {
  const ref = React.useRef(null);

  React.useEffect(() => {
    const viewId = findNodeHandle(ref.current);

    requestAnimationFrame(() => {
      UIManager.dispatchViewManagerCommand(
        viewId,
        UIManager.getViewManagerConfig('Xs2aReactNativeView').Commands.create,
        [viewId]
      );
    });
  }, []);

  return <NativeViewManager ref={ref} {...props} />;
};

export const Xs2aReactNativeViewManager =
  Platform.OS === 'android' ? AndroidView : NativeViewManager;

export const clearCredentials = () => {
  NativeModule.clearCredentials();
};

export default Xs2aReactNativeViewManager;
