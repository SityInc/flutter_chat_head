#import "FlutterChatHeadPlugin.h"
#if __has_include(<flutter_chat_head/flutter_chat_head-Swift.h>)
#import <flutter_chat_head/flutter_chat_head-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_chat_head-Swift.h"
#endif

@implementation FlutterChatHeadPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterChatHeadPlugin registerWithRegistrar:registrar];
}
@end
