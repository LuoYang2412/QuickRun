import 'package:flutter/material.dart';
import 'package:flutter_spinkit/flutter_spinkit.dart';
import 'package:quick_run_flutter/common/style/quick_run_style.dart';

import 'navigator_utils.dart';

class CommonUtils {
  static Future<Null> showLoadingDialog(BuildContext context) {
    return NavigatorUtils.showGSYDialog(
        context: context,
        builder: (BuildContext context) {
          return new Material(
              color: Colors.transparent,
              child: WillPopScope(
                onWillPop: () => new Future.value(false),
                child: Center(
                  child: new Container(
                    width: 200.0,
                    height: 200.0,
                    padding: new EdgeInsets.all(4.0),
                    decoration: new BoxDecoration(
                      color: Colors.transparent,
                      //用一个BoxDecoration装饰器提供背景图片
                      borderRadius: BorderRadius.all(Radius.circular(4.0)),
                    ),
                    child: new Column(
                      mainAxisAlignment: MainAxisAlignment.center,
                      children: <Widget>[
                        new Container(
                            child: SpinKitCubeGrid(color: Colors.white)),
                        new Container(height: 10.0),
                        new Container(
                          child: new Text(
                            "努力加载中···",
                            style: QuickRunStyle.normalTextWhite,
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ));
        });
  }
}
