import {Injectable} from '@angular/core';
import {Ace, config} from 'ace-builds';
import {VariablesAutoCompleter} from 'projects/editor/src/lib/variables-auto-completer';

// We copy the workers from ace-build to the assets/ace folder using angular-cli (assets config in the angular.json file)
config.set('basePath', 'assets/ace');
config.loadModule('ace/ext/language_tools', () => {
});

@Injectable()
export class CodeService {

  initCodeEditorComponent(editor: Ace.Editor, autoCompleter?: VariablesAutoCompleter) {
    editor.resize(true);
    if (autoCompleter) {
      (editor as any).completers.push({
        getCompletions: autoCompleter.autoCompleteVariableNames.bind(autoCompleter)
      });
    }
    // console.log((window as any).langTools);
    // console.log(_editor);
  }

}
