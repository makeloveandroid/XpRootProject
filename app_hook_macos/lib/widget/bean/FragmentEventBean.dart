class FragmentEventBean {
  late String action;
  Data? data;
  late int time;

  FragmentEventBean({required this.action, this.data, required this.time});

  FragmentEventBean.fromJson(Map<String, dynamic> json) {
    action = json['action'];
    data = json['data'] != null ? Data.fromJson(json['data']) : null;
    time = json['time'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['action'] = this.action;
    if (this.data != null) {
      data['data'] = this.data?.toJson();
    }
    data['time'] = this.time;
    return data;
  }
}

class Data {
  late String activityName;
  late String className;
  late String type;

  Data({required this.activityName, required this.className, required this.type});

  Data.fromJson(Map<String, dynamic> json) {
    activityName = json['activity_name'];
    className = json['class_name'];
    type = json['type'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['activity_name'] = this.activityName;
    data['class_name'] = this.className;
    data['type'] = this.type;
    return data;
  }
}
