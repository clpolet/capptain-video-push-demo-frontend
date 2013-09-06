package capptain.video.push.demo.frontend



import org.junit.*
import grails.test.mixin.*

@TestFor(DeviceController)
@Mock(Device)
class DeviceControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/device/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.deviceInstanceList.size() == 0
        assert model.deviceInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.deviceInstance != null
    }

    void testSave() {
        controller.save()

        assert model.deviceInstance != null
        assert view == '/device/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/device/show/1'
        assert controller.flash.message != null
        assert Device.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/device/list'

        populateValidParams(params)
        def device = new Device(params)

        assert device.save() != null

        params.id = device.id

        def model = controller.show()

        assert model.deviceInstance == device
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/device/list'

        populateValidParams(params)
        def device = new Device(params)

        assert device.save() != null

        params.id = device.id

        def model = controller.edit()

        assert model.deviceInstance == device
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/device/list'

        response.reset()

        populateValidParams(params)
        def device = new Device(params)

        assert device.save() != null

        // test invalid parameters in update
        params.id = device.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/device/edit"
        assert model.deviceInstance != null

        device.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/device/show/$device.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        device.clearErrors()

        populateValidParams(params)
        params.id = device.id
        params.version = -1
        controller.update()

        assert view == "/device/edit"
        assert model.deviceInstance != null
        assert model.deviceInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/device/list'

        response.reset()

        populateValidParams(params)
        def device = new Device(params)

        assert device.save() != null
        assert Device.count() == 1

        params.id = device.id

        controller.delete()

        assert Device.count() == 0
        assert Device.get(device.id) == null
        assert response.redirectedUrl == '/device/list'
    }
}
