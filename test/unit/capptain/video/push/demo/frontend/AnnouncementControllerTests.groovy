package capptain.video.push.demo.frontend



import org.junit.*
import grails.test.mixin.*

@TestFor(AnnouncementController)
@Mock(Announcement)
class AnnouncementControllerTests {

    def populateValidParams(params) {
        assert params != null
        // TODO: Populate valid properties like...
        //params["name"] = 'someValidName'
    }

    void testIndex() {
        controller.index()
        assert "/announcement/list" == response.redirectedUrl
    }

    void testList() {

        def model = controller.list()

        assert model.announcementInstanceList.size() == 0
        assert model.announcementInstanceTotal == 0
    }

    void testCreate() {
        def model = controller.create()

        assert model.announcementInstance != null
    }

    void testSave() {
        controller.save()

        assert model.announcementInstance != null
        assert view == '/announcement/create'

        response.reset()

        populateValidParams(params)
        controller.save()

        assert response.redirectedUrl == '/announcement/show/1'
        assert controller.flash.message != null
        assert Announcement.count() == 1
    }

    void testShow() {
        controller.show()

        assert flash.message != null
        assert response.redirectedUrl == '/announcement/list'

        populateValidParams(params)
        def announcement = new Announcement(params)

        assert announcement.save() != null

        params.id = announcement.id

        def model = controller.show()

        assert model.announcementInstance == announcement
    }

    void testEdit() {
        controller.edit()

        assert flash.message != null
        assert response.redirectedUrl == '/announcement/list'

        populateValidParams(params)
        def announcement = new Announcement(params)

        assert announcement.save() != null

        params.id = announcement.id

        def model = controller.edit()

        assert model.announcementInstance == announcement
    }

    void testUpdate() {
        controller.update()

        assert flash.message != null
        assert response.redirectedUrl == '/announcement/list'

        response.reset()

        populateValidParams(params)
        def announcement = new Announcement(params)

        assert announcement.save() != null

        // test invalid parameters in update
        params.id = announcement.id
        //TODO: add invalid values to params object

        controller.update()

        assert view == "/announcement/edit"
        assert model.announcementInstance != null

        announcement.clearErrors()

        populateValidParams(params)
        controller.update()

        assert response.redirectedUrl == "/announcement/show/$announcement.id"
        assert flash.message != null

        //test outdated version number
        response.reset()
        announcement.clearErrors()

        populateValidParams(params)
        params.id = announcement.id
        params.version = -1
        controller.update()

        assert view == "/announcement/edit"
        assert model.announcementInstance != null
        assert model.announcementInstance.errors.getFieldError('version')
        assert flash.message != null
    }

    void testDelete() {
        controller.delete()
        assert flash.message != null
        assert response.redirectedUrl == '/announcement/list'

        response.reset()

        populateValidParams(params)
        def announcement = new Announcement(params)

        assert announcement.save() != null
        assert Announcement.count() == 1

        params.id = announcement.id

        controller.delete()

        assert Announcement.count() == 0
        assert Announcement.get(announcement.id) == null
        assert response.redirectedUrl == '/announcement/list'
    }
}
