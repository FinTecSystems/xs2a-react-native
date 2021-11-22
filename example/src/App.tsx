import * as React from 'react';

import { StyleSheet, View } from 'react-native';
import Xs2aReactNativeViewManager from 'xs2a-react-native';

export default function App() {
  return (
    <View style={styles.container}>
      <Xs2aReactNativeViewManager
        wizardSessionKey="vpwScc7INIOu9PpMjwl3LtAo54XwTOfKG1LetzZq"
        style={{ flex: 1, width: '100%', height: '100%' }}
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
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
