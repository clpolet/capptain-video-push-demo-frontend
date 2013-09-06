class UrlMappings {

  static mappings = {
    "/$controller/$action?/$id?"{ constraints { } }

    /* Define index controller and action */
    "/" {
      controller = "VideoPush"
      action = "index"
    }
    "500"(view:'/error')
  }
}
