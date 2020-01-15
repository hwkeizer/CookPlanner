$(document).ready(function () {

    var removeActive = function() {
        $( "nav a" ).parents( "li, ul" ).removeClass("active");
    };

    $( "nav li" ).click(function() {
        removeActive();
        $(this).addClass( "active" );
    });

    removeActive();
    $( "a[href='" + location.hash + "']" ).parent( "li" ).addClass( "active" );

});