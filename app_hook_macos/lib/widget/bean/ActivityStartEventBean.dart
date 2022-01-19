class ActivityStartEventBean {
  late String action;
  Data? data;
  late int time;

  ActivityStartEventBean({required this.action, this.data, required this.time});

  ActivityStartEventBean.fromJson(Map<String, dynamic> json) {
    action = json['action'];
    data = json['data'] != null ? Data.fromJson(json['data']) : null;
    time = json['time'];
  }
}

class Data {
  late String activityName;
  late String startContext;
  late String stack;
  late Map<String, IntentData> intentData;

  Data({required this.activityName, required this.intentData});

  Data.fromJson(Map<String, dynamic> json) {
    activityName = json['activity_name'];
    startContext = json['start_context'];
    stack = json['stack'];
    if (json['intent_data'] != null) {
      Map<String, dynamic> orIntentData = json['intent_data'];
      intentData = <String, IntentData>{};
      orIntentData.forEach((key, value) {
        intentData[key] = IntentData.fromJson(value);
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
