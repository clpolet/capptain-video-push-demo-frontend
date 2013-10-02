package capptain.video.push.demo.frontend

@grails.validation.Validateable
class Video
{

  String url

  static belongsTo = [videoPush:VideoPush]

  /* Disable persistency */
  static mapWith = "none"

  static constraints =
  {  url url:true, blank:false }
}
