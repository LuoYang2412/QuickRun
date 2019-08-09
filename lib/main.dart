import 'dart:async';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:fluttertoast/fluttertoast.dart';
import 'package:quick_run_flutter/page/home_page.dart';

import 'common/event/http_error_event.dart';
import 'common/event/index.dart';
import 'common/net/code.dart';
import 'common/style/StringConstant.dart';
import 'page/detail_page.dart';
import 'page/login_page.dart';
import 'page/welcome_page.dart';

void main() {
  runZoned(() {
    SystemChrome.setPreferredOrientations(
      [DeviceOrientation.portraitUp],
    ).then((_) {
      runApp(QuickRunApp());
      PaintingBinding.instance.imageCache.maximumSize = 100;
    });
  }, onError: (Object obj, StackTrace stack) {
    print(obj);
    print(stack);
  });
}

class QuickRunApp extends StatefulWidget {
  @override
  State<StatefulWidget> createState() => QuickRunAppState();
}
class QuickRunAppState extends State<QuickRunApp>{

  StreamSubscription stream;
  @override
  void initState() {
    super.initState();
    ///Stream演示event bus
    stream = eventBus.on<HttpErrorEvent>().listen((event) {
      errorHandleFunction(event.code, event.message);
    });
  }
  ///网络错误提醒
  errorHandleFunction(int code, message) {
    switch (code) {
      case Code.NETWORK_ERROR:
        Fluttertoast.showToast(
            msg: StringConstant.network_error);
        break;
      case 401:
        Fluttertoast.showToast(
            msg: StringConstant.network_error_401);
        break;
      case 403:
        Fluttertoast.showToast(
            msg: StringConstant.network_error_403);
        break;
      case 404:
        Fluttertoast.showToast(
            msg: StringConstant.network_error_404);
        break;
      case Code.NETWORK_TIMEOUT:
      //超时
        Fluttertoast.showToast(
            msg:StringConstant.network_error_timeout);
        break;
      default:
        Fluttertoast.showToast(
            msg: StringConstant.network_error_unknown +
                " " +
                message);
        break;
    }
  }
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      title: 'Flutter Demo',
      theme: ThemeData(
        primarySwatch: Colors.blue,
      ),
      routes: {
        WelcomePage.pName: (context) => WelcomePage(),
        LoginPage.pName: (context) => LoginPage(),
        HomePage.pName: (context) => HomePage(),
        DetailPage.pName: (context) => DetailPage(),
      },
    );
  }
}