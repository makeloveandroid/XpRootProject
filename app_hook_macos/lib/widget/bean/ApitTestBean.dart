class TestApiBean {
  String? action;
  TestApiData? apiData;

  TestApiBean({this.action, this.apiData});

  TestApiBean.fromJson(Map<String, dynamic> json) {
    action = json['action'];
    apiData = json['data'] != null ? TestApiData.fromJson(json['data']) : null;
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['action'] = action;
    if (apiData != null) {
      data['data'] = apiData!.toJson();
    }
    return data;
  }
}

class TestApiData {
  late bool isGetConfig;
  late String host;
  late bool isHttps;
  late String action;

  TestApiData({required this.isGetConfig, required this.host, required this.isHttps, required this.action});

  TestApiData.fromJson(Map<String, dynamic> json) {
    isGetConfig = json['isGetConfig'];
    host = json['host'];
    isHttps = json['isHttps'];
    action = json['action'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['isGetConfig'] = isGetConfig;
    data['host'] = host;
    data['isHttps'] = isHttps;
    data['action'] = action;
    return data;
  }
}
