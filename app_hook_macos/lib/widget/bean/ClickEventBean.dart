class ClickEventBean {
  late String action;
  Data? data;
  late int time;

  ClickEventBean({required this.action, this.data, required this.time});

  ClickEventBean.fromJson(Map<String, dynamic> json) {
    action = json['action'];
    data = json['data'] != null ? Data.fromJson(json['data']) : null;
    time = json['time'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = <String, dynamic>{};
    data['action'] = this.action;
    if (this.data != null) {
      data['data'] = this.data!.toJson();
    }
    data['time'] = this.time;
    return data;
  }
}

class Data {
  late String eventName;
  late String type;
  late int viewId;
  late String viewName;
  late String stack;

  Data(
      {required this.eventName,
      required this.type,
      required this.viewId,
      required this.viewName,
      required this.stack});

  Data.fromJson(Map<String, dynamic> json) {
    eventName = json['event_name'];
    type = json['type'];
    viewId = json['view_id'];
    viewName = json['view_name'];
    stack = json['stack'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['event_name'] = this.eventName;
    data['type'] = this.type;
    data['view_id'] = this.viewId;
    data['view_name'] = this.viewName;
    return data;
  }
}
