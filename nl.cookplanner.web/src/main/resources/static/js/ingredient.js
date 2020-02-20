let baseUrl = 'http://192.168.2.14:8080/ingredient/';

$(document).ready( function () {
	showIngredientNameTable();
});

function showIngredientNameTable() {
	var table = $('#ingredientList').DataTable({
		"ordering": false,
		"columnDefs": [
			{ "width": "10%", "targets": 1 },
			{ "width": "10%", "targets": 4 },
			{ "width": "10%", "targets": 5 },
			{ "width": "10%", "targets": 6 }
		],
		"language": {
			"lengthMenu": "Toon _MENU_ ingrediënten per pagina",
			"zeroRecords": "Geen ingrediënten gevonden",
			"info": "Pagina _PAGE_ uit _PAGES_",
			"infoEmpty": "Geen ingrediënten gevonden",
			"infoFiltered": "gefilterd uit _MAX_ ingrediënten totaal",
			"emptyTable": "Geen ingrediënten beschikbaar",
			"search": "Filter",
			"paginate": {
				"first": "Eerste",
				"last": "Laatste",
				"next": "Volgende",
				"previous": "Vorige"
			}			
		}


	});
	table.column(0).visible(false)
	
}

