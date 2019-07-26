// GENERATED CODE - DO NOT MODIFY BY HAND

part of 'freight_bill.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

FreightBill _$FreightBillFromJson(Map<String, dynamic> json) {
  return FreightBill(
    json['carsId'] as String,
    json['createDate'] as String,
    json['createUserId'] as String,
    json['handover'] as String,
    json['id'] as String,
    json['manager'] as String,
    json['outputDate'] as String,
    json['outputNum'] as String,
    json['route'] as String,
    json['routeId'] as String,
    json['state'] as int,
    json['warehouseId'] as int,
  );
}

Map<String, dynamic> _$FreightBillToJson(FreightBill instance) =>
    <String, dynamic>{
      'carsId': instance.carsId,
      'createDate': instance.createDate,
      'createUserId': instance.createUserId,
      'handover': instance.handover,
      'id': instance.id,
      'manager': instance.manager,
      'outputDate': instance.outputDate,
      'outputNum': instance.outputNum,
      'route': instance.route,
      'routeId': instance.routeId,
      'state': instance.state,
      'warehouseId': instance.warehouseId,
    };
