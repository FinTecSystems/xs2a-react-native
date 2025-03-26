import { View, StyleSheet } from 'react-native';
import { Xs2aReactNativeViewManager } from '@fintecsystems/xs2a-react-native';

export default function App() {
  /*
  const clearAllStoredCredentials = () => {
    // In Case you are using the Credential Storing Feature
    // you might want to make use of the method:
    // clearCredentials();
  };
  */

  return (
    <View style={styles.container}>
      <Xs2aReactNativeViewManager
        wizardSessionKey="YOUR_WIZARD_SESSION_KEY"
        style={{ width: '100%', height: '100%' }}
        onSuccess={({ nativeEvent: { credentials } }) => {
          console.log('Success', credentials);
        }}
        onAbort={() => {
          console.log('Abort');
        }}
        onNetworkError={() => {
          console.log('Network error');
        }}
        onBackButtonTapped={({ nativeEvent: { currentStep } }) => {
          console.log('Pressed back on step:', currentStep);
          // Called when the back button was tapped. Optional to implement.
          // currentStep can be any of "login", "tan", "account", "bank" or empty string ("")
        }}
        onSessionError={({ nativeEvent: { errorCode, recoverable } }) => {
          console.log(errorCode, recoverable);
        }}
        onStep={({ nativeEvent: { newStep } }) => {
          console.log('new step: ', newStep);
        }}
        styleProvider={
          {
            //   tintColor: '#ff0000',
            //   logoVariation: 'STANDARD',
            //   backgroundColor: '#ffffff',
            //   textColor: '#000000',
            //   inputBackgroundColor: '#059392',
            //   inputBorderRadius: 15,
            //   inputTextColor: '#000000',
            //   placeholderColor: '#dddddd',
            //   buttonBorderRadius: 9,
            //   submitButtonStyle: {
            //     textColor: '#000000',
            //     backgroundColor: '#dddddd',
            //   },
            //   backButtonStyle: {
            //     textColor: '#000000',
            //     backgroundColor: '#dddddd',
            //   },
            //   abortButtonStyle: {
            //     textColor: '#000000',
            //     backgroundColor: '#dddddd',
            //   },
            //   restartButtonStyle: {
            //     textColor: '#000000',
            //     backgroundColor: '#dddddd',
            //   },
            //   alertBorderRadius: 12,
            //   errorStyle: {
            //     textColor: '#000000',
            //     backgroundColor: '#dddddd',
            //   },
            //   warningStyle: {
            //     textColor: '#000000',
            //     backgroundColor: '#dddddd',
            //   },
            //   infoStyle: {
            //     textColor: '#000000',
            //     backgroundColor: '#dddddd',
            //   },
          }
        }
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
