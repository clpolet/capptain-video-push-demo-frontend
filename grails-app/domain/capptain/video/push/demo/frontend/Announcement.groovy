package capptain.video.push.demo.frontend

class Announcement
{

  String kind
  String name
  String category
  String audience
  String pushMode
  String type
  String deliveryTime
  String notificationType
  String notificationMessage
  String actionButtonText
  String exitButtonText
  String actionUrl

  static belongsTo = [videoPush:VideoPush]

  /* Disable persistency */
  static mapWith = "none"

  static constraints =
  {
  }
}
