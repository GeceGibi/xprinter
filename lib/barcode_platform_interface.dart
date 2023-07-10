import 'package:plugin_platform_interface/plugin_platform_interface.dart';

import 'barcode_method_channel.dart';

abstract class BarcodePlatform extends PlatformInterface {
  /// Constructs a BarcodePlatform.
  BarcodePlatform() : super(token: _token);

  static final Object _token = Object();

  static BarcodePlatform _instance = MethodChannelBarcode();

  /// The default instance of [BarcodePlatform] to use.
  ///
  /// Defaults to [MethodChannelBarcode].
  static BarcodePlatform get instance => _instance;

  /// Platform-specific implementations should set this with their own
  /// platform-specific class that extends [BarcodePlatform] when
  /// they register themselves.
  static set instance(BarcodePlatform instance) {
    PlatformInterface.verifyToken(instance, _token);
    _instance = instance;
  }

  Future<String?> getPlatformVersion() {
    throw UnimplementedError('platformVersion() has not been implemented.');
  }

  Future<String?> print() {
    throw UnimplementedError('print() has not been implemented.');
  }
}
