import 'package:flutter/material.dart';
import 'package:flutter/services.dart';
import 'package:flutter/widgets.dart';
import 'package:quick_run_flutter/common/dao/user_dao.dart';
import 'package:quick_run_flutter/common/utils/common_utils.dart';
import 'package:quick_run_flutter/common/utils/navigator_utils.dart';

class LoginPage extends StatefulWidget {
  static final pName = "login";

  @override
  State<StatefulWidget> createState() {
    return _LoginPateState();
  }
}

class _LoginPateState extends State<LoginPage> {
  var _userName = "";
  var _password = "";

  final userController = TextEditingController();
  final pswController = TextEditingController();

  @override
  void initState() {
    super.initState();
    initParams();
  }

  initParams() async {
    userController.value = TextEditingValue(text: _userName ?? "");
    pswController.value = TextEditingValue(text: _password ?? "");
  }

  @override
  Widget build(BuildContext context) {
    return GestureDetector(
      behavior: HitTestBehavior.translucent,
      onTap: () {
        FocusScope.of(context).requestFocus(FocusNode());
      },
      child: Scaffold(
        appBar: AppBar(
          title: Text("登录",),
        ),
        body: Center(
            child: SafeArea(
                child: SingleChildScrollView(
          child: Card(
            elevation: 5.0,
            shape: RoundedRectangleBorder(
                borderRadius: BorderRadius.all(Radius.circular(10.0))),
            color: Colors.white,
            margin: EdgeInsets.all(30),
            child: Padding(
              padding:
                  EdgeInsets.only(left: 30, right: 30, top: 30, bottom: 30),
              child: Column(
                mainAxisAlignment: MainAxisAlignment.center,
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisSize: MainAxisSize.min,
                children: <Widget>[
                  Center(
                      child: Icon(
                    Icons.android,
                    size: 50,
                    color: Colors.green,
                  )),
                  Text("用户名:"),
                  TextField(
                    controller: userController,
                    decoration: InputDecoration(
                        hintText: "请输入用户名或账号",
                        icon: Icon(Icons.supervisor_account)),
                    onChanged: (String value) {
                      _userName = value;
                    },
                  ),
                  Padding(padding: EdgeInsets.only(top: 20)),
                  Text("密码:"),
                  TextField(
                    controller: pswController,
                    obscureText: true,
                    decoration: InputDecoration(
                        hintText: "请输入密码",
                        icon: Icon(Icons.enhanced_encryption)),
                    onChanged: (String value) {
                      _password = value;
                    },
                  ),
                  Padding(padding: EdgeInsets.only(top: 20)),
                  Container(
                    margin: EdgeInsets.only(left: 20, right: 20),
                    child: RaisedButton(
                      color: Colors.blue,
                      shape: RoundedRectangleBorder(
                          borderRadius: BorderRadius.horizontal(
                              left: Radius.circular(20),
                              right: Radius.circular(20))),
                      child: Flex(
                          mainAxisAlignment: MainAxisAlignment.center,
                          direction: Axis.horizontal,
                          children: <Widget>[
                            Text(
                              "登录",
                              style: TextStyle(color: Colors.white),
                              textAlign: TextAlign.center,
                            )
                          ]),
                      onPressed: () {
                        if (_userName == null || _userName.length == 0) {
                          return;
                        }
                        if (_password == null || _password.length == 0) {
                          return;
                        }
                        CommonUtils.showLoadingDialog(context);
                        UserDao.login(_userName.trim(), _password.trim(), null)
                            .then((res) {
                          Navigator.pop(context);
                          if (res != null && res.result) {
                            new Future.delayed(const Duration(seconds: 1), () {
                              NavigatorUtils.goHome(context);
                              return true;
                            });
                          }
                        });
                      },
                    ),
                  )
                ],
              ),
            ),
          ),
        ))),
      ),
    );
  }
}
