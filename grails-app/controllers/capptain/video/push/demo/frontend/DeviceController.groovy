package capptain.video.push.demo.frontend

import org.springframework.dao.DataIntegrityViolationException

class DeviceController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index() {
        redirect(action: "list", params: params)
    }

    def list(Integer max) {
        params.max = Math.min(max ?: 10, 100)
        [deviceInstanceList: Device.list(params), deviceInstanceTotal: Device.count()]
    }

    def create() {
        [deviceInstance: new Device(params)]
    }

    def save() {
        def deviceInstance = new Device(params)
        if (!deviceInstance.save(flush: true)) {
            render(view: "create", model: [deviceInstance: deviceInstance])
            return
        }

        flash.message = message(code: 'default.created.message', args: [message(code: 'device.label', default: 'Device'), deviceInstance.id])
        redirect(action: "show", id: deviceInstance.id)
    }

    def show(Long id) {
        def deviceInstance = Device.get(id)
        if (!deviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'device.label', default: 'Device'), id])
            redirect(action: "list")
            return
        }

        [deviceInstance: deviceInstance]
    }

    def edit(Long id) {
        def deviceInstance = Device.get(id)
        if (!deviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'device.label', default: 'Device'), id])
            redirect(action: "list")
            return
        }

        [deviceInstance: deviceInstance]
    }

    def update(Long id, Long version) {
        def deviceInstance = Device.get(id)
        if (!deviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'device.label', default: 'Device'), id])
            redirect(action: "list")
            return
        }

        if (version != null) {
            if (deviceInstance.version > version) {
                deviceInstance.errors.rejectValue("version", "default.optimistic.locking.failure",
                          [message(code: 'device.label', default: 'Device')] as Object[],
                          "Another user has updated this Device while you were editing")
                render(view: "edit", model: [deviceInstance: deviceInstance])
                return
            }
        }

        deviceInstance.properties = params

        if (!deviceInstance.save(flush: true)) {
            render(view: "edit", model: [deviceInstance: deviceInstance])
            return
        }

        flash.message = message(code: 'default.updated.message', args: [message(code: 'device.label', default: 'Device'), deviceInstance.id])
        redirect(action: "show", id: deviceInstance.id)
    }

    def delete(Long id) {
        def deviceInstance = Device.get(id)
        if (!deviceInstance) {
            flash.message = message(code: 'default.not.found.message', args: [message(code: 'device.label', default: 'Device'), id])
            redirect(action: "list")
            return
        }

        try {
            deviceInstance.delete(flush: true)
            flash.message = message(code: 'default.deleted.message', args: [message(code: 'device.label', default: 'Device'), id])
            redirect(action: "list")
        }
        catch (DataIntegrityViolationException e) {
            flash.message = message(code: 'default.not.deleted.message', args: [message(code: 'device.label', default: 'Device'), id])
            redirect(action: "show", id: id)
        }
    }
}
