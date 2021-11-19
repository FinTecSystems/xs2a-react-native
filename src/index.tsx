import { requireNativeComponent, ViewStyle } from 'react-native';

type Xs2aReactNativeProps = {
  color: string;
  style: ViewStyle;
};

export const Xs2aReactNativeViewManager = requireNativeComponent<Xs2aReactNativeProps>(
'Xs2aReactNativeView'
);

export default Xs2aReactNativeViewManager;
