import 'barcode_platform_interface.dart';

class Barcode {
  Future<String?> getPlatformVersion() {
    return BarcodePlatform.instance.getPlatformVersion();
  }

  Future<String?> print() {
    return BarcodePlatform.instance.print();
  }
}
