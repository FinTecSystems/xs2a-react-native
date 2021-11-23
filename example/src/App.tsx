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
        style={{ flex: 1, width: '100%', height: '100%', backgroundColor: '#aaa' }}
        onSuccess={() => {
          console.log('succccc');
        }}
        onAbort={() => {
          console.log('abort');
        }}
        onNetworkError={() => {
          console.log('network error');
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
