$(document).ready( function () {
	showTable();
});

function showTable() {
	var table = $('#planninglist').DataTable({
		"paging":   false,
        "info":     false,
        rowReorder: {
            
        }
	});
	table.column(3).visible(true);
	table.on( 'row-reorder', function ( e, diff, edit ) {
		let result = "[";
        for ( var i=0, ien=diff.length ; i<ien ; i++ ) {
            var rowData = table.row( diff[i].node ).data();
            result += "{\"id\" : " + rowData[3] + ', \"date\" : \"'+diff[i].newData+'\"},';
        }
        result = result.replace(/.$/,"]")
        updatePlanning(result);
    } );
	$('#planninglist tbody').on('click', 'tr', function(){
		var data = table.row(this).data();
		location.href = "/planning/" + data[3] + "/update";
	});
}

function updatePlanning(result) {
	$.ajax({
		url: "/planning/newdates",
		data: result,
		contentType: "application/json; charset=utf-8",
	    dataType: "json",
		method: "POST",
	})
}
