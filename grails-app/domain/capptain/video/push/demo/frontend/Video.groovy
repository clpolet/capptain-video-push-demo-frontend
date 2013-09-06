package capptain.video.push.demo.frontend

class Video
{

  String url

  static belongsTo = [videoPush:VideoPush]

  /* Disable persistency */
  static mapWith = "none"

  static constraints = { }
}
