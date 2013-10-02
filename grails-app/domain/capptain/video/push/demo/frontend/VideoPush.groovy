package capptain.video.push.demo.frontend

@grails.validation.Validateable
class VideoPush
{
  static hasOne = [push: Push, announcement: Announcement, device: Device, video: Video]

  /* Override setters for bidirectional relationship */
  public void setPush(Push push)
  {
    this.push = push;
    push.videoPush = this;
  }
  public void setAnnouncement(Announcement announcement)
  {
    this.announcement = announcement;
    announcement.videoPush = this;
  }
  public void setDevice(Device device)
  {
    this.device = device;
    device.videoPush = this;
  }
  public void setVideo(Video video)
  {
    this.video = video;
    video.videoPush = this;
  }

  /* Disable persistency */
  static mapWith = "none"

  /* Sub validator for nested validation */
  static subValidator =
  {value, object ->
    return value.validate()
  }

  /* Constraints on properties */
  static constraints =
  {
    push nullable: false, validator: subValidator
    announcement nullable: false, validator: subValidator
    device nullable: false, validator: subValidator
    video nullable: false, validator: subValidator
  }
}
