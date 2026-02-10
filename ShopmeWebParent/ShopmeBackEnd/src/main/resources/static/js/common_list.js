function clearFilter(){
	window.location = moduleURL;
}

function showDeleteConfirmModal(link,entityName){
	entityId = link.attr("entityID");
	$("#yesButton").attr("href",link.attr("href"));
	$("#confirmText").text("Are you sure you want to delete this"
		+entityName+" ID "+ entityId + " ?");
		
		let confirmModal = new bootstrap.Modal(
		      document.getElementById("confirmModal")
		  );
		  confirmModal.show();
}

