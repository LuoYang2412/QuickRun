import 'package:banner_view/banner_view.dart';
import 'package:flutter/material.dart' hide RefreshIndicator;
import 'package:flutter/widgets.dart';
import 'package:quick_run_flutter/model/freight_bill_model.dart';
import 'package:quick_run_flutter/widgets/refresh_indicator.dart';
import 'package:quick_run_flutter/widgets/wu_liu_dan_item.dart';

import 'detail_page.dart';

class HomePage extends StatefulWidget {
  static final pName = "home";

  @override
  State<StatefulWidget> createState() => _HomePageState();
}

class _HomePageState extends State<HomePage>
    with SingleTickerProviderStateMixin {
  TabController _tabController;
  var _freightBillListUnDone = List<FreightBill>();
  var _freightBillListDone = List<FreightBill>();
  var tabs = ["未完成", "已完成"];

  @override
  void initState() {
    super.initState();
    _tabController = TabController(
      length: tabs.length,
      vsync: this,
    );
    _refresh(0);
    _refresh(1);
  }

  @override
  void dispose() {
    _tabController.dispose();
    super.dispose();
  }

  _refresh(int state) {
    return Future<void>.delayed(Duration(seconds: 1), () {
      var reList = List<FreightBill>();
      for (int i = 0; i < 10; i++) {
        reList.add(FreightBill(
            "carsId-$state $i",
            "createData-$state $i",
            "createUserId-$state $i",
            "handover-$state $i",
            "id-$state $i",
            "manager-$state $i",
            "outputDate-$state $i",
            "outputNum-$state $i",
            "route-$state $i",
            "routeId-$state $i",
            i % 2,
            (i + 1) * 3 ~/ 2));
      }
      setState(() {
        if (state == 0) {
          _freightBillListUnDone = reList;
        } else if (state == 1) {
          _freightBillListDone = reList;
        }
      });
    });
  }

  _itemClick(String id) {
    Navigator.pushNamed(context, DetailPage.pName, arguments: id);
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      appBar: AppBar(
        title: Text("首页"),
        centerTitle: true,
      ),
      body: Column(
        mainAxisAlignment: MainAxisAlignment.start,
        mainAxisSize: MainAxisSize.min,
        children: <Widget>[
          SizedBox(
            height: 140,
            child: BannerView(
              [
                Image.network(
                  "https://api.neweb.top/bing.php?type=future",
                  fit: BoxFit.fitWidth,
                ),
                Image.network(
                  "https://api.neweb.top/bing.php?type=rand",
                  fit: BoxFit.fitWidth,
                )
              ],
              intervalDuration: Duration(seconds: 5),
            ),
          ),
          TabBar(
              controller: _tabController,
              labelColor: Colors.black,
              tabs: tabs
                  .map((e) => Tab(
                        text: e,
                      ))
                  .toList()),
          Expanded(
            child: TabBarView(
              controller: _tabController,
              children: [
                RefreshIndicator(
                    child: ListView.builder(
                      itemExtent: 110,
                      itemCount: _freightBillListUnDone.length,
                      itemBuilder: (BuildContext context, int index) {
                        return GestureDetector(
                          child: WuLiuDanItem(_freightBillListUnDone[index]),
                          onTap: () =>
                              _itemClick(_freightBillListUnDone[index].id),
                        );
                      },
                    ),
                    onRefresh: () => _refresh(0)),
                RefreshIndicator(
                    child: ListView.builder(
                      itemExtent: 110,
                      itemCount: _freightBillListDone.length,
                      itemBuilder: (BuildContext context, int index) {
                        return GestureDetector(
                          child: WuLiuDanItem(_freightBillListDone[index]),
                          onTap: () =>
                              _itemClick(_freightBillListDone[index].id),
                        );
                      },
                    ),
                    onRefresh: () => _refresh(1)),
              ],
            ),
          )
        ],
      ),
    );
  }
}
