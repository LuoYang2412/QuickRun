import 'package:flutter/material.dart';
import 'package:flutter/widgets.dart';

import 'login.dart';

class WelcomePage extends StatefulWidget {
  static final pName = "/";

  @override
  State<StatefulWidget> createState() => _WelcomePageState();
}

class _WelcomePageState extends State<WelcomePage> {
  @override
  void didChangeDependencies() {
    super.didChangeDependencies();
    Future.delayed(Duration(seconds: 2), () {
      Navigator.pushReplacementNamed(context, LoginPage.pName);
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Container(
        width: MediaQuery.of(context).size.width,
        color: Colors.white,
        padding: EdgeInsets.all(8),
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Icon(
              Icons.android,
              size: 100,
              color: Colors.green,
            ),
            Padding(
              padding: EdgeInsets.only(top: 20),
            ),
            Text(
              "Welcome Flutter",
              textAlign: TextAlign.center,
              style: TextStyle(color: Colors.blue),
            )
          ],
        ),
      ),
    );
  }
}
