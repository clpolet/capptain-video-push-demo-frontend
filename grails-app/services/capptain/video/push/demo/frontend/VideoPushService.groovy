
package capptain.video.push.demo.frontend

import static groovyx.net.http.ContentType.URLENC

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import capptain.video.push.demo.frontend.constants.AnnouncementDefaultValues
import capptain.video.push.demo.frontend.constants.DataPushDefaultValues
import capptain.video.push.demo.frontend.constants.PushDefaultValues



class VideoPushService
{
  static transactional = false

  def send(VideoPush videoPushInstance)
  {
    def message
    def campaignId
    def baseUrl
    def timestamp = Calendar.instance.timeInMillis
    def testKey =  hmac_sha1(videoPushInstance.push.apiKey, videoPushInstance.device.deviceId )
    def appInfoName = PushDefaultValues.CATEGORY+timestamp
    def jsonAudience = ["expression":"VideoDownloaded","criteria":["VideoDownloaded":[type: [generic:"boolean", name: appInfoName], value: true]]]
    def activateKey

    /* Announcement payload */
    def jsonAnnouncement = ["name": PushDefaultValues.NAME +" "+ AnnouncementDefaultValues.KIND +" "+ timestamp, "category":PushDefaultValues.CATEGORY, "audience": jsonAudience, "pushMode":PushDefaultValues.PUSH_MODE, type: PushDefaultValues.TYPE, "deliveryTime": AnnouncementDefaultValues.DELIVERY_TIME, "notificationType": AnnouncementDefaultValues.NOTIFICATION_TYPE, "notificationMessage":videoPushInstance.announcement.notificationMessage, "title":videoPushInstance.announcement.notificationMessage, "body":appInfoName, "actionButtonText":AnnouncementDefaultValues.ACTION_BUTTON_TEXT, "exitButtonText":AnnouncementDefaultValues.EXIT_BUTTON_TEXT,"actionUrl":videoPushInstance.announcement.actionUrl].encodeAsJSON()
    println(jsonAnnouncement);
    def createKey = hmac_sha1(videoPushInstance.push.apiKey, jsonAnnouncement )

    /* Datapush payload */
    def jsonDataPush = ["name": PushDefaultValues.NAME +" "+ DataPushDefaultValues.KIND +" "+ timestamp, "category":PushDefaultValues.CATEGORY, "type": PushDefaultValues.TYPE, "deliveryTime": DataPushDefaultValues.DELIVERY_TIME, "body":appInfoName,"filepush": ["url":videoPushInstance.video.url]].encodeAsJSON()

    /* Create Announcement campaign */
    baseUrl = String.format(PushDefaultValues.URL_FORMAT, PushDefaultValues.DOMAIN, PushDefaultValues.CREATE_METHOD)
    message = ["appid" : videoPushInstance.push.appId, "kind" : AnnouncementDefaultValues.KIND, "data" : jsonAnnouncement, "deviceid" : videoPushInstance.device.deviceId, "key" :createKey];
    campaignId = sendPush(message,baseUrl)

    /* Activate Announcement campaign */
    activateKey = hmac_sha1(videoPushInstance.push.apiKey, campaignId )
    baseUrl = String.format(PushDefaultValues.URL_FORMAT, PushDefaultValues.DOMAIN, PushDefaultValues.ACTIVATE_METHOD)
    message = ["appid" : videoPushInstance.push.appId, "kind" : AnnouncementDefaultValues.KIND, "id" : campaignId, "deviceid" : videoPushInstance.device.deviceId, "key" : activateKey];
    sendPush(message,baseUrl)

//    /* Create Announcement campaign */
//    createKey = hmac_sha1(videoPushInstance.push.apiKey, jsonDataPush )
//    baseUrl = String.format(PushDefaultValues.URL_FORMAT, PushDefaultValues.DOMAIN, PushDefaultValues.CREATE_METHOD)
//    message = ["appid" : videoPushInstance.push.appId, "kind" : DataPushDefaultValues.KIND, "data" : jsonDataPush, "deviceid" : videoPushInstance.device.deviceId, "key" : createKey];
//    campaignId = sendPush(message,baseUrl)
//
//    /* Activate Announcement campaign */
//    activateKey = hmac_sha1(videoPushInstance.push.apiKey, campaignId )
//    baseUrl = String.format(PushDefaultValues.URL_FORMAT, PushDefaultValues.DOMAIN, PushDefaultValues.ACTIVATE_METHOD)
//    message = ["appid" : videoPushInstance.push.appId, "kind" : DataPushDefaultValues.KIND, "id" : campaignId, "deviceid" : videoPushInstance.device.deviceId, "key" : activateKey];
//    sendPush(message,baseUrl)

    /* Send a DataPush test campaign */
        baseUrl = String.format(PushDefaultValues.URL_FORMAT, PushDefaultValues.DOMAIN, PushDefaultValues.TEST_METHOD)
        message = ["appid" : videoPushInstance.push.appId, "kind" : DataPushDefaultValues.KIND, "data" : jsonDataPush, "deviceid" : videoPushInstance.device.deviceId, "key" : testKey];
        sendPush(message,baseUrl)
  }

  private def sendPush(campaign, baseUrl)
  {
    withHttp(uri: baseUrl)
    {
      handler.failure =
      { resp -> println resp.statusLine }
      def html = post(body : campaign, requestContentType : URLENC)
      return html?.getText()
    }
  }

  private def hmac_sha1(String secretKey, String data)
  {
    String hmacKey;

    /* Create signign key */
    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");

    /* hmac_sha1 Mac instance */
    Mac mac = Mac.getInstance("HmacSHA1");

    /* init with signing key */
    mac.init(signingKey);

    /* caluclate hmacKey */
    byte[] rawHmac = mac.doFinal(data.getBytes());
    hmacKey = rawHmac.encodeHex()

    return hmacKey;
  }
}
