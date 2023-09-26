window.onload = function(){
	//실행될 코드
	console.log("ready!!");

	document.getElementById("title_icon").onclick = function() {
	    location.href='/';
	};

	document.getElementById("go_to_swagger_btn").onclick = function() {
	    location.href='/swagger-ui/index.html';
	};
}