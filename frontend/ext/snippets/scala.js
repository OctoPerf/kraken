define("ace/snippets/scala", ["require", "exports", "module"], function (require, exports, module) {
  "use strict";

// https://cloud9-sdk.readme.io/docs/snippets

  exports.snippetText = "# Prototype\n\
snippet pck\n\
	package ${1:package_name}\n\
snippet impg\n\
	import io.gatling.${1:package_name}._\n\
snippet imps\n\
	import io.gatling.core.Predef._\n\
	import io.gatling.http.Predef._\n\
	import scala.concurrent.duration._\n\
snippet class\n\
	class ${1:class_name} extends Simulation {\n\
		${4:// body...}\n\
	}\n\
snippet obj\n\
	object ${1:object_name} {\n\
		${4:// body...}\n\
	}\n\
snippet reqg\n\
	http(\"${1:request_name}\")\n\
		.get(\"${2:url}\")\
		.queryParam(\"${2:param_key}\", \"${2:param_value}\")\n\
snippet reqp\n\
	http(\"${1:request_name}\")\n\
		.post(\"${2:url}\")\n\
		.body(RawFileBody(\"${3:file}\"))\n\
snippet http\n\
	http\n\
		.baseUrl(\"${1:url}\")\n\
		.header(HttpHeaderNames.ContentType, HttpHeaderValues.ApplicationJson)\n\
		.header(HttpHeaderNames.Accept, HttpHeaderValues.ApplicationJson)\n\
";

  exports.scope = "scala";

});
(function () {
  window.require(["ace/snippets/scala"], function (m) {
    if (typeof module == "object" && typeof exports == "object" && module) {
      module.exports = m;
    }
  });
})();
