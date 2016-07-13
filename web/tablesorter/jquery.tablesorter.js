/*
 * 
 * TableSorter 2.0 - Client-side table sorting with ease!
 * Version 2.0.5b
 * @requires jQuery v1.2.3
 * 
 * Copyright (c) 2007 Christian Bach
 * Examples and docs at: http://tablesorter.com
 * Dual licensed under the MIT and GPL licenses:
 * http://www.opensource.org/licenses/mit-license.php
 * http://www.gnu.org/licenses/gpl.html
 * 
 */
/**
 * 
 * @description Create a sortable table with multi-column sorting capabilitys
 * 
 * @example $('table').tablesorter();
 * @desc Create a simple tablesorter interface.
 * 
 * @example $('table').tablesorter({ sortList:[[0,0],[1,0]] });
 * @desc Create a tablesorter interface and sort on the first and secound column column headers.
 * 
 * @example $('table').tablesorter({ headers: { 0: { sorter: false}, 1: {sorter: false} } });
 *          
 * @desc Create a tablesorter interface and disableing the first and second  column headers.
 *      
 * 
 * @example $('table').tablesorter({ headers: { 0: {sorter:"integer"}, 1: {sorter:"currency"} } });
 * 
 * @desc Create a tablesorter interface and set a column parser for the first
 *       and second column.
 * 
 * 
 * @param Object
 *            settings An object literal containing key/value pairs to provide
 *            optional settings.
 * 
 * 
 * @option String cssHeader (optional) A string of the class name to be appended
 *         to sortable tr elements in the thead of the table. Default value:
 *         "header"
 * 
 * @option String cssAsc (optional) A string of the class name to be appended to
 *         sortable tr elements in the thead on a ascending sort. Default value:
 *         "headerSortUp"
 * 
 * @option String cssDesc (optional) A string of the class name to be appended
 *         to sortable tr elements in the thead on a descending sort. Default
 *         value: "headerSortDown"
 * 
 * @option String sortInitialOrder (optional) A string of the inital sorting
 *         order can be asc or desc. Default value: "asc"
 * 
 * @option String sortMultisortKey (optional) A string of the multi-column sort
 *         key. Default value: "shiftKey"
 * 
 * @option String textExtraction (optional) A string of the text-extraction
 *         method to use. For complex html structures inside td cell set this
 *         option to "complex", on large tables the complex option can be slow.
 *         Default value: "simple"
 * 
 * @option Object headers (optional) An array containing the forces sorting
 *         rules. This option let's you specify a default sorting rule. Default
 *         value: null
 * 
 * @option Array sortList (optional) An array containing the forces sorting
 *         rules. This option let's you specify a default sorting rule. Default
 *         value: null
 * 
 * @option Array sortForce (optional) An array containing forced sorting rules.
 *         This option let's you specify a default sorting rule, which is
 *         prepended to user-selected rules. Default value: null
 * 
 * @option Boolean sortLocaleCompare (optional) Boolean flag indicating whatever
 *         to use String.localeCampare method or not. Default set to true.
 * 
 * 
 * @option Array sortAppend (optional) An array containing forced sorting rules.
 *         This option let's you specify a default sorting rule, which is
 *         appended to user-selected rules. Default value: null
 * 
 * @option Boolean widthFixed (optional) Boolean flag indicating if tablesorter
 *         should apply fixed widths to the table columns. This is usefull when
 *         using the pager companion plugin. This options requires the dimension
 *         jquery plugin. Default value: false
 * 
 * @option Boolean cancelSelection (optional) Boolean flag indicating if
 *         tablesorter should cancel selection of the table headers text.
 *         Default value: true
 * 
 * @option Boolean debug (optional) Boolean flag indicating if tablesorter
 *         should display debuging information usefull for development.
 * 
 * @type jQuery
 * 
 * @name tablesorter
 * 
 * @cat Plugins/Tablesorter
 * 
 * @author Christian Bach/christian.bach@polyester.se
 */

