<%@ page import="capptain.video.push.demo.frontend.Announcement"%>

<div class="content" role="main">
	<h1>
		<g:message code="announcement.form.label" />
	</h1>
	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>
	<g:hasErrors bean="${videoPushInstance?.announcement}">
		<ul class="errors" role="alert">
			<g:eachError bean="${videoPushInstance?.announcement}" var="error">
				<li
					<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
						error="${error}" /></li>
			</g:eachError>
		</ul>
	</g:hasErrors>

	<div
		class="fieldcontain ${hasErrors(bean: videoPushInstance?.announcement, field: 'videoPush.announcement.actionUrl', 'error')} ">
		<label for="videoPush.announcement.actionUrl"> <g:message
				code="announcement.actionUrl.label" />

		</label>
		<g:textField name="videoPush.announcement.actionUrl"
			value="${videoPushInstance?.announcement?.actionUrl}" />
	</div>
	<div
		class="fieldcontain ${hasErrors(bean: videoPushInstance?.announcement, field: 'videoPush.announcement.notificationMessage', 'error')} ">
		<label for="videoPush.announcement.notificationMessage"> <g:message
				code="announcement.notificationMessage.label" />

		</label>
		<g:textField name="videoPush.announcement.notificationMessage"
			value="${videoPushInstance?.announcement?.notificationMessage}" />
	</div>
</div>
