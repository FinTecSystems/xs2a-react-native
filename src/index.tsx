import { requireNativeComponent, ViewStyle } from 'react-native';

type Xs2aReactNativeProps = {
  wizardSessionKey: string;
  onSuccess: Function;
  onAbort: Function;
  onNetworkError: Function;
  style: ViewStyle;
};

export const Xs2aReactNativeViewManager =
  requireNativeComponent<Xs2aReactNativeProps>('Xs2aReactNativeView');

export default Xs2aReactNativeViewManager;
