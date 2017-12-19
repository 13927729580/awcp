/*
 * Author: Abdullah A Almsaeed
 * Date: 4 Jan 2014
 * Description:
 *      This is a demo file used only for the main dashboard (index.html)
 **/

$(function () {

  'use strict';

  // Make the dashboard widgets sortable Using jquery UI
  $('.connectedSortable').sortable({
    placeholder         : 'sort-highlight',
    connectWith         : '.connectedSortable',
    handle              : '.box-header, .nav-tabs',
    forcePlaceholderSize: true,
    zIndex              : 999999
  });
  $('.connectedSortable .box-header, .connectedSortable .nav-tabs-custom').css('cursor', 'move');

  // jQuery UI sortable for the todo list
  $('.todo-list').sortable({
    placeholder         : 'sort-highlight',
    handle              : '.handle',
    forcePlaceholderSize: true,
    zIndex              : 999999
  });

  // bootstrap WYSIHTML5 - text editor
  $('.textarea').wysihtml5();

  $('.daterange').daterangepicker({
    ranges   : {
      'Today'       : [moment(), moment()],
      'Yesterday'   : [moment().subtract(1, 'days'), moment().subtract(1, 'days')],
      'Last 7 Days' : [moment().subtract(6, 'days'), moment()],
      'Last 30 Days': [moment().subtract(29, 'days'), moment()],
      'This Month'  : [moment().startOf('month'), moment().endOf('month')],
      'Last Month'  : [moment().subtract(1, 'month').startOf('month'), moment().subtract(1, 'month').endOf('month')]
    },
    startDate: moment().subtract(29, 'days'),
    endDate  : moment()
  }, function (start, end) {
    window.alert('You chose: ' + start.format('MMMM D, YYYY') + ' - ' + end.format('MMMM D, YYYY'));
  });

  /* jQueryKnob */
  $('.knob').knob();

  // The Calender
  $('#calendar').datepicker();

  // SLIMSCROLL FOR CHAT WIDGET
  $('#chat-box').slimScroll({
    height: '250px'
  });


  // Fix for charts under tabs
  $('.box ul.nav a').on('shown.bs.tab', function () {
    area.redraw();
    donut.redraw();
    line.redraw();
  });

  /* The todo list plugin */
  $('.todo-list').todoList({
    onCheck  : function () {
      window.console.log($(this), 'The element has been checked');
    },
    onUnCheck: function () {
      window.console.log($(this), 'The element has been unchecked');
    }
  });
  // Sales chart

  var line = new Morris.Line({
	    element: 'line-chart',
	    resize: true,
	    data: [
	      {y: '2011 Q1', item1: 2666},
	      {y: '2011 Q2', item1: 2778},
	      {y: '2011 Q3', item1: 4912},
	      {y: '2011 Q4', item1: 3767},
	      {y: '2012 Q1', item1: 6810},
	      {y: '2012 Q2', item1: 5670},
	      {y: '2012 Q3', item1: 4820},
	      {y: '2012 Q4', item1: 15073},
	      {y: '2013 Q1', item1: 10687},
	      {y: '2013 Q2', item1: 8432}
	    ],
	    xkey: 'y',
	    ykeys: ['item1'],
	    labels: ['Item 1'],
	    lineColors: ['#efefef'],
	    lineWidth: 2,
	    hideHover: 'auto',
	    gridTextColor: "#fff",
	    gridStrokeWidth: 0.4,
	    pointSize: 4,
	    pointStrokeColors: ["#efefef"],
	    gridLineColor: "#efefef",
	    gridTextFamily: "Open Sans",
	    gridTextSize: 10
	  });



});
