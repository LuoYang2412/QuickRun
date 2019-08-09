import 'package:flutter/material.dart';
import 'package:quick_run_flutter/model/order_model.dart';

class OrederItemWidget extends StatelessWidget {
  const OrederItemWidget({
    Key key,
    @required this.order,
  }) : super(key: key);

  final Order order;

  @override
  Widget build(BuildContext context) {
    return Column(
      children: <Widget>[
        Container(
          margin: EdgeInsets.only(left: 4, top: 8, right: 4),
          child: Row(
            children: <Widget>[Text("物流单号: "), Text(order.shipmentNumber)],
          ),
        ),
        Container(
          margin: EdgeInsets.only(left: 4, top: 4, right: 4, bottom: 8),
          child: Row(
            children: <Widget>[Text("收获地址: "), Text(order.adress)],
          ),
        )
      ],
    );
  }
}
