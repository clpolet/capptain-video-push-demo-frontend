package capptain.video.push.demo.frontend

@grails.validation.Validateable
class Push
{

  String appId
  String apiKey

  static belongsTo = [videoPush: VideoPush]

  /* Override Grails persistency by user Cookie */
  static mapWith = "none"

  static constraints =
  {
    appId blank: false
    apiKey blank: false
  }
}
