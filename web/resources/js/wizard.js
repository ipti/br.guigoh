/* 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
locale = $("#localeAcronym").val();
navigator.userLanguage = locale.substring(0, 2);



$(document).ready(function() {
    loadAll();
    jsf.ajax.addOnEvent(function(data){
        if (data.status === 'success') {
            loadAll();
        }
    });
});

function loadAll(){
    $(".dateMMyyyy").mask("99/9999");
    $(".phone").mask("(99) 9999-9999");
    $(".zipcode").mask("99999-999");
    // choose either the full version
    $(".multiselect").multiselect({
        sortable: false, 
        searchable: false
    });
    $('.multiselect').bind('multiselectChange', function(evt, ui) {
        var selectedCount = $("option:selected", this).length;
        $(this)
        .find('option:not(:selected)').prop('disabled', selectedCount >= 6).end()
        .multiselect('refresh');
    });
    
    $( "#dialog-form" ).dialog({
        autoOpen: false,
        height: 220,
        width: 600,
        modal: true,
        overlay: {
            backgroundColor: "#000", 
            opacity: 0.5
        },
        buttons:{
            "Close": function() {
                $(this).dialog("close");
            }
        },
        close: function(ev, ui) {
            $(this).hide();
        }
    });
 
    $('.change_avatar').click(function() {
        $("#dialog-form").dialog('open');
        return false;
    });
    
    $( ".occupation" ).autocomplete({
        source: occupations,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".occupation" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".occupation" ).val(ui.item.label);
            $( "#occupationHiddenid" ).val(ui.item.value);
            return false;
        }
    
    });
    $( ".occupation_edit_name" ).autocomplete({
        source: occupations,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".occupation_edit_name" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".occupation_edit_name" ).val(ui.item.label);
            return false;
        }
    
    });
    $( ".activity" ).autocomplete({
        source: occupations,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".activity" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".activity" ).val(ui.item.label);
            $( "#activityHiddenid" ).val(ui.item.value);
            return false;
        }
    });
    $( ".books1" ).autocomplete({
        source: books,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".books1" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".books1" ).val(ui.item.label);
            $( "#booksHidden1" ).val(ui.item.value);
            return false;
        }
    });
    $( ".books2" ).autocomplete({
        source: books,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".books2" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".books2" ).val(ui.item.label);
            $( "#booksHidden2" ).val(ui.item.value);
            return false;
        }
    });
    $( ".books3" ).autocomplete({
        source: books,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".books3" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".books3" ).val(ui.item.label);
            $( "#booksHidden3" ).val(ui.item.value);
            return false;
        }
    });
    $( ".musics1" ).autocomplete({
        source: musics,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".musics1" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".musics1" ).val(ui.item.label);
            $( "#musicsHidden1" ).val(ui.item.value);
            return false;
        }
    });
    $( ".musics2" ).autocomplete({
        source: musics,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".musics2" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".musics2" ).val(ui.item.label);
            $( "#musicsHidden2" ).val(ui.item.value);
            return false;
        }
    });
    $( ".musics3" ).autocomplete({
        source: musics,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".musics3" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".musics3" ).val(ui.item.label);
            $( "#musicsHidden3" ).val(ui.item.value);
            return false;
        }
    });
    $( ".movies1" ).autocomplete({
        source: movies,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".movies1" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".movies1" ).val(ui.item.label);
            $( "#moviesHidden1" ).val(ui.item.value);
            return false;
        }
    });
    $( ".movies2" ).autocomplete({
        source: movies,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".movies2" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".movies2" ).val(ui.item.label);
            $( "#moviesHidden2" ).val(ui.item.value);
            return false;
        }
    });
    $( ".movies3" ).autocomplete({
        source: movies,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".movies3" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".movies3" ).val(ui.item.label);
            $( "#moviesHidden3" ).val(ui.item.value);
            return false;
        }
    });
    $( ".sports1" ).autocomplete({
        source: sports,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".sports1" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".sports1" ).val(ui.item.label);
            $( "#sportsHidden1" ).val(ui.item.value);
            return false;
        }
    });
    $( ".sports2" ).autocomplete({
        source: sports,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".sports2" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".sports2" ).val(ui.item.label);
            $( "#sportsHidden2" ).val(ui.item.value);
            return false;
        }
    });
    $( ".sports3" ).autocomplete({
        source: sports,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".sports3" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".sports3" ).val(ui.item.label);
            $( "#sportsHidden3" ).val(ui.item.value);
            return false;
        }
    });
    $( ".hobbies1" ).autocomplete({
        source: hobbies,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".hobbies1" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".hobbies1" ).val(ui.item.label);
            $( "#hobbiesHidden1" ).val(ui.item.value);
            return false;
        }
    });
    $( ".hobbies2" ).autocomplete({
        source: hobbies,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".hobbies2" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".hobbies2" ).val(ui.item.label);
            $( "#hobbiesHidden2" ).val(ui.item.value);
            return false;
        }
    });
    $( ".hobbies3" ).autocomplete({
        source: hobbies,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".hobbies3" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".hobbies3" ).val(ui.item.label);
            $( "#hobbiesHidden3" ).val(ui.item.value);
            return false;
        }
    });
    
    $( ".experiencesLocation" ).autocomplete({
        source: experiencesLocation,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".experiencesLocation" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".experiencesLocation" ).val(ui.item.label);
            $( "#experiencesLocationHidden" ).val(ui.item.value);
            return false;
        }
    });
    $( ".educationName" ).autocomplete({
        source: educationName,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".educationName" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".educationName" ).val(ui.item.label);
            $( "#educationNameHidden" ).val(ui.item.value);
            return false;
        }
    });
    $( ".educationLocation" ).autocomplete({
        source: educationLocation,
        focus: function(event, ui) {
            // Set the text input value to the focused item's label, instead of the value.
            $( ".educationLocation" ).val(ui.item.label);
            return false;
        },
        select: function(event, ui) {
            // Save the selection item and value in the two inputs.
            $( ".educationLocation" ).val(ui.item.label);
            $( "#educationLocationHidden" ).val(ui.item.value);
            return false;
        }
    });
}

