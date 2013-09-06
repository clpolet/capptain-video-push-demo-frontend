<div class="content" role="main">
<g:javascript> 
function refreshPreview()
{ 
    var videoUrl = document.getElementById("videoUrl");
    var videoPreview = document.getElementById("videoPreview");
    videoPreview.setAttribute("src", videoUrl.value);    
} 
</g:javascript> 
    
	<h1>
		<g:message code="video.form.label" />
	</h1>
	<g:if test="${flash.message}">
		<div class="message" role="status">
			${flash.message}
		</div>
	</g:if>
	<g:hasErrors bean="${videoPushInstance?.video}">
		<ul class="errors" role="alert">
			<g:eachError bean="${videoPushInstance?.video}" var="error">
				<li
					<g:if test="${error in org.springframework.validation.FieldError}">data-field-id="${error.field}"</g:if>><g:message
						error="${error}" /></li>
			</g:eachError>
		</ul>
	</g:hasErrors>

	<div
		class="fieldcontain ${hasErrors(bean: videoPushInstance?.video, field: 'videoPush.video.url', 'error')} ">
		<label for="videoPush.video.url"> <g:message code="video.url.label" />
		</label>
		<g:textField id="videoUrl" name="videoPush.video.url" value="${videoPushInstance?.video?.url}" onchange="refreshPreview();"/>
	</div>

	<div class="fieldcontain">
		<label for="preview"> <g:message code="video.preview.label" />
		</label>
		<video id="videoPreview" width="400" height="222" controls
			src="${videoPushInstance?.video?.url}"></video>
	</div>
</div>
