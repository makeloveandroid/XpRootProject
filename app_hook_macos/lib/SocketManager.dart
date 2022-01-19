import 'dart:convert';

import 'package:bot_toast/bot_toast.dart';
import 'package:web_socket_channel/io.dart';

typedef void MsgCallBack(String action, dynamic data);

class SocketManager {
  final Map<String, List<MsgCallBack>> _listens = {};

  IOWebSocketChannel? _channel;

  SocketManager._privateConstructor();

  static final SocketManager _instance = SocketManager._privateConstructor();

  factory SocketManager(){
    return _instance;
  }


  void open() async {
//    adb forward tcp:8000 localabstract:app_hook
//    var commands = ["forward", "tcp:8000", "localabstract:app_hook"];
//    print('Running command: adb ${commands.join(" ")}');
//    Process process = await Process.start('adb', commands, runInShell: false);
//    int code = await process.exitCode;
//    print("执行完成:$code");
//    var reslut = await run('dart', ['--version'], verbose: true);
//    print("执行完成:${reslut.stdout}");

    _channel = IOWebSocketChannel.connect("ws://127.0.0.1:8000/apphook");

    _channel?.stream.listen(_onData, onError: _onError, onDone: _onDone);
  }

  void _onData(message) {
    print("收到数据$message");
    var jsonData = json.decode(message);
    String action = jsonData["action"];
    _listens[action]?.forEach((callback) {
      callback(action, jsonData);
    });
  }

  void addMessageListen(String action, MsgCallBack msgCallBack) {
    List<MsgCallBack>? callback = _listens[action];
    if (callback == null) {
      callback = <MsgCallBack>[];
      _listens[action] = callback;
    }
    callback.add(msgCallBack);
  }

  void sendMessage(String msg) {
    if (_channel != null) {
      _channel?.sink.add(msg);
    }
  }

  _onError(err) {
    print("连接失败$err");
  }

  void _onDone() {
    BotToast.showText(text: "未连接设备 2 秒后重试!");
    Future.delayed(Duration(seconds: 2), open);
  }
}
