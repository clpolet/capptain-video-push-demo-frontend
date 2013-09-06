<%@ page import="capptain.video.push.demo.frontend.Device"%>

<div class="content" role="main">
	<h1>
		<g:message code="device.form.label" />
	</h1>
	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>
	<g:hasErrors bean="${videoPushInstance?.device}">
		<ul class="errors" role="alert">
			<g:eachError bean="${videoPushInstance?.device}" var="error">
				<li
					<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
						error="${error}" /></li>
			</g:eachError>
		</ul>
	</g:hasErrors>
	<div
		class="fieldcontain ${hasErrors(bean: videoPushInstance?.device, field: 'videoPush.device.deviceId', 'error')} ">
		<label for="videoPush.device.deviceId"> <g:message
				code="device.deviceId.label" />

		</label>
		<g:textField name="videoPush.device.deviceId"
			value="${videoPushInstance?.device?.deviceId}" />
	</div>
</div>
