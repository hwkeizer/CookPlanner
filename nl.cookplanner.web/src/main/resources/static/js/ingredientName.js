let baseUrl = 'http://192.168.2.14:8080/ingredient-name/';

$(document).ready( function () {
	showIngredientNameTable();
});

function showIngredientNameTable() {
	var table = $('#ingredientNameList').DataTable({
		"aoColumns": [
            null,
            null,
            null,
            { "orderSequence": [  ] },
        ],
		"columnDefs": [
			{ "width": "10%", "targets": 3 }
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
	$('#ingredientNameList tbody').on('click', 'tr', function(){
		var data = table.row(this).data();
		location.href = "/ingredient-name/" + data[0] + "/update";
	});
}