(function ($) {

    $.extend({
        tablesorter: new
                function () {

                    var parsers = [],
                            widgets = [];

                    this.defaults = {
                        cssHeader: "header",
                        cssAsc: "headerSortUp",
                        cssDesc: "headerSortDown",
                        cssChildRow: "expand-child",
                        sortInitialOrder: "asc",
                        sortMultiSortKey: "shiftKey",
                        sortForce: null,
                        sortAppend: null,
                        sortLocaleCompare: true,
                        textExtraction: "simple",
                        parsers: {}, widgets: [],
                        widgetZebra: {
                            css: ["even", "odd"]
                        }, headers: {}, widthFixed: false,
                        cancelSelection: true,
                        sortList: [],
                        headerList: [],
                        dateFormat: "dd.mm.yyyy",
                        decimal: '/\.|\,/g',
                        onRenderHeader: null,
                        selectorHeaders: 'thead th',
                        debug: false
                    };

                    /* debuging utils */

                    function benchmark(s, d) {
                        log(s + "," + (new Date().getTime() - d.getTime()) + "ms");
                    }

                    this.benchmark = benchmark;

                    function log(s) {
                        if (typeof console != "undefined" && typeof console.debug != "undefined") {
                            console.log(s);
                        } else {
                            alert(s);
                        }
                    }

                    /* parsers utils */

                    function buildParserCache(table, $headers) {

                        if (table.config.debug) {
                            var parsersDebug = "";
                        }

                        if (table.tBodies.length == 0)
                            return; // In the case of empty tables
                        var rows = table.tBodies[0].rows;

                        if (rows[0]) {

                            var list = [],
                                    cells = rows[0].cells,
                                    l = cells.length;

                            for (var i = 0; i < l; i++) {

                                var p = false;

                                if ($.metadata && ($($headers[i]).metadata() && $($headers[i]).metadata().sorter)) {

                                    p = getParserById($($headers[i]).metadata().sorter);

                                } else if ((table.config.headers[i] && table.config.headers[i].sorter)) {

                                    p = getParserById(table.config.headers[i].sorter);
                                }
                                if (!p) {

                                    p = detectParserForColumn(table, rows, -1, i);
                                }

                                if (table.config.debug) {
                                    parsersDebug += "column:" + i + " parser:" + p.id + "\n";
                                }

                                list.push(p);
                            }
                        }

                        if (table.config.debug) {
                            log(parsersDebug);
                        }

                        return list;
                    }
                    ;

                    function detectParserForColumn(table, rows, rowIndex, cellIndex) {
                        var l = parsers.length,
                                node = false,
                                nodeValue = false,
                                keepLooking = true;
                        while (nodeValue == '' && keepLooking) {
                            rowIndex++;
                            if (rows[rowIndex]) {
                                node = getNodeFromRowAndCellIndex(rows, rowIndex, cellIndex);
                                nodeValue = trimAndGetNodeText(table.config, node);
                                if (table.config.debug) {
                                    log('Checking if value was empty on row:' + rowIndex);
                                }
                            } else {
                                keepLooking = false;
                            }
                        }
                        for (var i = 1; i < l; i++) {
                            if (parsers[i].is(nodeValue, table, node)) {
                                return parsers[i];
                            }
                        }
                        // 0 is always the generic parser (text)
                        return parsers[0];
                    }

                    function getNodeFromRowAndCellIndex(rows, rowIndex, cellIndex) {
                        return rows[rowIndex].cells[cellIndex];
                    }

                    function trimAndGetNodeText(config, node) {
                        return $.trim(getElementText(config, node));
                    }

                    function getParserById(name) {
                        var l = parsers.length;
                        for (var i = 0; i < l; i++) {
                            if (parsers[i].id.toLowerCase() == name.toLowerCase()) {
                                return parsers[i];
                            }
                        }
                        return false;
                    }

                    /* utils */

                    function buildCache(table) {

                        if (table.config.debug) {
                            var cacheTime = new Date();
                        }

                        var totalRows = (table.tBodies[0] && table.tBodies[0].rows.length) || 0,
                                totalCells = (table.tBodies[0].rows[0] && table.tBodies[0].rows[0].cells.length) || 0,
                                parsers = table.config.parsers,
                                cache = {
                                    row: [],
                                    normalized: []
                                };

                        for (var i = 0; i < totalRows; ++i) {

                            /** Add the table data to main data array */
                            var c = $(table.tBodies[0].rows[i]),
                                    cols = [];

                            // if this is a child row, add it to the last row's children and
                            // continue to the next row
                            if (c.hasClass(table.config.cssChildRow)) {
                                cache.row[cache.row.length - 1] = cache.row[cache.row.length - 1].add(c);
                                // go to the next for loop
                                continue;
                            }

                            cache.row.push(c);

                            for (var j = 0; j < totalCells; ++j) {
                                cols.push(parsers[j].format(getElementText(table.config, c[0].cells[j]), table, c[0].cells[j]));
                            }

                            cols.push(cache.normalized.length); // add position for rowCache
                            cache.normalized.push(cols);
                            cols = null;
                        }
                        ;

                        if (table.config.debug) {
                            benchmark("Building cache for " + totalRows + " rows:", cacheTime);
                        }

                        return cache;
                    }
                    ;

                    function getElementText(config, node) {

                        var text = "";

                        if (!node)
                            return "";

                        if (!config.supportsTextContent)
                            config.supportsTextContent = node.textContent || false;

                        if (config.textExtraction == "simple") {
                            if (config.supportsTextContent) {
                                text = node.textContent;
                            } else {
                                if (node.childNodes[0] && node.childNodes[0].hasChildNodes()) {
                                    text = node.childNodes[0].innerHTML;
                                } else {
                                    text = node.innerHTML;
                                }
                            }
                        } else {
                            if (typeof (config.textExtraction) == "function") {
                                text = config.textExtraction(node);
                            } else {
                                text = $(node).text();
                            }
                        }
                        return text;
                    }

                    function appendToTable(table, cache) {

                        if (table.config.debug) {
                            var appendTime = new Date()
                        }

                        var c = cache,
                                r = c.row,
                                n = c.normalized,
                                totalRows = n.length,
                                checkCell = (n[0].length - 1),
                                tableBody = $(table.tBodies[0]),
                                rows = [];


                        for (var i = 0; i < totalRows; i++) {
                            var pos = n[i][checkCell];

                            rows.push(r[pos]);

                            if (!table.config.appender) {

                                //var o = ;
                                var l = r[pos].length;
                                for (var j = 0; j < l; j++) {
                                    tableBody[0].appendChild(r[pos][j]);
                                }

                                // 
                            }
                        }



                        if (table.config.appender) {

                            table.config.appender(table, rows);
                        }

                        rows = null;

                        if (table.config.debug) {
                            benchmark("Rebuilt table:", appendTime);
                        }

                        // apply table widgets
                        applyWidget(table);

                        // trigger sortend
                        setTimeout(function () {
                            $(table).trigger("sortEnd");
                        }, 0);

                    }
                    ;

                    function buildHeaders(table) {

                        if (table.config.debug) {
                            var time = new Date();
                        }

                        var meta = ($.metadata) ? true : false;

                        var header_index = computeTableHeaderCellIndexes(table);

                        $tableHeaders = $(table.config.selectorHeaders, table).each(function (index) {

                            this.column = header_index[this.parentNode.rowIndex + "-" + this.cellIndex];
                            // this.column = index;
                            this.order = formatSortingOrder(table.config.sortInitialOrder);


                            this.count = this.order;

                            if (checkHeaderMetadata(this) || checkHeaderOptions(table, index))
                                this.sortDisabled = true;
                            if (checkHeaderOptionsSortingLocked(table, index))
                                this.order = this.lockedOrder = checkHeaderOptionsSortingLocked(table, index);

                            if (!this.sortDisabled) {
                                var $th = $(this).addClass(table.config.cssHeader);
                                if (table.config.onRenderHeader)
                                    table.config.onRenderHeader.apply($th);
                            }

                            // add cell to headerList
                            table.config.headerList[index] = this;
                        });

                        if (table.config.debug) {
                            benchmark("Built headers:", time);
                            log($tableHeaders);
                        }

                        return $tableHeaders;

                    }
                    ;

                    // from:
                    // http://www.javascripttoolbox.com/lib/table/examples.php
                    // http://www.javascripttoolbox.com/temp/table_cellindex.html


                    function computeTableHeaderCellIndexes(t) {
                        var matrix = [];
                        var lookup = {};
                        var thead = t.getElementsByTagName('THEAD')[0];
                        var trs = thead.getElementsByTagName('TR');

                        for (var i = 0; i < trs.length; i++) {
                            var cells = trs[i].cells;
                            for (var j = 0; j < cells.length; j++) {
                                var c = cells[j];

                                var rowIndex = c.parentNode.rowIndex;
                                var cellId = rowIndex + "-" + c.cellIndex;
                                var rowSpan = c.rowSpan || 1;
                                var colSpan = c.colSpan || 1
                                var firstAvailCol;
                                if (typeof (matrix[rowIndex]) == "undefined") {
                                    matrix[rowIndex] = [];
                                }
                                // Find first available column in the first row
                                for (var k = 0; k < matrix[rowIndex].length + 1; k++) {
                                    if (typeof (matrix[rowIndex][k]) == "undefined") {
                                        firstAvailCol = k;
                                        break;
                                    }
                                }
                                lookup[cellId] = firstAvailCol;
                                for (var k = rowIndex; k < rowIndex + rowSpan; k++) {
                                    if (typeof (matrix[k]) == "undefined") {
                                        matrix[k] = [];
                                    }
                                    var matrixrow = matrix[k];
                                    for (var l = firstAvailCol; l < firstAvailCol + colSpan; l++) {
                                        matrixrow[l] = "x";
                                    }
                                }
                            }
                        }
                        return lookup;
                    }

                    function checkCellColSpan(table, rows, row) {
                        var arr = [],
                                r = table.tHead.rows,
                                c = r[row].cells;

                        for (var i = 0; i < c.length; i++) {
                            var cell = c[i];

                            if (cell.colSpan > 1) {
                                arr = arr.concat(checkCellColSpan(table, headerArr, row++));
                            } else {
                                if (table.tHead.length == 1 || (cell.rowSpan > 1 || !r[row + 1])) {
                                    arr.push(cell);
                                }
                                // headerArr[row] = (i+row);
                            }
                        }
                        return arr;
                    }
                    ;

                    function checkHeaderMetadata(cell) {
                        if (($.metadata) && ($(cell).metadata().sorter === false)) {
                            return true;
                        }
                        ;
                        return false;
                    }

                    function checkHeaderOptions(table, i) {
                        if ((table.config.headers[i]) && (table.config.headers[i].sorter === false)) {
                            return true;
                        }
                        ;
                        return false;
                    }

                    function checkHeaderOptionsSortingLocked(table, i) {
                        if ((table.config.headers[i]) && (table.config.headers[i].lockedOrder))
                            return table.config.headers[i].lockedOrder;
                        return false;
                    }

                    function applyWidget(table) {
                        var c = table.config.widgets;
                        var l = c.length;
                        for (var i = 0; i < l; i++) {

                            getWidgetById(c[i]).format(table);
                        }

                    }

                    function getWidgetById(name) {
                        var l = widgets.length;
                        for (var i = 0; i < l; i++) {
                            if (widgets[i].id.toLowerCase() == name.toLowerCase()) {
                                return widgets[i];
                            }
                        }
                    }
                    ;

                    function formatSortingOrder(v) {
                        if (typeof (v) != "Number") {
                            return (v.toLowerCase() == "desc") ? 1 : 0;
                        } else {
                            return (v == 1) ? 1 : 0;
                        }
                    }

                    function isValueInArray(v, a) {
                        var l = a.length;
                        for (var i = 0; i < l; i++) {
                            if (a[i][0] == v) {
                                return true;
                            }
                        }
                        return false;
                    }

                    function setHeadersCss(table, $headers, list, css) {
                        // remove all header information
                        $headers.removeClass(css[0]).removeClass(css[1]);

                        var h = [];
                        $headers.each(function (offset) {
                            if (!this.sortDisabled) {
                                h[this.column] = $(this);
                            }
                        });

                        var l = list.length;
                        for (var i = 0; i < l; i++) {
                            h[list[i][0]].addClass(css[list[i][1]]);
                        }
                    }

                    function fixColumnWidth(table, $headers) {
                        var c = table.config;
                        if (c.widthFixed) {
                            var colgroup = $('<colgroup>');
                            $("tr:first td", table.tBodies[0]).each(function () {
                                colgroup.append($('<col>').css('width', $(this).width()));
                            });
                            $(table).prepend(colgroup);
                        }
                        ;
                    }

                    function updateHeaderSortCount(table, sortList) {
                        var c = table.config,
                                l = sortList.length;
                        for (var i = 0; i < l; i++) {
                            var s = sortList[i],
                                    o = c.headerList[s[0]];
                            o.count = s[1];
                            o.count++;
                        }
                    }

                    /* sorting methods */

                    function multisort(table, sortList, cache) {

                        if (table.config.debug) {
                            var sortTime = new Date();
                        }

                        var dynamicExp = "var sortWrapper = function(a,b) {",
                                l = sortList.length;

                        // TODO: inline functions.
                        for (var i = 0; i < l; i++) {

                            var c = sortList[i][0];
                            var order = sortList[i][1];
                            // var s = (getCachedSortType(table.config.parsers,c) == "text") ?
                            // ((order == 0) ? "sortText" : "sortTextDesc") : ((order == 0) ?
                            // "sortNumeric" : "sortNumericDesc");
                            // var s = (table.config.parsers[c].type == "text") ? ((order == 0)
                            // ? makeSortText(c) : makeSortTextDesc(c)) : ((order == 0) ?
                            // makeSortNumeric(c) : makeSortNumericDesc(c));
                            var s = (table.config.parsers[c].type == "text") ? ((order == 0) ? makeSortFunction("text", "asc", c) : makeSortFunction("text", "desc", c)) : ((order == 0) ? makeSortFunction("numeric", "asc", c) : makeSortFunction("numeric", "desc", c));
                            var e = "e" + i;

                            dynamicExp += "var " + e + " = " + s; // + "(a[" + c + "],b[" + c
                            // + "]); ";
                            dynamicExp += "if(" + e + ") { return " + e + "; } ";
                            dynamicExp += "else { ";

                        }

                        // if value is the same keep orignal order
                        var orgOrderCol = cache.normalized[0].length - 1;
                        dynamicExp += "return a[" + orgOrderCol + "]-b[" + orgOrderCol + "];";

                        for (var i = 0; i < l; i++) {
                            dynamicExp += "}; ";
                        }

                        dynamicExp += "return 0; ";
                        dynamicExp += "}; ";

                        if (table.config.debug) {
                            benchmark("Evaling expression:" + dynamicExp, new Date());
                        }

                        eval(dynamicExp);

                        cache.normalized.sort(sortWrapper);

                        if (table.config.debug) {
                            benchmark("Sorting on " + sortList.toString() + " and dir " + order + " time:", sortTime);
                        }

                        return cache;
                    }
                    ;

                    function makeSortFunction(type, direction, index) {
                        var a = "a[" + index + "]",
                                b = "b[" + index + "]";
                        if (type == 'text' && direction == 'asc') {
                            return "(" + a + " == " + b + " ? 0 : (" + a + " === null ? Number.POSITIVE_INFINITY : (" + b + " === null ? Number.NEGATIVE_INFINITY : (" + a + " < " + b + ") ? -1 : 1 )));";
                        } else if (type == 'text' && direction == 'desc') {
                            return "(" + a + " == " + b + " ? 0 : (" + a + " === null ? Number.POSITIVE_INFINITY : (" + b + " === null ? Number.NEGATIVE_INFINITY : (" + b + " < " + a + ") ? -1 : 1 )));";
                        } else if (type == 'numeric' && direction == 'asc') {
                            return "(" + a + " === null && " + b + " === null) ? 0 :(" + a + " === null ? Number.POSITIVE_INFINITY : (" + b + " === null ? Number.NEGATIVE_INFINITY : " + a + " - " + b + "));";
                        } else if (type == 'numeric' && direction == 'desc') {
                            return "(" + a + " === null && " + b + " === null) ? 0 :(" + a + " === null ? Number.POSITIVE_INFINITY : (" + b + " === null ? Number.NEGATIVE_INFINITY : " + b + " - " + a + "));";
                        }
                    }
                    ;

                    function makeSortText(i) {
                        return "((a[" + i + "] < b[" + i + "]) ? -1 : ((a[" + i + "] > b[" + i + "]) ? 1 : 0));";
                    }
                    ;

                    function makeSortTextDesc(i) {
                        return "((b[" + i + "] < a[" + i + "]) ? -1 : ((b[" + i + "] > a[" + i + "]) ? 1 : 0));";
                    }
                    ;

                    function makeSortNumeric(i) {
                        return "a[" + i + "]-b[" + i + "];";
                    }
                    ;

                    function makeSortNumericDesc(i) {
                        return "b[" + i + "]-a[" + i + "];";
                    }
                    ;

                    function sortText(a, b) {
                        if (table.config.sortLocaleCompare)
                            return a.localeCompare(b);
                        return ((a < b) ? -1 : ((a > b) ? 1 : 0));
                    }
                    ;

                    function sortTextDesc(a, b) {
                        if (table.config.sortLocaleCompare)
                            return b.localeCompare(a);
                        return ((b < a) ? -1 : ((b > a) ? 1 : 0));
                    }
                    ;

                    function sortNumeric(a, b) {
                        return a - b;
                    }
                    ;

                    function sortNumericDesc(a, b) {
                        return b - a;
                    }
                    ;

                    function getCachedSortType(parsers, i) {
                        return parsers[i].type;
                    }
                    ; /* public methods */
                    this.construct = function (settings) {
                        return this.each(function () {
                            // if no thead or tbody quit.
                            if (!this.tHead || !this.tBodies)
                                return;
                            // declare
                            var $this, $document, $headers, cache, config, shiftDown = 0,
                                    sortOrder;
                            // new blank config object
                            this.config = {};
                            // merge and extend.
                            config = $.extend(this.config, $.tablesorter.defaults, settings);
                            // store common expression for speed
                            $this = $(this);
                            // save the settings where they read
                            $.data(this, "tablesorter", config);
                            // build headers
                            $headers = buildHeaders(this);
                            // try to auto detect column type, and store in tables config
                            this.config.parsers = buildParserCache(this, $headers);
                            // build the cache for the tbody cells
                            cache = buildCache(this);
                            // get the css class names, could be done else where.
                            var sortCSS = [config.cssDesc, config.cssAsc];
                            // fixate columns if the users supplies the fixedWidth option
                            fixColumnWidth(this);
                            // apply event handling to headers
                            // this is to big, perhaps break it out?
                            $headers.click(
                                    function (e) {
                                        var totalRows = ($this[0].tBodies[0] && $this[0].tBodies[0].rows.length) || 0;
                                        if (!this.sortDisabled && totalRows > 0) {
                                            // Only call sortStart if sorting is
                                            // enabled.
                                            $this.trigger("sortStart");
                                            // store exp, for speed
                                            var $cell = $(this);
                                            // get current column index
                                            var i = this.column;
                                            // get current column sort order
                                            this.order = this.count++ % 2;
                                            // always sort on the locked order.
                                            if (this.lockedOrder)
                                                this.order = this.lockedOrder;

                                            // user only whants to sort on one
                                            // column
                                            if (!e[config.sortMultiSortKey]) {
                                                // flush the sort list
                                                config.sortList = [];
                                                if (config.sortForce != null) {
                                                    var a = config.sortForce;
                                                    for (var j = 0; j < a.length; j++) {
                                                        if (a[j][0] != i) {
                                                            config.sortList.push(a[j]);
                                                        }
                                                    }
                                                }
                                                // add column to sort list
                                                config.sortList.push([i, this.order]);
                                                // multi column sorting
                                            } else {
                                                // the user has clicked on an all
                                                // ready sortet column.
                                                if (isValueInArray(i, config.sortList)) {
                                                    // revers the sorting direction
                                                    // for all tables.
                                                    for (var j = 0; j < config.sortList.length; j++) {
                                                        var s = config.sortList[j],
                                                                o = config.headerList[s[0]];
                                                        if (s[0] == i) {
                                                            o.count = s[1];
                                                            o.count++;
                                                            s[1] = o.count % 2;
                                                        }
                                                    }
                                                } else {
                                                    // add column to sort list array
                                                    config.sortList.push([i, this.order]);
                                                }
                                            }
                                            ;
                                            setTimeout(function () {
                                                // set css for headers
                                                setHeadersCss($this[0], $headers, config.sortList, sortCSS);
                                                appendToTable(
                                                        $this[0], multisort(
                                                        $this[0], config.sortList, cache)
                                                        );
                                            }, 1);
                                            // stop normal event by returning false
                                            return false;
                                        }
                                        // cancel selection
                                    }).mousedown(function () {
                                if (config.cancelSelection) {
                                    this.onselectstart = function () {
                                        return false
                                    };
                                    return false;
                                }
                            });
                            // apply easy methods that trigger binded events
                            $this.bind("update", function () {
                                var me = this;
                                setTimeout(function () {
                                    // rebuild parsers.
                                    me.config.parsers = buildParserCache(
                                            me, $headers);
                                    // rebuild the cache map
                                    cache = buildCache(me);
                                }, 1);
                            }).bind("updateCell", function (e, cell) {
                                var config = this.config;
                                // get position from the dom.
                                var pos = [(cell.parentNode.rowIndex - 1), cell.cellIndex];
                                // update cache
                                cache.normalized[pos[0]][pos[1]] = config.parsers[pos[1]].format(
                                        getElementText(config, cell), cell);
                            }).bind("sorton", function (e, list) {
                                $(this).trigger("sortStart");
                                config.sortList = list;
                                // update and store the sortlist
                                var sortList = config.sortList;
                                // update header count index
                                updateHeaderSortCount(this, sortList);
                                // set css for headers
                                setHeadersCss(this, $headers, sortList, sortCSS);
                                // sort the table and append it to the dom
                                appendToTable(this, multisort(this, sortList, cache));
                            }).bind("appendCache", function () {
                                appendToTable(this, cache);
                            }).bind("applyWidgetId", function (e, id) {
                                getWidgetById(id).format(this);
                            }).bind("applyWidgets", function () {
                                // apply widgets
                                applyWidget(this);
                            });
                            if ($.metadata && ($(this).metadata() && $(this).metadata().sortlist)) {
                                config.sortList = $(this).metadata().sortlist;
                            }
                            // if user has supplied a sort list to constructor.
                            if (config.sortList.length > 0) {
                                $this.trigger("sorton", [config.sortList]);
                            }
                            // apply widgets
                            applyWidget(this);
                        });
                    };
                    this.addParser = function (parser) {
                        var l = parsers.length,
                                a = true;
                        for (var i = 0; i < l; i++) {
                            if (parsers[i].id.toLowerCase() == parser.id.toLowerCase()) {
                                a = false;
                            }
                        }
                        if (a) {
                            parsers.push(parser);
                        }
                        ;
                    };
                    this.addWidget = function (widget) {
                        widgets.push(widget);
                    };
                    this.formatFloat = function (s) {
                        var i = parseFloat(s);
                        return (isNaN(i)) ? 0 : i;
                    };
                    this.formatInt = function (s) {
                        var i = parseInt(s);
                        return (isNaN(i)) ? 0 : i;
                    };
                    this.isDigit = function (s, config) {
                        // replace all an wanted chars and match.
                        return /^[-+]?\d*$/.test($.trim(s.replace(/[,.']/g, '')));
                    };
                    this.clearTableBody = function (table) {
                        if ($.browser.msie) {
                            function empty() {
                                while (this.firstChild)
                                    this.removeChild(this.firstChild);
                            }
                            empty.apply(table.tBodies[0]);
                        } else {
                            table.tBodies[0].innerHTML = "";
                        }
                    };
                }
    });

    // extend plugin scope
    $.fn.extend({
        tablesorter: $.tablesorter.construct
    });

    // make shortcut
    var ts = $.tablesorter;

    // add default parsers
    ts.addParser({
        id: "text",
        is: function (s) {
            return true;
        }, format: function (s) {
            return $.trim(s.toLocaleLowerCase());
        }, type: "text"
    });



    ts.addParser({
        id: "digit",
        is: function (s, table) {
            var c = table.config;
            return $.tablesorter.isDigit(s, c);
        }, format: function (s) {
            return $.tablesorter.formatFloat(s);
        }, type: "numeric"
    });

    ts.addParser({
        id: "currency",
        is: function (s) {
            return /^[£$€?.]/.test(s);
        }, format: function (s) {
            return $.tablesorter.formatFloat(s.replace(new RegExp(/[£$€]/g), ""));
        }, type: "numeric"
    });

    ts.addParser({
        id: "ipAddress",
        is: function (s) {
            return /^\d{2,3}[\.]\d{2,3}[\.]\d{2,3}[\.]\d{2,3}$/.test(s);
        }, format: function (s) {
            var a = s.split("."),
                    r = "",
                    l = a.length;
            for (var i = 0; i < l; i++) {
                var item = a[i];
                if (item.length == 2) {
                    r += "0" + item;
                } else {
                    r += item;
                }
            }
            return $.tablesorter.formatFloat(r);
        }, type: "numeric"
    });

    ts.addParser({
        id: "url",
        is: function (s) {
            return /^(https?|ftp|file):\/\/$/.test(s);
        }, format: function (s) {
            return jQuery.trim(s.replace(new RegExp(/(https?|ftp|file):\/\//), ''));
        }, type: "text"
    });

    ts.addParser({
        id: "isoDate",
        is: function (s) {
            return /^\d{4}[\/-]\d{1,2}[\/-]\d{1,2}$/.test(s);
        }, format: function (s) {
            return $.tablesorter.formatFloat((s != "") ? new Date(s.replace(
                    new RegExp(/-/g), "/")).getTime() : "0");
        }, type: "numeric"
    });

    ts.addParser({
        id: "percent",
        is: function (s) {
            return /\%$/.test($.trim(s));
        }, format: function (s) {
            return $.tablesorter.formatFloat(s.replace(new RegExp(/%/g), ""));
        }, type: "numeric"
    });

    ts.addParser({
        id: "usLongDate",
        is: function (s) {
            return s.match(new RegExp(/^[A-Za-z]{3,10}\.? [0-9]{1,2}, ([0-9]{4}|'?[0-9]{2}) (([0-2]?[0-9]:[0-5][0-9])|([0-1]?[0-9]:[0-5][0-9]\s(AM|PM)))$/));
        }, format: function (s) {
            return $.tablesorter.formatFloat(new Date(s).getTime());
        }, type: "numeric"
    });

    ts.addParser({
        id: "shortDate",
        is: function (s) {
            return /\d{1,2}[\/\-]\d{1,2}[\/\-]\d{2,4}/.test(s);
        }, format: function (s, table) {
            var c = table.config;
            s = s.replace(/\-/g, "/");
            if (c.dateFormat == "us") {
                // reformat the string in ISO format
                s = s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})/, "$3/$1/$2");
            } else if (c.dateFormat == "uk") {
                // reformat the string in ISO format
                s = s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{4})/, "$3/$2/$1");
            } else if (c.dateFormat == "dd/mm/yy" || c.dateFormat == "dd-mm-yy") {
                s = s.replace(/(\d{1,2})[\/\-](\d{1,2})[\/\-](\d{2})/, "$1/$2/$3");
            }
            return $.tablesorter.formatFloat(new Date(s).getTime());
        }, type: "numeric"
    });

    //-------------------------------------------------------------------------------
    function replaceAll(str, find, replace) {
        return str.replace(new RegExp(find, 'g'), replace);
    }

    ts.addParser({
        // set a unique id 
        id: 'levels',
        is: function (s) {
            // return false so this parser is not auto detected 
            return false;
        },
        format: function (s) {
            // format your data for normalization 

            if (s == "")
            {
                return 121;
            }
//                var hi = replaceAll(s,' ','');
//                var hi1 = replaceAll(hi,'.','');
//                var hi2 = replaceAll(hi1,'-','');
            s = " " + s + " ";
            var xy = s.toUpperCase()
                    .replace(/ OBR D.LFV /i, 0)
                    .replace(/ BR D.LFV /i, 1)
                    .replace(/ ABI D.LFV /i, 2)
                    .replace(/ HBI D.LFV /i, 3)
                    .replace(/ OBI D.LFV /i, 4)
                    .replace(/ BI D.LFV /i, 5)
                    .replace(/ HBM D.LFV /i, 6)
                    .replace(/ OBM D.LFV /i, 7)
                    .replace(/ BM D.LFV /i, 8)
                    .replace(/ HLM D.LFV /i, 9)
                    .replace(/ OLM D.LFV /i, 10)
                    .replace(/ LM D.LFV /i, 11)
                    .replace(/ EBFR /i, 12)
                    .replace(/ BFR /i, 13)
                    .replace(/ ELBD /i, 14)
                    .replace(/ ÖBFV-PRÄS. LBD /i, 15)
                    .replace(/ LBD /i, 16)
                    .replace(/ ELBDS /i, 17)
                    .replace(/ LBDS /i, 18)
                    .replace(/ LFVET /i, 19)
                    .replace(/ ELFA /i, 20)
                    .replace(/ LFA /i, 21)
                    .replace(/ LFKUR /i, 22)
                    .replace(/ BD /i, 23)
                    .replace(/ ELFR /i, 24)
                    .replace(/ LFR /i, 25)
                    .replace(/ EHV /i, 26)
                    .replace(/ HV /i, 27)
                    .replace(/ EOBR /i, 28)
                    .replace(/ OBR D.ÖBFV /i, 29)
                    .replace(/ OBR D.F. /i, 30)
                    .replace(/ OBR A.D. /i, 31)
                    .replace(/ OBR /i, 32)
                    .replace(/ EBR /i, 33)
                    .replace(/ BR D.ÖBFV /i, 34)
                    .replace(/ BR D.V. /i, 35)
                    .replace(/ BR D.S. /i, 36)
                    .replace(/ BR D.F. /i, 37)
                    .replace(/ BR A.D. /i, 38)
                    .replace(/ BR /i, 39)
                    .replace(/ BFKUR /i, 40)
                    .replace(/ BFVET /i, 41)
                    .replace(/ BFA A.D. /i, 42)
                    .replace(/ EBFA /i, 43)
                    .replace(/ BFA /i, 44)
                    .replace(/ EABI /i, 45)
                    .replace(/ ABI D.V. /i, 46)
                    .replace(/ ABI D.S. /i, 47)
                    .replace(/ ABI D.F. /i, 48)
                    .replace(/ ABI A.D. /i, 49)
                    .replace(/ ABI /i, 50)
                    .replace(/ EAFA /i, 51)
                    .replace(/ AFA /i, 52)
                    .replace(/ EOV /i, 53)
                    .replace(/ OV /i, 54)
                    .replace(/ EHBI /i, 55)
                    .replace(/ HBI D.V. /i, 56)
                    .replace(/ HBI D.S. /i, 57)
                    .replace(/ HBI D.F. /i, 58)
                    .replace(/ HBI A.D. /i, 59)
                    .replace(/ HBI /i, 60)
                    .replace(/ EOBI /i, 61)
                    .replace(/ OBI D.V. /i, 62)
                    .replace(/ OBI D.S. /i, 63)
                    .replace(/ OBI D.F. /i, 64)
                    .replace(/ OBI A.D. /i, 65)
                    .replace(/ OBI /i, 66)
                    .replace(/ FT-A /i, 67)
                    .replace(/ FT-B /i, 68)
                    .replace(/ FVET /i, 69)
                    .replace(/ EFA /i, 70)
                    .replace(/ FA /i, 71)
                    .replace(/ FKUR /i, 72)
                    .replace(/ EFKUR /i, 73)
                    .replace(/ EBI /i, 74)
                    .replace(/ BI D.V. /i, 75)
                    .replace(/ BI D.S. /i, 76)
                    .replace(/ BI D.F. /i, 77)
                    .replace(/ BI A.D. /i, 78)
                    .replace(/ BI /i, 79)
                    .replace(/ EV /i, 80)
                    .replace(/ V /i, 81)
                    .replace(/ EHBM /i, 82)
                    .replace(/ HBM D.V. /i, 83)
                    .replace(/ HBM D.S. /i, 84)
                    .replace(/ HBM D.F. /i, 85)
                    .replace(/ HBM /i, 86)
                    .replace(/ EOBM /i, 87)
                    .replace(/ OBM D.V. /i, 88)
                    .replace(/ OBM D.S. /i, 89)
                    .replace(/ OBM D.F. /i, 90)
                    .replace(/ OBM /i, 91)
                    .replace(/ EBM /i, 92)
                    .replace(/ BM D.V. /i, 93)
                    .replace(/ BM D.S. /i, 94)
                    .replace(/ BM D.F. /i, 95)
                    .replace(/ BM /i, 96)
                    .replace(/ EHLM /i, 97)
                    .replace(/ HLM D.V. /i, 98)
                    .replace(/ HLM D.S. /i, 99)
                    .replace(/ HLM D.F. /i, 100)
                    .replace(/ HLM /i, 101)
                    .replace(/ EOLM /i, 102)
                    .replace(/ OLM D.V. /i, 103)
                    .replace(/ OLM D.S. /i, 104)
                    .replace(/ OLM D.F. /i, 105)
                    .replace(/ OLM /i, 106)
                    .replace(/ ELM /i, 107)
                    .replace(/ LM D.V. /i, 108)
                    .replace(/ LM D.S. /i, 109)
                    .replace(/ LM D.F. /i, 110)
                    .replace(/ LM /i, 111)
                    .replace(/ EHFM /i, 112)
                    .replace(/ HFM /i, 113)
                    .replace(/ EOFM /i, 114)
                    .replace(/ OFM /i, 115)
                    .replace(/ EFM /i, 116)
                    .replace(/ FM /i, 117)
                    .replace(/ PFM /i, 118)
                    .replace(/ ZD /i, 119)
                    .replace(/ JFM /i, 120)
                    .replace(/ EM /i, 121);

            if (xy.length == 1)
            {
                xy = "00" + xy;
            } else if (xy.length == 2)
            {
                xy = "0" + xy;
            }
            return xy;
        },
        // set type, either numeric or text 

        type: 'text'
    });


    ts.addParser({
        id: 'germandate',
        is: function (s) {
            return false;
        },
        format: function (s) {
            var a = s.split('.');
            var x = a[2] + a[1] + a[0];

            return x;

        },
        type: 'text'
    });

    ts.addParser({
        id: 'berichtdate',
        is: function (s) {
            return false;
        },
        format: function (s) {
            var datum2teile = s.split(' ');
            if (datum2teile.length == 2)
            {
                var datumteil1 = datum2teile[0].split('.');
                var datumteil2 = datum2teile[1].split(':');
                return datumteil1[2] + datumteil1[1] + datumteil1[0] + datumteil2[0] + datumteil2[1];
            } else
            {
                var datumteil1 = s.split('.');
                return datumteil1[2] + datumteil1[1] + datumteil1[0];
            }
        },
        type: 'text'
    });


    ts.addParser({
        id: 'stundenauswertung',
        is: function (s) {
            return false;
        },
        format: function (s) {
//              alert(s);
            var text = s.split('min')[0];
            var splitText = text.split('h ');
            var retValue = 0;
            if (splitText.length > 1)
            {
                retValue = +parseInt(splitText[0] * 60) + parseInt(splitText[1]);
            } else
            {
                retValue = +parseInt(text);
            }
            return retValue;
        },
        type: 'number'
    });


    ts.addParser({
        id: "time",
        is: function (s) {
            return /^(([0-2]?[0-9]:[0-5][0-9])|([0-1]?[0-9]:[0-5][0-9]\s(am|pm)))$/.test(s);
        }, format: function (s) {
            return $.tablesorter.formatFloat(new Date("2000/01/01 " + s).getTime());
        }, type: "numeric"
    });
    ts.addParser({
        id: "metadata",
        is: function (s) {
            return false;
        }, format: function (s, table, cell) {
            var c = table.config,
                    p = (!c.parserMetadataName) ? 'sortValue' : c.parserMetadataName;
            return $(cell).metadata()[p];
        }, type: "numeric"
    });

    // add default widgets
    ts.addWidget({
        id: "zebra",
        format: function (table) {
            if (table.config.debug) {
                var time = new Date();
            }
            var $tr, row = -1,
                    odd;
            // loop through the visible rows
            $("tr:visible", table.tBodies[0]).each(function (i) {
                $tr = $(this);
                // style children rows the same way the parent
                // row was styled
                if (!$tr.hasClass(table.config.cssChildRow))
                    row++;
                odd = (row % 2 == 0);
                $tr.removeClass(
                        table.config.widgetZebra.css[odd ? 0 : 1]).addClass(
                        table.config.widgetZebra.css[odd ? 1 : 0])
            });
            if (table.config.debug) {
                $.tablesorter.benchmark("Applying Zebra widget", time);
            }
        }
    });
})(jQuery);