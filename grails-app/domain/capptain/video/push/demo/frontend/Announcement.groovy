package capptain.video.push.demo.frontend

@grails.validation.Validateable
class Announcement
{
  String notificationMessage
  String actionUrl

  static belongsTo = [videoPush:VideoPush]

  /* Disable persistency */
  static mapWith = "none"

  static constraints =
  { actionUrl url: true, blank: false }
}
