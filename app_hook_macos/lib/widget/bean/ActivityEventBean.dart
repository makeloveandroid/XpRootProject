class ActivityEventBean {
  late String action;
  Data? data;
  late int time;

  ActivityEventBean({required this.action, this.data, required this.time});

  ActivityEventBean.fromJson(Map<String, dynamic> json) {
    action = json['action']!;
    data = json['data'] != null ? Data.fromJson(json['data']) : null;
    time = json['time']!;
  }
}

class Data {
  String? activityName;
  Map<String, IntentData>? intentData;

  Data({this.activityName, this.intentData});

  Data.fromJson(Map<String, dynamic> json) {
    activityName = json['activity_name'];
    if (json['intent_data'] != null) {
      Map<String, dynamic> orIntentData = json['intent_data'];
      intentData = <String, IntentData>{};
      orIntentData.forEach((key, value) {
        intentData![key] = IntentData.fromJson(value);
      });
    }
  }
}

class IntentData {
  late String type;
  late String value;

  IntentData({required this.type, required this.value});

  IntentData.fromJson(Map<String, dynamic> json) {
    type = json['type'];
    value = json['value'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['type'] = type;
    data['value'] = value;
    return data;
  }
}
