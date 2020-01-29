let baseUrl = 'http://192.168.2.14:8080/measure-unit/';

$(document).ready( function () {
	showMeasureUnitTable();
});

function showMeasureUnitTable() {
	var table = $('#measureUnitList').DataTable({
		"aoColumns": [
            null,
            null,
            null
        ],
		"language": {
			"lengthMenu": "Toon _MENU_ maateenheden per pagina",
			"zeroRecords": "Geen maateenheden gevonden",
			"info": "Pagina _PAGE_ uit _PAGES_",
			"infoEmpty": "Geen maateenheden gevonden",
			"infoFiltered": "gefilterd uit _MAX_ maateenheden totaal",
			"emptyTable": "Geen maateenheden beschikbaar",
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
	$('#measureUnitList tbody').on('click', 'tr', function(){
		var data = table.row(this).data();
		location.href = "/measure-unit/" + data[0] + "/update";
	});
}
