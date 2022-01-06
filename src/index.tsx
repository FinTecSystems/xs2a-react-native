import { requireNativeComponent, ViewStyle } from 'react-native';

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
  onSessionError: Function;
  onBackButtonTapped: Function;
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

export const Xs2aReactNativeViewManager =
  requireNativeComponent<Xs2aReactNativeProps>('Xs2aReactNativeView');

export default Xs2aReactNativeViewManager;
