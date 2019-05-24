import {Completion} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import {CompletionContext} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-context';
import * as _ from 'lodash';

export class HttpProtocolCompletion extends Completion {

  private static readonly HTTP_SCORE = 1110;
  private static readonly HTTP_META = 'HTTP Protocol';

  private static readonly HTTP_CONF_SCORE = 1109;
  private static readonly HTTP_CONF_META = 'HTTP Configuration';


  results(context: CompletionContext): CompletionCommandResult[] {
    return _.concat(
      Completion.matching([CompletionCommandResult.fromText('http', HttpProtocolCompletion.HTTP_SCORE, HttpProtocolCompletion.HTTP_META)],
        context,
        Completion.noDot,
        Completion.noParent,
      ),
      Completion.matching([
          CompletionCommandResult.fromCommandWithParams('baseUrl(url)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('baseUrls(url...)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('virtualHost(name)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('proxy(proxyObject)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('noProxyFor(hosts)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('httpsPort(port)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('credentials(username, password)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('socks4', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('socks5', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('acceptHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('acceptCharsetHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('acceptEncodingHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('acceptLanguageHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('authorizationHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('connectionHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('contentTypeHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('doNotTrackHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('userAgentHeader(value)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('disableFollowRedirect', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('maxRedirects(max)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('disableAutomaticReferer', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('disableWarmUp', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('warmUp(url)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('inferHtmlResources(WhiteList)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('nameInferredHtmlResourcesAfterUrlTail(WhiteList)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('nameInferredHtmlResourcesAfterPath', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('nameInferredHtmlResourcesAfterAbsoluteUrl', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('nameInferredHtmlResourcesAfterRelativeUrl', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('nameInferredHtmlResourcesAfterLastPathElement', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('nameInferredHtmlResources(Uri => String)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('maxConnectionsPerHost(max)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('shareConnections', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('enableHttp2', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('http2PriorKnowledge(Map[String, Boolean])', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('asyncNameResolution', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('perUserNameResolution', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('localAddress(InetAddress)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('localAddresses(InetAddress...)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('disableCaching', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('disableUrlEncoding', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('silentUri(String)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromText('silentResources', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('basicAuth(user, password)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('digestAuth(user, password)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('ntlmAuth(user, password)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
          CompletionCommandResult.fromCommandWithParams('authRealm(realm)', HttpProtocolCompletion.HTTP_CONF_SCORE, HttpProtocolCompletion.HTTP_CONF_META),
        ],
        context,
        Completion.dot,
        Completion.after('http'),
        Completion.noParent,
      ),
    );
  }
}
