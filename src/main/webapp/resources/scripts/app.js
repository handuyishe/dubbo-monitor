$(function() {
    $('input[name="table_search"]').on('keyup', function() {
        var key = $(this).val();
        $('table tbody tr').each(function() {
            if (key != '' && $('td:eq(0)', this).text().indexOf(key) < 0) {
                $(this).hide();
            } else {
                $(this).show();
            }
        });
    });
});