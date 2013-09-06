package capptain.video.push.demo.frontend



import org.junit.*
import grails.test.mixin.*

@TestFor(PushController)
@Mock(Push)
class PushControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/push/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.pushInstanceList.size() == 0
        assert model.pushInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.pushInstance != null
    }

    void testSave() {
        controller.save()

        assert model.pushInstance != null
        assert view == '/push/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/push/show/1'
        assert controller.flash.message != null
        assert Push.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/push/list'

        populateValidParams(params)
        def push = new Push(params)

        assert push.save() != null

        params.id = push.id

        def model = controller.show()

        assert model.pushInstance == push
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/push/list'

        populateValidParams(params)
        def push = new Push(params)

        assert push.save() != null

        params.id = push.id

        def model = controller.edit()

        assert model.pushInstance == push
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/push/list'

        response.reset()

        populateValidParams(params)
        def push = new Push(params)

        assert push.save() != null

        // test invalid parameters in update
        params.id = push.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/push/edit"
        assert model.pushInstance != null

        push.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/push/show/$push.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        push.clearErrors()

        populateValidParams(params)
        params.id = push.id
        params.version = -1
        controller.update()

        assert view == "/push/edit"
        assert model.pushInstance != null
        assert model.pushInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/push/list'

        response.reset()

        populateValidParams(params)
        def push = new Push(params)

        assert push.save() != null
        assert Push.count() == 1

        params.id = push.id

        controller.delete()

        assert Push.count() == 0
        assert Push.get(push.id) == null
        assert response.redirectedUrl == '/push/list'
    }
}
