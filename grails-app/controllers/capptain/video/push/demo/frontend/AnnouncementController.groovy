package capptain.video.push.demo.frontend

import org.springframework.dao.DataIntegrityViolationException

class AnnouncementController {

  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

  def index() {
    redirect(action: "list", params: params)
  }

  def create() {
    [announcementInstance: new Announcement(params)]
  }

  def save() {
    def announcementInstance = new Announcement(params)
    if (!announcementInstance.save(flush: true)) {
      render(view: "create", model: [announcementInstance: announcementInstance])
      return
    }

    flash.message = message(code: 'default.created.message', args: [
      message(code: 'announcement.label', default: 'Announcement'),
      announcementInstance.id
    ])
    redirect(action: "show", id: announcementInstance.id)
  }

  def show(Long id) {
    def announcementInstance = Announcement.get(id)
    if (!announcementInstance) {
      flash.message = message(code: 'default.not.found.message', args: [
        message(code: 'announcement.label', default: 'Announcement'),
        id
      ])
      redirect(action: "list")
      return
    }

    [announcementInstance: announcementInstance]
  }

  def edit(Long id) {
    def announcementInstance = Announcement.get(id)
    if (!announcementInstance) {
      flash.message = message(code: 'default.not.found.message', args: [
        message(code: 'announcement.label', default: 'Announcement'),
        id
      ])
      redirect(action: "list")
      return
    }

    [announcementInstance: announcementInstance]
  }

  def update(Long id, Long version) {
    def announcementInstance = Announcement.get(id)
    if (!announcementInstance) {
      flash.message = message(code: 'default.not.found.message', args: [
        message(code: 'announcement.label', default: 'Announcement'),
        id
      ])
      redirect(action: "list")
      return
    }

    if (version != null) {
      if (announcementInstance.version > version) {
        announcementInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
            [
              message(code: 'announcement.label', default: 'Announcement')] as Object[],
            "Another user has updated this Announcement while you were editing")
        render(view: "edit", model: [announcementInstance: announcementInstance])
        return
      }
    }

    announcementInstance.properties = params

    if (!announcementInstance.save(flush: true)) {
      render(view: "edit", model: [announcementInstance: announcementInstance])
      return
    }

    flash.message = message(code: 'default.updated.message', args: [
      message(code: 'announcement.label', default: 'Announcement'),
      announcementInstance.id
    ])
    redirect(action: "show", id: announcementInstance.id)
  }

  def delete(Long id) {
    def announcementInstance = Announcement.get(id)
    if (!announcementInstance) {
      flash.message = message(code: 'default.not.found.message', args: [
        message(code: 'announcement.label', default: 'Announcement'),
        id
      ])
      redirect(action: "list")
      return
    }

    try {
      announcementInstance.delete(flush: true)
      flash.message = message(code: 'default.deleted.message', args: [
        message(code: 'announcement.label', default: 'Announcement'),
        id
      ])
      redirect(action: "list")
    }
    catch (DataIntegrityViolationException e) {
      flash.message = message(code: 'default.not.deleted.message', args: [
        message(code: 'announcement.label', default: 'Announcement'),
        id
      ])
      redirect(action: "show", id: id)
    }
  }
}
