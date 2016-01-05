$(document).ready(function () {
    var saveTimer;
    tinymce.init({
        selector: 'textarea',
        height: 500,
        width: 700,
        statusbar: false,
        plugins: [
            'advlist autolink lists link image charmap print preview anchor',
            'searchreplace visualblocks fullscreen',
            'insertdatetime media table contextmenu paste'
        ],
        toolbar: 'insertfile undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image',
        setup: function (ed) {
            ed.on('keyup change', function (e) {
                clearTimeout(saveTimer);
                saveTimer = setTimeout(function () {
                    $("#text").val(tinyMCE.activeEditor.getContent());
                    $(".save-text").click();
                }, 3000);
            });
        }
    });
})