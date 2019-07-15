define('ace/mode/log', function (require, exports, module) {

  var oop = require("ace/lib/oop");
  var TextMode = require("ace/mode/text").Mode;
  var LogHighlightRules = require("ace/mode/log_highlight_rules").LogHighlightRules;

  var Mode = function () {
    this.HighlightRules = LogHighlightRules;
  };
  oop.inherits(Mode, TextMode);

  (function () {
    // Extra logic goes here. (see below)
  }).call(Mode.prototype);

  exports.Mode = Mode;
});

define('ace/mode/log_highlight_rules', function (require, exports, module) {

  var oop = require("ace/lib/oop");
  var TextHighlightRules = require("ace/mode/text_highlight_rules").TextHighlightRules;

  var LogHighlightRules = function () {
    this.$rules = {
      "start": [
        {
          token: "log.info",
          regex: /(info|INFO|Info)\b/
        },
        {
          token: "log.warn",
          regex: /(warn|WARN|Warn|warning|WARNING|Warning)\b/
        },
        {
          token: "log.error",
          regex: /(error|ERROR|Error)\b/
        },
      ]
    };
  };

  oop.inherits(LogHighlightRules, TextHighlightRules);

  exports.LogHighlightRules = LogHighlightRules;
});
