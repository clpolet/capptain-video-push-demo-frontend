package capptain.video.push.demo.frontend

import grails.validation.ValidationException




class VideoPushController
{
  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  /* Get Domain Cookie Service */
  def propertiesCookieService;
  def videoPushService;

  /**
   * Video push frontend index page processing
   * @return videoPushInstance the video push
   */
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

  /**
   * Get video push from user form then send it
   */
  def sendVideoPush()
  {
    /* Retrieve VideoPush Values from HTML form */
    def videoPushInstance = new VideoPush(params.videoPush);

    /* Validate form */
    if(!(videoPushInstance.validate()))
    {
      render(view: "index", model: [videoPushInstance: videoPushInstance]);
      return
    }

    /* Save push to Cookie */
    propertiesCookieService.propertiesToCookie(videoPushInstance.push.properties, Push.class);

    /* Save device to Cookie */
    propertiesCookieService.propertiesToCookie(videoPushInstance.device.properties, Device.class);

    /* Send video push */
    try
    {
      videoPushService.send(videoPushInstance);
    }catch (ValidationException e)
    {
      videoPushInstance.errors = e.errors
    }

    /* Go back to index */
    render(view: "index", model: [videoPushInstance: videoPushInstance]);
  }
}

