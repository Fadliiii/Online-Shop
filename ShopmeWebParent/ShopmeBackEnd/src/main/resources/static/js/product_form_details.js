
function addNextDetailSection(){
	allDivDetails=$("[id^= 'divDetail']");
	divDetailsCount = allDivDetails.length;

	
	htmlDetailSection=`
	<div class="d-flex align-items-center" id="divDetail${divDetailsCount}">
			<label class="m-3">Name: </label>
			<input type="text" class="form-control w-25" name="detailsNames" maxlength="255">
			<label class="m-3">Value: </label>
			<input type="text" class="form-control w-25" name="detailsValues" maxlength="255">	
	</div>
	`;
	$("#divProductDetails").append(htmlDetailSection);
	
	previousDivDetailSection = allDivDetails.last();
	previousDivDetailID =previousDivDetailSection.attr("id");
	
		htmlLinkRemove = `
		    <a class="btn" title="remove this detail"
			href="javascript:removeDetailSectionById('${previousDivDetailID}')">
		        <i class="fa-solid fa-circle-xmark fa-2x icon-dark"></i>
		    </a>
		`;
	
	previousDivDetailSection.append(htmlLinkRemove);
	
	$("input[name='detailNames']").last();
}

function removeDetailSectionById(id)
{
	alert(id);
	$("#"+id).remove();
}
