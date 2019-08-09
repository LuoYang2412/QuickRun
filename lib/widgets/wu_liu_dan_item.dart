import 'package:flutter/material.dart';
import 'package:quick_run_flutter/model/freight_bill_model.dart';

class WuLiuDanItem extends StatelessWidget {
  FreightBill _freightBill;

  WuLiuDanItem(this._freightBill);

  @override
  Widget build(BuildContext context) {
    return Container(
      margin: EdgeInsets.only(
        left: 8,
        right: 8,
      ),
      child: Card(
        shape: RoundedRectangleBorder(
            borderRadius: BorderRadius.horizontal(
          left: Radius.circular(8),
          right: Radius.circular(8),
        )),
        child: Padding(
          padding: EdgeInsets.all(8),
          child: Column(
            mainAxisAlignment: MainAxisAlignment.spaceBetween,
            children: <Widget>[
              Row(
                children: <Widget>[
                  Text("出库单号:"),
                  Container(
                    margin: EdgeInsets.only(left: 4),
                    child: Text(_freightBill.outputNum),
                  ),
                  Expanded(
                    child: Flex(
                      direction: Axis.horizontal,
                      mainAxisAlignment: MainAxisAlignment.end,
                      children: <Widget>[
                        Text(_freightBill.state == 0 ? "已完成" : "未完成"),
                      ],
                    ),
                  ),
                ],
              ),
              Padding(
                padding: EdgeInsets.only(
                  top: 4,
                ),
              ),
              Row(
                children: <Widget>[
                  Image.asset(
                    "assets/images/ic_route.png",
                    width: 50,
                  ),
                  Padding(
                    padding: EdgeInsets.only(
                      left: 4,
                    ),
                  ),
                  Expanded(
                    child: Center(
                      child: Text(
                        _freightBill.route,
                        maxLines: 1,
                        overflow: TextOverflow.ellipsis,
                        style: TextStyle(
                          fontSize: 20,
                        ),
                      ),
                    ),
                  ),
                ],
              ),
              Padding(
                padding: EdgeInsets.only(
                  top: 4,
                ),
              ),
              Row(
                mainAxisAlignment: MainAxisAlignment.end,
                children: <Widget>[
                  Text("创建时间:"),
                  Container(
                    margin: EdgeInsets.only(left: 4),
                    child: Text(_freightBill.createDate),
                  ),
                ],
              ),
            ],
          ),
        ),
      ),
    );
  }
}
