$(document).ready(function () {
    init();
});

function init() {
    $("#archivesList").load(site.archives);
}