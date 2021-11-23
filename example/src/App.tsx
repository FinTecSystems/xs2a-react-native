/* eslint-disable react-native/no-inline-styles */
import * as React from 'react';

import { StyleSheet, View } from 'react-native';
import Xs2aReactNativeViewManager from 'xs2a-react-native';

export default function App() {
  return (
    <View style={styles.container}>
      <Xs2aReactNativeViewManager
        wizardSessionKey="hx0rHrXuASA1y2C8qH9XvrJMaMA5UVBtPxiEPU4E"
        style={{ flex: 1, width: '100%', height: '100%' }}
        onSuccess={({ nativeEvent: { credentials } }) => {
          console.log('succccc');
        }}
        onAbort={() => {
          console.log('abort');
        }}
        onNetworkError={() => {
          console.log('network error');
        }}
        styleProvider={{
          font: 'Chalkduster',
          tintColor: '#ffffff',
          logoVariation: 'STANDARD',
          backgroundColor: '#ffffff',
          textColor: '#000000',
          inputBackgroundColor: '#059392',
          inputBorderRadius: 4,
          inputTextColor: '#000000',
          placeholderColor: '#000000',
          buttonBorderRadius: 4,
          submitButtonStyle: {
            textColor: '#000000',
            backgroundColor: '#000000',
          },
          backButtonStyle: {
            textColor: '#000000',
            backgroundColor: '#000000',
          },
          abortButtonStyle: {
            textColor: '#000000',
            backgroundColor: '#000000',
          },
          restartButtonStyle: {
            textColor: '#000000',
            backgroundColor: '#000000',
          },
          alertBorderRadius: 4,
        //   errorStyle: {
        //     textColor: '#000000',
        //     backgroundColor: '#000000',
        //   },
          warningStyle: {
            textColor: '#000000',
            backgroundColor: '#000000',
          },
          infoStyle: {
            textColor: '#000000',
            backgroundColor: '#000000',
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
  },
  box: {
    width: 60,
    height: 60,
    marginVertical: 20,
  },
});
