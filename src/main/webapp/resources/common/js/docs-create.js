tinyMCE.init({
    // General options
    selector: "#docs-textarea",
    theme: "modern",
    height: 500,
    plugins: [
        "advlist autolink lists link image charmap print preview hr anchor pagebreak",
        "searchreplace wordcount visualblocks visualchars fullscreen",
        "insertdatetime media nonbreaking save table contextmenu directionality",
        "template paste textcolor colorpicker textpattern imagetools"
    ],
    removed_menuitems: 'newdocument',
    toolbar1: "undo redo | styleselect | bold italic | alignleft aligncenter alignright alignjustify | bullist numlist outdent indent | link image",
    toolbar2: "print preview media | forecolor backcolor | fontsizeselect fontselect",
    image_advtab: true,
    fontsize_formats: "8pt 10pt 12pt 14pt 18pt 24pt 36pt",
    font_formats: "Andale Mono=andale mono,times;" +
            "Arial=arial,helvetica,sans-serif;" +
            "Arial Black=arial black,avant garde;" +
            "Book Antiqua=book antiqua,palatino;" +
            "Comic Sans MS=comic sans ms,sans-serif;" +
            "Courier New=courier new,courier;" +
            "Georgia=georgia,palatino;" +
            "Helvetica=helvetica;" +
            "Impact=impact,chicago;" +
            "Symbol=symbol;" +
            "Tahoma=tahoma,arial,helvetica,sans-serif;" +
            "Terminal=terminal,monaco;" +
            "Times New Roman=times new roman,times;" +
            "Trebuchet MS=trebuchet ms,geneva;" +
            "Verdana=verdana,geneva;" +
            "Webdings=webdings;" +
            "Wingdings=wingdings,zapf dingbats",
    resize: false,
    statusbar: false,
    table_default_styles: {
        width: '100%'
    }
});


function getContent() {
    $("#text").val(tinyMCE.activeEditor.getContent());
    return true;
}

jsf.ajax.addOnEvent(function (data) {
    if (data.status === "success") {
        console.log($("#text").val());
        tinyMCE.activeEditor.setContent($("#text").val());
    }
});