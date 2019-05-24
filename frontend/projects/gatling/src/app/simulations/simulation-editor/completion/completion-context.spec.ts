import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';

export const testCompletionContextNoParent: () => CompletionContext = () => {
  return {
    precedingKeywords: ['http', 'get', 'header'],
    lastKeyword: 'header',
    parentKeyword: null,
    parentIndex: -1,
    endsWithDot: false,
  };
};

export const testCompletionContextWithParent: () => CompletionContext = () => {
  return {
    precedingKeywords: ['exec', 'http', 'get', 'header'],
    lastKeyword: 'header',
    parentKeyword: 'exec',
    parentIndex: 0,
    endsWithDot: false,
  };
};
