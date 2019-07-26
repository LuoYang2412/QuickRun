import 'package:json_annotation/json_annotation.dart';

part 'freight_bill.g.dart';

///运单
@JsonSerializable()
class FreightBill {
  String carsId;
  String createDate;
  String createUserId;
  String handover;
  String id;
  String manager;
  String outputDate;
  String outputNum;
  String route;
  String routeId;
  int state;
  int warehouseId;

  FreightBill(
      this.carsId,
      this.createDate,
      this.createUserId,
      this.handover,
      this.id,
      this.manager,
      this.outputDate,
      this.outputNum,
      this.route,
      this.routeId,
      this.state,
      this.warehouseId);

  factory FreightBill.formJson(Map<String, dynamic> json) =>
      _$FreightBillFromJson(json);

  Map<String, dynamic> toJson() => _$FreightBillToJson(this);
}
