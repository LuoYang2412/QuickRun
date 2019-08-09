import 'package:flutter/material.dart';
import 'package:quick_run_flutter/model/order_model.dart';
import 'package:quick_run_flutter/model/station_model.dart';
import 'package:quick_run_flutter/widgets/order_item.dart';
import 'package:quick_run_flutter/widgets/station_item.dart';

class DetailPage extends StatefulWidget {
  static final pName = "detail";

  @override
  State<StatefulWidget> createState() => _DetailPageState();
}

class _DetailPageState extends State<DetailPage> {
  var stationList = List<Station>();
  var orderList = List<Order>();

  _getStationDate() async {
    stationList = await Future.delayed(Duration(seconds: 1), () {
      return List<Station>.generate(10, (index) {
        if (index == 0) {
          return Station(
            "-1",
            "出库",
            StationStatus.ED,
          );
        } else if (index == 1) {
          return Station(
            "0",
            "发货",
            StationStatus.ED,
          );
        } else {
          return Station(
            index.toString(),
            "站点-$index",
            _getStationStatus(index),
          );
        }
      });
    });
    setState(() {});
  }

  _getStationStatus(int index) {
    if (index < 5) {
      return StationStatus.ED;
    } else if (index == 5) {
      return StationStatus.ING;
    } else {
      return StationStatus.WILL;
    }
  }
  _getOrderData() async {
    orderList = await Future<List<Order>>.delayed(Duration(seconds: 1), () {
      return List<Order>.generate(10, (int index) {
        return Order(
            "adress$index",
            "freightOrderId$index",
            "id$index",
            "pickUpId$index",
            index,
            "shipmentNumber$index",
            "warehouseAddress$index",
            "warehouseCity$index",
            "warehouseCountry$index",
            "warehouseName$index",
            "warehouseProvince$index",
            "warehouseTown$index");
      });
    });
    setState(() {});
  }

  _getOrderItem(String stationId) {}

  @override
  void initState() {
    super.initState();
    _getStationDate();
    _getOrderData();
  }

  @override
  Widget build(BuildContext context) {
    String id = ModalRoute.of(context).settings.arguments;
    return Scaffold(
      appBar: AppBar(
        centerTitle: true,
        title: Text("路线详情"),
      ),
      body: Column(
        children: <Widget>[
          Container(
            height: 100,
            child: ListView.builder(
              itemExtent:
                  100 > MediaQuery.of(context).size.width / stationList.length
                      ? 100
                      : MediaQuery.of(context).size.width / stationList.length,
              scrollDirection: Axis.horizontal,
              itemCount: stationList.length,
              itemBuilder: (BuildContext context, int index) {
                return Center(
                  child: StationItem(stationList[index]),
                );
              },
            ),
          ),
          Container(
            height: 100,
            child: Card(
              color: Colors.lightBlue,
              shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.all(
                  Radius.circular(8),
                ),
              ),
              child: Container(
                margin: EdgeInsets.only(left: 8),
                child: Column(
                  mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                  children: <Widget>[
                    Row(
                      children: <Widget>[
                        Text(
                          "联系方式: ",
                          style: TextStyle(
                            color: Colors.white,
                          ),
                        ),
                        Text(
                          "14444444444",
                          style: TextStyle(
                            color: Colors.white,
                          ),
                        ),
                      ],
                    ),
                    Row(
                      children: <Widget>[
                        Text(
                          "地址: ",
                          style: TextStyle(
                            color: Colors.white,
                          ),
                        ),
                        Text(
                          "天堂33号",
                          style: TextStyle(
                            color: Colors.white,
                          ),
                        ),
                      ],
                    ),
                  ],
                ),
              ),
            ),
          ),
          Expanded(
            child: ListView.separated(
              itemCount: orderList.length,
              itemBuilder: (BuildContext context, int index) {
                return OrederItemWidget(
                  order: orderList[index],
                );
              },
              separatorBuilder: (BuildContext context, int index) {
                return Container(
                  height: 0.5,
                  color: Colors.black12,
                );
              },
            ),
          ),
        ],
      ),
    );
  }
}
