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
	table.column(3).visible(false);
	table.on( 'row-reorder', function ( e, diff, edit ) {
		var result = 'Reorder started on row: '+edit.triggerRow.data()[1]+'<br>';
		

        for ( var i=0, ien=diff.length ; i<ien ; i++ ) {
            var rowData = table.row( diff[i].node ).data();
            result += rowData[3]+' updated to be in position '+
                diff[i].newData+' (was '+diff[i].oldData+')<br>';
            updatePlanning(rowData[3], diff[i].newData)
        }
    } );
}

function updatePlanning(id, date) {
	console.log("id: " + id + "date: " + date)
	$.ajax({
		url: "/planning/" + id + "/" + date + "/update",
		method: "POST",
		error: function() {
			console.log("Error in test!!!")
		},
		success: function(data, textstatus, request) {
		}
	
	})
}
