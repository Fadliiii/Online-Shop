var buttonLoad4States;
var dropDownCountry4States;
var dropDownStates;
var buttonAddState;
var buttonUpdateState;
var buttonDeleteState;
var labelStateName;
var fieldStateName;


$(document).ready(function(){
	buttonLoad4States = $("#buttonLoadCountriesForStates");
	dropDownCountry4States=$("#dropDownCountriesForStates");
	dropDownStates = $("#dropDownStates");
	buttonAddState =$("#buttonAddState");
	buttonUpdateState =$("#buttonUpdateState");
	buttonDeleteState =$("#buttonDeleteState");
	labelStateName =$("#labelStateName");
	fieldStateName =$("#fieldStateName");

	
	buttonLoad4States.click(function(){
		loadCountries4States();
	});
	dropDownCountry4States.on("change",function(){
		loadStates4Countries();
	});
	dropDownStates.on("change",function(){
		changeFormStateToSelectedState();
	});
	buttonAddState.click(function(){
		if(buttonAddState.val() == "Add"){
			addState();
		}else{
			changeFormStateToNew();
		}
	});
	buttonUpdateState.click(function(){
		updateState();
	});
	buttonDeleteState.click(function(){
		deleteState();
	});
});

function deleteState(){
stateId = dropDownStates.val();
url = contextPath+"states/delete/"+stateId;
$.get(url,function(){
		$("#dropDownStates option[value='"+stateId+"']").remove();
		changeFormStateToNew();	
	}).done(function(){
		showToastMessage(" State has been deleted");
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
	
}

function updateState(){
	url = contextPath+"states/save";
	stateId = dropDownStates.val();
	stateName = fieldStateName.val();
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val().split("-")[0];
	
	if(stateId === ""){
						showToastMessage("Choose State First");
						fieldStateName.focus();
						return;
					}
	
	if(stateName === ""){
					showToastMessage("Choose State First");
					fieldStateName.focus();
					return;
				}
		
		if(countryId === ""){
				showToastMessage("Choose Country First");
				fieldStateName.focus();
				return;
			}
			jsonData={id:stateId,name:stateName,country:{id:countryId}};
		
				$.ajax({
					type:'POST',
					url:url,
					beforeSend:function(xhr){
						xhr.setRequestHeader(csrfHeaderName,csrfValue);		
					},
					data: JSON.stringify(jsonData),
					contentType: 'application/json'
				}).done(function(stateId){
					$("#dropDownStates option:selected").text(stateName);
					showToastMessage("The new state has been update");
				}).fail(function(){
					showToastMessage("ERROR: Could not connect to server or server encountered an error")
				});
}

function addState(){
	url = contextPath+"states/save";
	
	stateName = fieldStateName.val();
	
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val().split("-")[0];
	countryName = selectedCountry.text();
	
	if(stateName === ""){
				showToastMessage("State name is required");
				fieldStateName.focus();
				return;
			}
	
	if(countryName === ""){
			showToastMessage("Choose Country First");
			fieldStateName.focus();
			return;
		}
	
	if(countryId === ""){
					showToastMessage("Choose Country First");
					fieldStateName.focus();
				return;
		}	
	jsonData={name:stateName,country:{id:countryId,name:countryName}};
	
	$.ajax({
		type:'POST',
		url:url,
		beforeSend:function(xhr){
			xhr.setRequestHeader(csrfHeaderName,csrfValue);		
		},
		data: JSON.stringify(jsonData),
		contentType: 'application/json'
	}).done(function(stateId){
		selectNewlyAddState(stateId,stateName);
		showToastMessage("The new state has been added");
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
}

function selectNewlyAddState(stateId,stateName){
	$("<option>").val(stateId).text(stateName).appendTo(dropDownStates);
	
	$("#dropDownStates option[value='"+stateId+"']").prop("selected",true);
	
	fieldStateName.val("").focus();
}

function changeFormStateToSelectedState(){
		buttonAddState.prop("value","New");
		buttonUpdateState.prop("disabled",false);
		buttonDeleteState.prop("disabled",false);
		
		labelStateName.text("Selected State :");

		selectedStateName = $("#dropDownStates option:selected").text();
		fieldStateName.val(selectedStateName);
		

}

function changeFormStateToNew(){
		buttonAddState.val("Add");
		labelStateName.text("State/Province Name:");
		
		buttonUpdateState.prop("disabled",true);
		buttonDeleteState.prop("disabled",true);
		
		fieldStateName.val("").focus();
}

function loadStates4Countries(){
	selectedCountry = $("#dropDownCountriesForStates option:selected");
	countryId = selectedCountry.val().split("-")[0];
	 
	url = contextPath+"states/list_by_country/"+countryId;
	
	$.get(url,function(responseJson){
		dropDownStates.empty();
		
		$.each(responseJson,function(index,state){
			$("<option>").val(state.id).text(state.name).appendTo("#dropDownStates");
		});
	}).done(function(){
		changeFormStateToNew();
		showToastMessage("All states have been loaded");
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
}


function loadCountries4States(){
	url =contextPath+"countries/list";
	
	$.get(url, function(responseJSON){
		dropDownCountry4States.empty();
		
		$.each(responseJSON,function(index,country){
			optionValue = country.id + "-" +country.code;
			$("<option>").val(optionValue).text(country.name).appendTo(dropDownCountry4States);
		});
	}).done(function(){
		buttonLoad.val("Refresh Country List");
		showToastMessage("All countries have been loaded");
	}).fail(function(){
		showToastMessage("ERROR: Could not connect to server or server encountered an error")
	});
}

function showToastMessage(message){
	$("#toastMessage").text(message);
	$(".toast").toast('show');
}

