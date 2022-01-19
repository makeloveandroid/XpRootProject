import 'dart:convert';

import 'BaseAction.dart';

class StartActivityAction extends BaseAction {
 late String activityName;
 bool? isSchema;
 Map<String, String>? intentParameter;

  StartActivityAction(this.activityName, {this.isSchema,  this.intentParameter})
      : super("StartActivityAction");

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> parent = <String, dynamic>{};

    final Map<String, dynamic> data = Map<String, dynamic>();
    data['activityName'] = this.activityName;
    if (this.intentParameter != null) {
      data['intentParameter'] = json.encode(this.intentParameter);
    }
    data["isSchema"] = isSchema;
    parent["data"] = data;
    parent["action"] = action;
    return parent;
  }
}
