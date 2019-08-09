import 'dart:convert';

import 'package:dio/dio.dart';
import 'package:quick_run_flutter/common/config/config.dart';
import 'package:quick_run_flutter/common/local/local_storage.dart';
import 'package:quick_run_flutter/common/net/address.dart';
import 'package:quick_run_flutter/common/net/api.dart';

import 'dao_result.dart';

class UserDao {
  static login(userName, password, store) async {
    String type = userName + ":" + password;
    var bytes = utf8.encode(type);
    var base64Str = base64.encode(bytes);
    if (Config.DEBUG) {
      print("base64Str login " + base64Str);
    }
    await LocalStorage.save(Config.USER_NAME_KEY, userName);
    await LocalStorage.save(Config.USER_BASIC_CODE, base64Str);

    Map<String, dynamic> requestParams = {
      "account": userName,
      "password": password,
      "uuid": 0,
    };
    httpManager.clearAuthorization();

    var res = await httpManager.netFetch(Address.login(),
        FormData.from(requestParams), null, new Options(method: "post"));
//    var resultData;
    if (res != null && res.result) {
      await LocalStorage.save(Config.PW_KEY, password);
//      var resultData = await getUserInfo(null);
      if (Config.DEBUG) {
        print("user result " + res.result.toString());
        print(res.data);
        print(res.data.toString());
      }
//      store.dispatch(new UpdateUserAction(resultData.data));
    }
    return new DataResult(res, res.result);
  }

  ///获取用户详细信息
//  static getUserInfo(userName, {needDb = false}) async {
//    UserInfoDbProvider provider = new UserInfoDbProvider();
//    next() async {
//      var res;
//      if (userName == null) {
//        res = await httpManager.netFetch(Address.getMyUserInfo(), null, null, null);
//      } else {
//        res = await httpManager.netFetch(Address.getUserInfo(userName), null, null, null);
//      }
//      if (res != null && res.result) {
//        String starred = "---";
//        if (res.data["type"] != "Organization") {
//          var countRes = await getUserStaredCountNet(res.data["login"]);
//          if (countRes.result) {
//            starred = countRes.data;
//          }
//        }
//        User user = User.fromJson(res.data);
//        user.starred = starred;
//        if (userName == null) {
//          LocalStorage.save(Config.USER_INFO, json.encode(user.toJson()));
//        } else {
//          if (needDb) {
//            provider.insert(userName, json.encode(user.toJson()));
//          }
//        }
//        return new DataResult(user, true);
//      } else {
//        return new DataResult(res.data, false);
//      }
//    }
//
//    if (needDb) {
//      User user = await provider.getUserInfo(userName);
//      if (user == null) {
//        return await next();
//      }
//      DataResult dataResult = new DataResult(user, true, next: next());
//      return dataResult;
//    }
//    return await next();
//  }
}
