package capptain.video.push.demo.frontend



import org.junit.*
import grails.test.mixin.*

@TestFor(DataPushController)
@Mock(DataPush)
class DataPushControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/datapush/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.datapushInstanceList.size() == 0
        assert model.datapushInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.datapushInstance != null
    }

    void testSave() {
        controller.save()

        assert model.datapushInstance != null
        assert view == '/datapush/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/datapush/show/1'
        assert controller.flash.message != null
        assert DataPush.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/datapush/list'

        populateValidParams(params)
        def datapush = new DataPush(params)

        assert datapush.save() != null

        params.id = datapush.id

        def model = controller.show()

        assert model.datapushInstance == datapush
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/datapush/list'

        populateValidParams(params)
        def datapush = new DataPush(params)

        assert datapush.save() != null

        params.id = datapush.id

        def model = controller.edit()

        assert model.datapushInstance == datapush
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/datapush/list'

        response.reset()

        populateValidParams(params)
        def datapush = new DataPush(params)

        assert datapush.save() != null

        // test invalid parameters in update
        params.id = datapush.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/datapush/edit"
        assert model.datapushInstance != null

        datapush.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/datapush/show/$datapush.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        datapush.clearErrors()

        populateValidParams(params)
        params.id = datapush.id
        params.version = -1
        controller.update()

        assert view == "/datapush/edit"
        assert model.datapushInstance != null
        assert model.datapushInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/datapush/list'

        response.reset()

        populateValidParams(params)
        def datapush = new DataPush(params)

        assert datapush.save() != null
        assert DataPush.count() == 1

        params.id = datapush.id

        controller.delete()

        assert DataPush.count() == 0
        assert DataPush.get(datapush.id) == null
        assert response.redirectedUrl == '/datapush/list'
    }
}
