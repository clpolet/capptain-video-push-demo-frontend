package capptain.video.push.demo.frontend

class VideoPush
{
  static hasOne = [push: Push, announcement: Announcement, datapush: DataPush, device: Device, video: Video]

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
  public void setDataPush(DataPush datapush)
  {
    this.datapush = datapush;
    datapush.videoPush = this;
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

  /* Constraints on properties */
  static constraints =
  {
  }
}
