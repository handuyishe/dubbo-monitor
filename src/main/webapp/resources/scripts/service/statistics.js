/**
 * Created by Silence<me@chenzhiguo.cn> on 15/7/6.
 */
$(function () {
    var dateFormat = 'YYYY-MM-DD';
    var rangePicker = $('#searchDateRange');
    var rangeSpan = rangePicker.find('span');
    var dateFrom = $('#invokeDateFrom');
    var dateTo = $('#invokeDateTo');
    //Date range picker
    rangePicker.daterangepicker({
        ranges: {
            'Today': [moment(), moment()],
            'Yesterday': [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
            'Last 7 Days': [moment().subtract(6, 'days'), moment()],
            'Last 30 Days': [moment().subtract(29, 'days'), moment()],
            'This Month': [moment().startOf('month'), moment().endOf('month')],
            'Last Month': [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
        }, format: dateFormat
    }).on('apply.daterangepicker', function (ev, picker) {
        dateFrom.val(new Date(picker.startDate.format(dateFormat).replace(new RegExp("-","gm"),"/") + ' 00:00:00'));
        dateTo.val(new Date(picker.endDate.format(dateFormat).replace(new RegExp("-","gm"),"/") + ' 23:59:59'));
        //rangeSpan.text(dateFrom.val() + ' ~ ' + dateTo.val());
        $("#statisticsSearchForm").submit();
    });
    dateFrom.val(new Date(moment().format(dateFormat).replace(new RegExp("-","gm"),"/") + ' 00:00:00'));
    dateTo.val(new Date(moment().format(dateFormat).replace(new RegExp("-","gm"),"/") + ' 23:59:59'));
    //rangeSpan.text(dateFrom.val() + ' ~ ' + dateTo.val());
    rangeSpan.text("Choose Count Period");
});
