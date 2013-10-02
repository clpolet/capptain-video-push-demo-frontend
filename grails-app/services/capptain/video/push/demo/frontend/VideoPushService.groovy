
package capptain.video.push.demo.frontend

import static groovyx.net.http.ContentType.URLENC
import grails.validation.ValidationException

import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec

import capptain.video.push.demo.frontend.constants.AnnouncementDefaultValues
import capptain.video.push.demo.frontend.constants.DataPushDefaultValues
import capptain.video.push.demo.frontend.constants.PushDefaultValues



class VideoPushService
{
  static transactional = false

  /**
   * Send the video push
   * @param videoPushInstance the video push object to be sent
   */
  def send(VideoPush videoPushInstance)
  {
    def message
    def campaignId
    def baseUrl
    def timestamp = Calendar.instance.timeInMillis
    def appInfoName = PushDefaultValues.CATEGORY+timestamp
    def jsonAudience = ["expression":"VideoDownloaded","criteria":["VideoDownloaded":[type: [generic:"boolean", name: appInfoName], value: true]]]

    /* Announcement payload */
    def jsonAnnouncement = ["name": PushDefaultValues.NAME +" "+ AnnouncementDefaultValues.KIND +" "+ timestamp, "category":PushDefaultValues.CATEGORY, "audience": jsonAudience, "pushMode":PushDefaultValues.PUSH_MODE, type: PushDefaultValues.TYPE, "deliveryTime": AnnouncementDefaultValues.DELIVERY_TIME, "notificationType": AnnouncementDefaultValues.NOTIFICATION_TYPE, "notificationMessage":videoPushInstance.announcement.notificationMessage, "body":appInfoName, "actionButtonText":AnnouncementDefaultValues.ACTION_BUTTON_TEXT,"actionUrl":videoPushInstance.announcement.actionUrl].encodeAsJSON()
    println(jsonAnnouncement);

    /* Announcement creation key */
    def createKey = hmac_sha1(videoPushInstance.push.apiKey, jsonAnnouncement )

    /* Datapush payload */
    def jsonDataPush = ["name": PushDefaultValues.NAME +" "+ DataPushDefaultValues.KIND +" "+ timestamp, "category":PushDefaultValues.CATEGORY, "type": PushDefaultValues.TYPE, "deliveryTime": DataPushDefaultValues.DELIVERY_TIME, "body":appInfoName,"filepush": ["url":videoPushInstance.video.url]].encodeAsJSON()

    /* Datapush test key */
    def testKey =  hmac_sha1(videoPushInstance.push.apiKey, videoPushInstance.device.deviceId )

    /* Create Announcement campaign */
    baseUrl = String.format(PushDefaultValues.URL_FORMAT, PushDefaultValues.DOMAIN, PushDefaultValues.CREATE_METHOD)
    message = ["appid" : videoPushInstance.push.appId, "kind" : AnnouncementDefaultValues.KIND, "data" : jsonAnnouncement, "deviceid" : videoPushInstance.device.deviceId, "key" :createKey];
    campaignId = sendPush(message,baseUrl)
    if (!campaignId)
    {
      /* Add an error to video push instance */
      videoPushInstance.errors.reject("videopush.api.error")
      throw new ValidationException("Unable to send video push", videoPushInstance.errors)
    }


    /* Announcement activation key */
    def activateKey = hmac_sha1(videoPushInstance.push.apiKey, campaignId )

    /* Activate Announcement campaign */
    baseUrl = String.format(PushDefaultValues.URL_FORMAT, PushDefaultValues.DOMAIN, PushDefaultValues.ACTIVATE_METHOD)
    message = ["appid" : videoPushInstance.push.appId, "kind" : AnnouncementDefaultValues.KIND, "id" : campaignId, "deviceid" : videoPushInstance.device.deviceId, "key" : activateKey];
    if (sendPush(message,baseUrl))
    {
      /* Add an error to video push instance */
      videoPushInstance.errors.reject("videopush.api.error")
      throw new ValidationException("Unable to send video push", videoPushInstance.errors)
    }

    /* Send DataPush test campaign */
    baseUrl = String.format(PushDefaultValues.URL_FORMAT, PushDefaultValues.DOMAIN, PushDefaultValues.TEST_METHOD)
    message = ["appid" : videoPushInstance.push.appId, "kind" : DataPushDefaultValues.KIND, "data" : jsonDataPush, "deviceid" : videoPushInstance.device.deviceId, "key" : testKey];
    if (sendPush(message,baseUrl))
    {
      /* Add an error to video push instance */
      videoPushInstance.errors.reject("videopush.api.error")
      throw new ValidationException("Unable to send video push", videoPushInstance.errors)
    }
  }

  /**
   * Send a push to Capptain's platform
   * @param campaign campaign payload
   * @param baseUrl Capptain's platform URL
   * @return response
   */
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

  /**
   * Generate an hmac sha1 key 
   * @param secretKey the secret key
   * @param data data
   * @return an hmac-sha1 key
   */
  private def hmac_sha1(String secretKey, String data)
  {
    String hmacKey;

    /* Create signing key */
    SecretKeySpec signingKey = new SecretKeySpec(secretKey.getBytes(), "HmacSHA1");

    /* hmac_sha1 Mac instance */
    Mac mac = Mac.getInstance("HmacSHA1");

    /* init with signing key */
    mac.init(signingKey);

    /* calculate hmacKey */
    byte[] rawHmac = mac.doFinal(data.getBytes());
    hmacKey = rawHmac.encodeHex()

    return hmacKey;
  }
}
