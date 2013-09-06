package capptain.video.push.demo.frontend

class DataPush
{

  String kind
  String name
  String category
  String audience
  String pushMode
  String deliveryTime
  String filepush

  static belongsTo = [videoPush:VideoPush]

  /* Disable persistency */
  static mapWith = "none"
}
