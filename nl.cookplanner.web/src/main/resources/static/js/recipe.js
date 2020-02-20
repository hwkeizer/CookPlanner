let baseUrl = 'http://192.168.2.14:8080/recipe/';

$(document).ready( function () {
	showRecipeTable();
});

function showRecipeTable() {
	var table = $('#recipeList').DataTable({
		"columnDefs": [
			{ "width": "10%", "targets": 4 }
		],
		"language": {
			"lengthMenu": "Toon _MENU_ recepten per pagina",
			"zeroRecords": "Geen recepten gevonden",
			"info": "Pagina _PAGE_ uit _PAGES_",
			"infoEmpty": "Geen recepten gevonden",
			"infoFiltered": "gefilterd uit _MAX_ recepten totaal",
			"emptyTable": "Geen recepten beschikbaar",
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
	$('#recipeList tbody').on('click', 'tr', function(){
		var data = table.row(this).data();
		location.href = "/recipe/" + data[0] + "/show";
	});
}