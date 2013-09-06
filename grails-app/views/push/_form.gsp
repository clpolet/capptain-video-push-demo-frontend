<%@ page import="capptain.video.push.demo.frontend.Push" %>


<div class="content" role="main">
    <h1>
        <g:message code="push.form.label" />
    </h1>
    <g:if test="${flash.message}">
        <div class="message" role="status">
            ${flash.message}
        </div>
    </g:if>
    <g:hasErrors bean="${videoPushInstance?.push}">
        <ul class="errors" role="alert">
            <g:eachError bean="${videoPushInstance?.push}" var="error">
                <li
                    <g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
                        error="${error}" /></li>
            </g:eachError>
        </ul>
    </g:hasErrors>
<div class="fieldcontain ${hasErrors(bean: videoPushInstance?.push, field: 'appId', 'error')} ">
	<label for="videoPush.push.appId">
		<g:message code="push.appId.label" />
		
	</label>
	<g:textField name="videoPush.push.appId" value="${videoPushInstance?.push?.appId}"/>
</div>
<div class="fieldcontain ${hasErrors(bean: videoPushInstance?.push, field: 'apiKey', 'error')} ">
    <label for="videoPush.push.apiKey">
        <g:message code="push.apiKey.label" />
        
    </label>
    <g:textField name="videoPush.push.apiKey" value="${videoPushInstance?.push?.apiKey}"/>
</div>
</div>

