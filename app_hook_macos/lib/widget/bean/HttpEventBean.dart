class HttpEventBean {
  late String action;
  Data? data;
  late int time;

  HttpEventBean({required this.action, required this.data, required this.time});

  HttpEventBean.fromJson(Map<String, dynamic> json) {
    action = json['action'];
    data = json['data'] != null ? new Data.fromJson(json['data']) : null;
    time = json['time'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['action'] = this.action;
    if (this.data != null) {
      data['data'] = this.data?.toJson();
    }
    data['time'] = this.time;
    return data;
  }
}

class Data {
  late String host;
  late String method;
  late String path;
  late String protocol;
  late bool requestBodyIsPlainText;
  late String requestDate;
  late String requestHeaders;
  late bool responseBodyIsPlainText;
  late int responseCode;
  late int responseContentLength;
  late String responseContentType;
  late String responseDate;
  late String responseHeaders;
  late String responseMessage;
  late String scheme;
  late int tookMs;
  late String url;

  late String requestBody;
  late String responseBody;

  Data({required this.host,
    required this.method,
    required this.path,
    required this.protocol,
    required this.requestBodyIsPlainText,
    required this.requestDate,
    required this.requestHeaders,
    required this.responseBodyIsPlainText,
    required this.responseCode,
    required this.responseContentLength,
    required this.responseContentType,
    required this.responseDate,
    required this.responseHeaders,
    required this.responseMessage,
    required this.scheme,
    required this.tookMs,
    required this.url}) {
    // TODO: implement
    throw UnimplementedError();
  }

  Data.fromJson(Map<String, dynamic> json) {
    host = json['host'];
    method = json['method'];
    path = json['path'];
    protocol = json['protocol'];
    requestBodyIsPlainText = json['request_body_is_plain_text'];
    requestDate = json['request_date'];
    requestHeaders = json['request_headers'];
    requestBody = json['request_body'];
    responseBodyIsPlainText = json['response_body_is_plain_text'];
    responseCode = json['response_code'];
    responseContentLength = json['response_content_length'];
    responseContentType = json['response_content_type'];
    responseDate = json['response_date'];
    responseHeaders = json['response_headers'];
    responseMessage = json['response_message'];
    responseBody = json['response_body'];
    scheme = json['scheme'];
    tookMs = json['took_ms'];
    url = json['url'];
  }

  Map<String, dynamic> toJson() {
    final Map<String, dynamic> data = new Map<String, dynamic>();
    data['host'] = this.host;
    data['method'] = this.method;
    data['path'] = this.path;
    data['protocol'] = this.protocol;
    data['request_body_is_plain_text'] = this.requestBodyIsPlainText;
    data['request_date'] = this.requestDate;
    data['request_headers'] = this.requestHeaders;
    data['response_body_is_plain_text'] = this.responseBodyIsPlainText;
    data['response_code'] = this.responseCode;
    data['response_content_length'] = this.responseContentLength;
    data['response_content_type'] = this.responseContentType;
    data['response_date'] = this.responseDate;
    data['response_headers'] = this.responseHeaders;
    data['response_message'] = this.responseMessage;
    data['scheme'] = this.scheme;
    data['took_ms'] = this.tookMs;
    data['url'] = this.url;
    return data;
  }
}
