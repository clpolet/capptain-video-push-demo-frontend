package capptain.video.push.demo.frontend

class Device
{

  String name
  String deviceId

  static belongsTo = [videoPush:VideoPush]

  /* Override Grails persistency by user Cookie */
  static mapWith = "none"

  static constraints =
  {
  }
}
