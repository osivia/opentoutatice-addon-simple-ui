var lang = 'fr';

function createTinyMce(clazz) {
	var textareas = document.getElementsByClassName(clazz);
	for ( var index = 0; index < textareas.length; index++) {
		tinyMCE.execCommand('mceAddEditor', false, textareas[index].id);
	}
}

window.onload = function(e){

tinyMCE.init({
	editor_selector : "mceEditorMinimalInLine",
	mode : "specific_textareas",
	theme : "modern",
	plugins : "",
	language : lang,
	theme_advanced_resizing : true,
	height : "25",
	content_css: "/nuxeo/css/toutatice-popup.min.css?" + new Date().getTime(),

	inline_styles : false,
	formats : {
		underline : {
			inline : 'u',
			exact : false
		}
	},

	// Disable <p> and <br> tag generation
	force_p_newlines : false,
	forced_root_block : false,
	invalid_elements : "br",
	valid_elements : "strong/b,em,u,span[style<text-decoration: underline;]",

	relative_urls : false,
	remove_script_host: true,
	document_base_url : baseURL,

	theme_advanced_buttons1 : "bold,italic,underline",
	theme_advanced_buttons2 : "",
	theme_advanced_buttons3 : ""

});

createTinyMce("mceEditorMinimalInLine,disableMCEInit");

tinyMCE
.init({
	editor_selector : "mceEditorMinimal",
	mode : "specific_textareas",
	theme : "modern",
	plugins : "",
	language : lang,
	theme_advanced_resizing : true,
	height : "150",
	content_css: "/nuxeo/css/toutatice-popup.min.css?" + new Date().getTime(),

	inline_styles : false,
	formats : {
		underline : {
			inline : 'u',
			exact : false
		}
	},

	valid_elements : "p,br,strong/b,em,u,span[style<text-decoration: underline;]",

	relative_urls : false,
	remove_script_host: true,
	document_base_url : baseURL,

	theme_advanced_buttons1 : "bold,italic,underline",
	theme_advanced_buttons2 : "",
	theme_advanced_buttons3 : "",

});

createTinyMce("mceEditorMinimal,disableMCEInit");

}
