/**
 * Created by Silence on 15/6/30.
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
        dateFrom.val(picker.startDate.format(dateFormat));
        dateTo.val(picker.endDate.format(dateFormat));
        rangeSpan.text(dateFrom.val() + ' ~ ' + dateTo.val());
        loadChartsData();
    });
    dateFrom.val(moment().format(dateFormat));
    dateTo.val(moment().format(dateFormat));
    rangeSpan.text(dateFrom.val() + ' ~ ' + dateTo.val());
    loadChartsData();
});

function loadChartsData() {
    $.ajax({
        type: "POST", url: "services/charts/loadChartsData", dataType: "json", data: {
            "service": $('#invokeSearchService').val(),
            "invokeDateFrom": new Date($('#invokeDateFrom').val().replace(new RegExp("-","gm"),"/") + ' 00:00:00'),
            "invokeDateTo": new Date($('#invokeDateTo').val().replace(new RegExp("-","gm"),"/") + ' 23:59:59')
        }, error: function (req, status, err) {
            alert('Failed reason: ' + err);
        }, success: function (data) {
            if (data.success) {
                drawCharts(data);
            } else {
                alert('Failed reason: ' + data.message);
            }
        }
    });
}

function drawCharts(data) {
    for (x in data.data) {
        drawChart(data.data[x]);
    }
}

function drawChart(data) {
    $('#' + data.chartType + '-' + data.method).highcharts({
        chart: {
            zoomType: 'x'
        }, title: {
            text: data.title, x: -20 //center
        }, subtitle: {
            text: data.subtitle, x: -20
        }, xAxis: {
            type: 'datetime', dateTimeLabelFormats: {
                hour: '%H:%M'
            }
        }, yAxis: {
            min: 0, title: {
                text: data.yAxisTitle
            }, plotLines: [{
                value: 0, width: 1, color: '#808080'
            }]
        }, tooltip: {
            xDateFormat: '%Y-%m-%d, %A', valueSuffix: data.yAxisTitle
        }, plotOptions: {
            series: {
                //pointStart: Date.UTC(2015, 6, 5),
                pointInterval: 60 * 1000 // one day
            }
        }, series: data.seriesData
    });
}