package capptain.video.push.demo.frontend

import org.springframework.dao.DataIntegrityViolationException

class DataPushController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [datapushInstanceList: DataPush.list(params), datapushInstanceTotal: DataPush.count()]
    }

    def create() {
        [datapushInstance: new DataPush(params)]
    }

    def save() {
        def datapushInstance = new DataPush(params)
        if (!datapushInstance.save(flush: true)) {
            render(view: "create", model: [datapushInstance: datapushInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'datapush.label', default: 'Datapush'), datapushInstance.id])
        redirect(action: "show", id: datapushInstance.id)
    }

    def show(Long id) {
        def datapushInstance = DataPush.get(id)
        if (!datapushInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'datapush.label', default: 'Datapush'), id])
            redirect(action: "list")
            return
        }

        [datapushInstance: datapushInstance]
    }

    def edit(Long id) {
        def datapushInstance = DataPush.get(id)
        if (!datapushInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'datapush.label', default: 'Datapush'), id])
            redirect(action: "list")
            return
        }

        [datapushInstance: datapushInstance]
    }

    def update(Long id, Long version) {
        def datapushInstance = DataPush.get(id)
        if (!datapushInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'datapush.label', default: 'Datapush'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (datapushInstance.version > version) {
                datapushInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'datapush.label', default: 'Datapush')] as Object[],
                          "Another user has updated this Datapush while you were editing")
                render(view: "edit", model: [datapushInstance: datapushInstance])
                return
            }
        }

        datapushInstance.properties = params

        if (!datapushInstance.save(flush: true)) {
            render(view: "edit", model: [datapushInstance: datapushInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'datapush.label', default: 'Datapush'), datapushInstance.id])
        redirect(action: "show", id: datapushInstance.id)
    }

    def delete(Long id) {
        def datapushInstance = DataPush.get(id)
        if (!datapushInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'datapush.label', default: 'Datapush'), id])
            redirect(action: "list")
            return
        }

        try {
            datapushInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'datapush.label', default: 'Datapush'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'datapush.label', default: 'Datapush'), id])
            redirect(action: "show", id: id)
        }
    }
}
