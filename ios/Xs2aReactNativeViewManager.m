#import "React/RCTViewManager.h"
#import <React/RCTUIManager.h>
#import <React/RCTLog.h>

@interface RCT_EXTERN_MODULE(Xs2aReactNativeViewManager, RCTViewManager)

RCT_EXPORT_VIEW_PROPERTY(wizardSessionKey, NSString)
RCT_EXPORT_VIEW_PROPERTY(styleProvider, NSDictionary *)
RCT_EXPORT_VIEW_PROPERTY(onSuccess, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onAbort, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onNetworkError, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onSessionError, RCTDirectEventBlock)
RCT_EXPORT_VIEW_PROPERTY(onBackButtonTapped, RCTDirectEventBlock)

@end

