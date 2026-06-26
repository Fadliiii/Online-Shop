	function showModalDialog(title, message) {
		document.getElementById("modalTitle").textContent = title;
		document.getElementById("modalBody").textContent = message;

		const modalEl = document.getElementById("modalDialog");
		const modal = bootstrap.Modal.getOrCreateInstance(modalEl);
		modal.show();

	}

	function showErrorModal(message) {
		showModalDialog("Error", message);
	}
	function showWarningModal(message) {
		showModalDialog("Warning", message);
	}