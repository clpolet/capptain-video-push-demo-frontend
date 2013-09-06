package capptain.video.push.demo.frontend




class VideoPushController
{
  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  /* Get Domain Cookie Service */
  def propertiesCookieService;
  def videoPushService;

  def index()
  {
    /* Get an empty VideoPush for this new session */
    def videoPushInstance = new VideoPush();

    /* Get device from Cookie */
    videoPushInstance.device = new Device(propertiesCookieService.cookieToProperties(Device.class));

    /* Retrieve push properties from Cookie */
    videoPushInstance.push = new Push(propertiesCookieService.cookieToProperties(Push.class));

    /* Send videoPush Model to View */
    [videoPushInstance: videoPushInstance]
  }

  def sendVideoPush()
  {
    /* Retrieve VideoPush Values from HTML form */
    def videoPushInstance = new VideoPush(params.videoPush);

    /* Save push to Cookie */
    propertiesCookieService.propertiesToCookie(videoPushInstance.push.properties, Push.class);

    /* Save device to Cookie */
    propertiesCookieService.propertiesToCookie(videoPushInstance.device.properties, Device.class);

    /* Send video push */
    videoPushService.send(videoPushInstance);

    /* Go back to index */
    // render(view: "index", model: [videoPushInstance: videoPushInstance]);
  }
}

