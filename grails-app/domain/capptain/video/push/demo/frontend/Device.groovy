package capptain.video.push.demo.frontend

@grails.validation.Validateable
class Device
{

  String deviceId

  static belongsTo = [videoPush:VideoPush]

  /* Override Grails persistency by user Cookie */
  static mapWith = "none"

  static constraints =
  { deviceId blank: false }
}
