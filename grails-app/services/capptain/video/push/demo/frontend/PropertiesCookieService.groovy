package capptain.video.push.demo.frontend

import org.codehaus.groovy.grails.web.binding.DataBindingLazyMetaPropertyMap

import com.dalew.CookieService

class PropertiesCookieService extends CookieService
{
  def propertiesToCookie(DataBindingLazyMetaPropertyMap properties, Class clazz)
  {
    super.set(clazz.simpleName, properties.encodeAsJSON());
  }

  def cookieToProperties(Class clazz)
  {
    def properties = super.get(clazz.simpleName);

    if (properties)
    {
      properties = grails.converters.JSON.parse(properties)
    }

    return  properties;
  }
}
