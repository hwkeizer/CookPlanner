let baseUrl = 'http://192.168.2.14:8080/recipe/';

$(document).ready( function () {
	showRecipeTable();
	
	$("#selectNewImage").on("click", function() {
        $("input").trigger("click");
    });
});

function ConfirmDelete(recipeName, recipeId) {
	if (confirm("Weet u zeker dat u het recept " + recipeName + " wilt verwijderen?")) {
		location.href = "/recipe/" + recipeId + "/delete";
	  } else {

	  }
}

function showRecipeTable() {
	var table = $('#recipeList').DataTable({
		"aoColumns": [
			  { "bSearchable": false }, // Id
		      { "bSearchable": false }, // Image
		      null, // Name
		      null, // Type
		      null, // Tags
		      null, // Rating
		      null, // LastServed
		      null, // TimesServed
		      { "bSearchable": false } // Plan
		],
		"columnDefs": [
			{ "width": "8%", "targets": 3 }
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
		},
		"order": [[ 2, "asc" ]]


	});
	table.column(0).visible(false)
	$('#recipeList tbody').on('click', 'tr', function(){
		var data = table.row(this).data();
		location.href = "/recipe/" + data[0] + "/show";
	});
}
