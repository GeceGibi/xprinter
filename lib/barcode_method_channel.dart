import 'package:flutter/foundation.dart';
import 'package:flutter/services.dart';

import 'barcode_platform_interface.dart';

/// An implementation of [BarcodePlatform] that uses method channels.
class MethodChannelBarcode extends BarcodePlatform {
  /// The method channel used to interact with the native platform.
  @visibleForTesting
  final methodChannel = const MethodChannel('barcode');

  @override
  Future<String?> getPlatformVersion() async {
    return methodChannel.invokeMethod<String>('getPlatformVersion');
  }

  @override
  Future<String?> print() async {
    return methodChannel.invokeMethod<String>('print');
  }
}
