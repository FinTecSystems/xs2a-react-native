/* eslint-disable react-native/no-inline-styles */
import * as React from 'react';

import { StyleSheet, UIManager, findNodeHandle, View } from 'react-native';
import Xs2aReactNativeViewManager from 'xs2a-react-native';

export default function App() {
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
    <View style={styles.container}>
      <Xs2aReactNativeViewManager
        ref={ref}
        wizardSessionKey="69nIDdeEnSXxleR7BRIqHzcHtE9iwAYLZ7xPlUdn"
        style={{ flex: 1, width: '100%', height: '100%' }}
        onSuccess={({ nativeEvent: { credentials } }) => {
          console.log('succccc', credentials);
        }}
        onAbort={() => {
          console.log('abort');
        }}
        onNetworkError={() => {
          console.log('network error');
        }}
        styleProvider={{
          font: 'Comic Sans',
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
    </View>
  );
}

const styles = StyleSheet.create({
  container: {
    flex: 1,
    alignItems: 'center',
    justifyContent: 'center',
    backgroundColor: '#00aaaa',
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
