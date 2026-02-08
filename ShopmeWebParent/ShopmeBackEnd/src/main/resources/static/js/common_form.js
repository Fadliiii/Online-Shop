$(document)
			.ready(
					function() {
						$("#buttonCancel").on("click", function() {
							window.location = moduleURL;
						});

						$("#fileImage")
								.change(
										function() {
											fileSize = this.files[0].size;

											if (fileSize > 102400) {
												this
														.setCustomValidity("You must chose an image less than 1MB!");
												this.reportValidity();
											} else {
												this.setCustomValidity("");
												showImageThumbnail(this);
											}
										});
					});

	function showImageThumbnail(fileInput) {
		var file = fileInput.files[0];
		var reader = new FileReader();
		reader.onload = function(e) {
			$("#thumbnail").attr("src", e.target.result);
		};
		reader.readAsDataURL(file);
	}
	
	function showModalDialog(title,message){
		document.getElementById("modalTitle").textContent = title;
		document.getElementById("modalBody").textContent = message;

		const modalEl = document.getElementById("modalDialog");
		const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
		modal.show();

	}

	function showErrorModal(message){
		showModalDialog("Error",massage);
	}
	function showWarningModal(message){
		showModalDialog("Warning",message);
	}