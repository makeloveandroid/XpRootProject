class CutActivityBean {
  late String action;
  Data? data;
  late int time;

  CutActivityBean({required this.action, this.data, required this.time});

  CutActivityBean.fromJson(Map<String, dynamic> json) {
    action = json['action'];
    data = json['data'] != null ? new Data.fromJson(json['data']) : null;
    time = json['time'];
  }
}

class Data {
  late String activityName;
  late String base64Byte;

  Data({required this.activityName, required this.base64Byte});

  Data.fromJson(Map<String, dynamic> json) {
    activityName = json['activity_name'];
    base64Byte = json['base64_byte'];
  }
}
