import {Completion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import * as _ from 'lodash';

export class HttpCompletion extends Completion {

  private static readonly HTTP_SCORE = 1040;
  private static readonly HTTP_META = 'HTTP';

  private static readonly HTTP_VERB_SCORE = 1049;
  private static readonly HTTP_VERB_META = 'HTTP Verb';

  private static readonly HTTP_BODY_SCORE = 1048;
  private static readonly HTTP_BODY_META = 'HTTP Body';

  private static readonly HTTP_BODY_CONTENT_SCORE = 1048;
  private static readonly HTTP_BODY_CONTENT_META = 'HTTP Body';

  private static readonly HTTP_OPTION_SCORE = 1047;
  private static readonly HTTP_OPTION_META = 'HTTP Option';


  static afterVerb(context: CompletionContext): boolean {
    return Completion.after('get', 'post', 'put', 'delete', 'head', 'patch', 'options', 'httpRequest')(context);
  }

  results(context: CompletionContext): CompletionCommandResult[] {
    return _.concat(
      Completion.matching([CompletionCommandResult.fromCommandWithParams('http(name)', HttpCompletion.HTTP_SCORE, HttpCompletion.HTTP_META)],
        context,
        Completion.noDot,
        Completion.parent('exec', 'resources'),
      ),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('get(name)', HttpCompletion.HTTP_VERB_SCORE, HttpCompletion.HTTP_VERB_META),
          CompletionCommandResult.fromCommandWithParams('post(name)', HttpCompletion.HTTP_VERB_SCORE, HttpCompletion.HTTP_VERB_META),
          CompletionCommandResult.fromCommandWithParams('put(name)', HttpCompletion.HTTP_VERB_SCORE, HttpCompletion.HTTP_VERB_META),
          CompletionCommandResult.fromCommandWithParams('delete(name)', HttpCompletion.HTTP_VERB_SCORE, HttpCompletion.HTTP_VERB_META),
          CompletionCommandResult.fromCommandWithParams('head(name)', HttpCompletion.HTTP_VERB_SCORE, HttpCompletion.HTTP_VERB_META),
          CompletionCommandResult.fromCommandWithParams('patch(name)', HttpCompletion.HTTP_VERB_SCORE, HttpCompletion.HTTP_VERB_META),
          CompletionCommandResult.fromCommandWithParams('options(name)', HttpCompletion.HTTP_VERB_SCORE, HttpCompletion.HTTP_VERB_META),
          CompletionCommandResult.fromCommandWithParams('httpRequest(method, url)', HttpCompletion.HTTP_VERB_SCORE, HttpCompletion.HTTP_VERB_META),
        ],
        context,
        Completion.dot,
        // Completion.parent('exec', 'resources'),
        Completion.afterDirect('http')
      ),
      Completion.matching([
          CompletionCommandResult.fromCommand('body()', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommand('bodyPart()', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommandWithParams('formParam(key, value)', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommandWithParams('multivaluedFormParam(key, value...)', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommandWithParams('formParamSeq((key, value)...)', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommandWithParams('formParamMap(Map[key -> value])', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommandWithParams('form(Map[key -> Seq[value]])', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommandWithParams('processRequestBody(Body => Body)', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommandWithParams('transformResponse((Session => Response) => Validation[Response]))', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
          CompletionCommandResult.fromCommandWithParams('formUpload(key, filePath)', HttpCompletion.HTTP_BODY_SCORE, HttpCompletion.HTTP_BODY_META),
        ],
        context,
        Completion.dot,
        // Completion.parent('exec', 'resources'),
        Completion.afterWithParent('http'),
        HttpCompletion.afterVerb,
      ),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('queryParam(key, value)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('multivaluedQueryParam(key, value...)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('queryParamsSeq((key, value)...)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('queryParamsMap(Map[key -> value])', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('header(key, value)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('headers(Map[key, value])', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('sign(signatureCalculator)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('signWithOAuth1(consumerKey, clientSharedSecret, token, tokenSecret)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('basicAuth(user, password)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('digestAuth(user, password)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('ntlmAuth(user, password)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('authRealm(Realm)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromCommandWithParams('resources(request...)', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromText('disableUrlEncoding', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromText('silent', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
          CompletionCommandResult.fromText('noSilent', HttpCompletion.HTTP_OPTION_SCORE, HttpCompletion.HTTP_OPTION_META),
        ],
        context,
        Completion.dot,
        // Completion.parent('exec', 'resources'),
        Completion.afterWithParent('http'),
        HttpCompletion.afterVerb,
      ),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('RawFileBody(path)', HttpCompletion.HTTP_BODY_CONTENT_SCORE, HttpCompletion.HTTP_BODY_CONTENT_META),
          CompletionCommandResult.fromCommandWithParams('ELFileBody(path)', HttpCompletion.HTTP_BODY_CONTENT_SCORE, HttpCompletion.HTTP_BODY_CONTENT_META),
          CompletionCommandResult.fromCommandWithParams('StringBody(string)', HttpCompletion.HTTP_BODY_CONTENT_SCORE, HttpCompletion.HTTP_BODY_CONTENT_META),
          CompletionCommandResult.fromCommandWithParams('ByteArrayBody(bytes)', HttpCompletion.HTTP_BODY_CONTENT_SCORE, HttpCompletion.HTTP_BODY_CONTENT_META),
          CompletionCommandResult.fromCommandWithParams('InputStreamBody(stream)', HttpCompletion.HTTP_BODY_CONTENT_SCORE, HttpCompletion.HTTP_BODY_CONTENT_META),
          CompletionCommandResult.fromCommandWithParams('PebbleBody(path)', HttpCompletion.HTTP_BODY_CONTENT_SCORE, HttpCompletion.HTTP_BODY_CONTENT_META),
        ],
        context,
        Completion.noDot,
        Completion.parent('body', 'bodyPart'),
      )
    );
  }
}
