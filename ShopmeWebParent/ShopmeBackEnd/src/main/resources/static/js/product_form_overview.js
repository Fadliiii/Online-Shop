dropdownBrands = $("#brand")
dropdownCategories = $("#category")

$(document).ready(function() {

	$("#shortDescription").richText();
	$("#fullDescription").richText();


	dropdownBrands.change(function() {
		dropdownCategories.empty();
		getCategories();
	});
	getCategories();

	
});



function getCategories() {
	brandId = dropdownBrands.val();
	url = brandModuleURL + "/" + brandId + "/categories";

	$.get(url, function(responseJson) {
		$.each(responseJson, function(index, category) {
			$("<option>").val(category.id).text(category.name).appendTo(dropdownCategories);
		});
	});

}

function checkUnique(form) {
	let productId = $("#id").val();
	let productName = $("#name").val();

	csrfValue = $("input[name='_csrf']").val();

	let param = {
		id: productId,
		name: productName,
		_csrf: csrfValue
	};

	$.post(checkUniqueUrl, param, function(response) {

		if (response == "OK") {
			form.submit();
		}
		else if (response == "DUPLICATE") {
			showWarningModal("Product name already exist!" + productName);
		}

	}).fail(function() {
		showErrorModal("Could not connect to server");
	});
	return false;
}