package capptain.video.push.demo.frontend


class PushController
{

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  /* Get Domain Cookie Service */
  def propertiesCookieService;

  def index()
  {
    redirect(action: "edit")
  }

  def edit()
  {

    /* Send instance to view */
    [pushInstance: pushInstance]
  }

  def save(long version)
  {
    redirect(controller: "videoPush", action: "index");
  }
}
