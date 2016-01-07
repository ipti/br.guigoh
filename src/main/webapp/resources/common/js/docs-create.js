var saveTimer;

$(document).ready(function () {
    tinymce.init({
        selector: 'textarea',
        height: 400,
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
                    $(".doc-saved").addClass("visible");
                    setTimeout(function () {
                        $(".doc-saved").removeClass("visible");
                    }, 3000);
                }, 3000);
            });
        },
        init_instance_callback: function (editor) {
            editor.setContent($("#text").val());
        }
    });
});

$("#title").keyup(function (e) {
    clearTimeout(saveTimer);
    saveTimer = setTimeout(function () {
        $("#text").val(tinyMCE.activeEditor.getContent());
        $(".save-text").click();
        $(".doc-saved").addClass("visible");
        setTimeout(function () {
            $(".doc-saved").removeClass("visible");
        }, 3000);
    }, 3000);
});

$("#title").focus(function () {
    var save_this = $(this);
    window.setTimeout(function () {
        save_this.select();
    }, 100);
});