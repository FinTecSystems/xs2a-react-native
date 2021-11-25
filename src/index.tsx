import { findNodeHandle, Platform, requireNativeComponent, UIManager, ViewStyle } from 'react-native';
import * as React from 'react';

type ButtonStyle = {
  textColor: String;
  backgroundColor: String;
};

type AlertStyle = {
  textColor: String;
  backgroundColor: String;
};

type Xs2aReactNativeProps = {
  wizardSessionKey: string;
  onSuccess: Function;
  onAbort: Function;
  onNetworkError: Function;
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

const NativeViewManager = requireNativeComponent<Xs2aReactNativeProps>('Xs2aReactNativeView');

const AndroidView = (props: Xs2aReactNativeProps) => {
  const ref = React.useRef(null);

  React.useEffect(() => {
    const viewId = findNodeHandle(ref.current);

    UIManager.dispatchViewManagerCommand(
      viewId,
      UIManager.getViewManagerConfig('Xs2aReactNativeView').Commands.create,
      [viewId]
    )
  }, []);

  return (
    <NativeViewManager
      ref={ref}
      {...props}
    />
  )
}

export const Xs2aReactNativeViewManager = Platform.OS === 'android' ? AndroidView : NativeViewManager;

export default Xs2aReactNativeViewManager;
