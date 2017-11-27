// For BO compatibility
var lang = 'fr';

function createTinyMceBar(clazz) {
	var textareas = document.getElementsByClassName(clazz);
	for ( var index = 0; index < textareas.length; index++) {
		tinyMCE.execCommand('mceAddEditor', false, textareas[index].id);
	}
}

function createTinyMCEResponsive() {
tinyMCE
	.init({
		  editor_selector : /(mceEditor|mceEditorResponsive)/,
		  mode : "specific_textareas",
		  width : "100%",
		  height : "350",
		  theme : "modern",
		  plugins : ["link ottc_image code charmap fullscreen nuxeoimageupload nuxeolink"],
		  language : lang,
		  block_formats: "Paragraphe=p;Adresse=address;Pre=pre;Titre 1=h1;Titre 2=h2;Titre 3=h3;Titre 4=h4;Titre 5=h5; Titre 6=h6",
		  content_css: "/nuxeo/css/toutatice-popup.min.css?" + new Date().getTime(),
		
		  relative_urls : false,
	      remove_script_host: true,
	      document_base_url : baseURL,
		
		  toolbar1 : "formatselect | bold italic underline | alignleft aligncenter alignright alignjustify | bullist numlist",
		  toolbar2 : "fullscreen | undo redo | code | link unlink nuxeolink | nuxeoimageupload ottc_image",
		  menubar: false,
		  statusbar: false
	  
	});

    createTinyMceBar("mceEditorResponsive,mceEditor,disableMCEInit");

}

window.addEventListener("load", createTinyMCEResponsive);