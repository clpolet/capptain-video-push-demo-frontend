package capptain.video.push.demo.frontend

import org.springframework.dao.DataIntegrityViolationException

class VideoController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [videoInstanceList: Video.list(params), videoInstanceTotal: Video.count()]
    }

    def create() {
        [videoInstance: new Video(params)]
    }

    def save() {
        def videoInstance = new Video(params)
        if (!videoInstance.save(flush: true)) {
            render(view: "create", model: [videoInstance: videoInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'video.label', default: 'Video'), videoInstance.id])
        redirect(action: "show", id: videoInstance.id)
    }

    def show(Long id) {
        def videoInstance = Video.get(id)
        if (!videoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'video.label', default: 'Video'), id])
            redirect(action: "list")
            return
        }

        [videoInstance: videoInstance]
    }

    def edit(Long id) {
        def videoInstance = Video.get(id)
        if (!videoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'video.label', default: 'Video'), id])
            redirect(action: "list")
            return
        }

        [videoInstance: videoInstance]
    }

    def update(Long id, Long version) {
        def videoInstance = Video.get(id)
        if (!videoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'video.label', default: 'Video'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (videoInstance.version > version) {
                videoInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'video.label', default: 'Video')] as Object[],
                          "Another user has updated this Video while you were editing")
                render(view: "edit", model: [videoInstance: videoInstance])
                return
            }
        }

        videoInstance.properties = params

        if (!videoInstance.save(flush: true)) {
            render(view: "edit", model: [videoInstance: videoInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'video.label', default: 'Video'), videoInstance.id])
        redirect(action: "show", id: videoInstance.id)
    }

    def delete(Long id) {
        def videoInstance = Video.get(id)
        if (!videoInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'video.label', default: 'Video'), id])
            redirect(action: "list")
            return
        }

        try {
            videoInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'video.label', default: 'Video'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'video.label', default: 'Video'), id])
            redirect(action: "show", id: id)
        }
    }
}
