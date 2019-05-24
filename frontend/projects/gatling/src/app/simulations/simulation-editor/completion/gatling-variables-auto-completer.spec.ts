import {Component, ElementRef, Input, OnInit} from '@angular/core';
import {Ace, edit} from 'ace-builds';
import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {GatlingVariablesAutoCompleter} from 'projects/gatling/src/app/simulations/simulation-editor/completion/gatling-variables-auto-completer';
import {VendorsModule} from 'projects/vendors/src/lib/vendors.module';
import {CompletionCallback} from 'projects/editor/src/lib/completion-callback';
import {CompletionCommandResult} from 'projects/gatling/src/app/simulations/simulation-editor/completion/completion-command-result';
import * as _ from 'lodash';

@Component({
  selector: 'app-test',
  template: ''
})
class TestComponent implements OnInit {
  public editor: Ace.Editor;

  @Input() value: string;

  constructor(private elRef: ElementRef) {
  }

  ngOnInit() {
    this.editor = edit(this.elRef.nativeElement, {
      value: this.value,
    } as any);
  }
}

describe('GatlingVariablesAutoCompleter', () => {
  let component: TestComponent;
  let fixture: ComponentFixture<TestComponent>;
  let completer: GatlingVariablesAutoCompleter;
  let callback: CompletionCallback;
  let results: CompletionCommandResult[] = [];

  beforeEach(async(() => {
    TestBed
      .configureTestingModule({
        imports: [VendorsModule],
        declarations: [
          TestComponent,
        ],
        providers: [
          GatlingVariablesAutoCompleter
        ]
      })
      .compileComponents();
    callback = (error: string | null, _results: { name: string, value: string, score: number, meta: string }[]) => {
      results = _results as any;
    };
    completer = TestBed.get(GatlingVariablesAutoCompleter);
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TestComponent);
    component = fixture.componentInstance;
    component.value =
      `/*
 * Copyright 2011-2019 GatlingCorp (https://gatling.io)
 */

package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._

class BasicSimulation extends Simulation {

  val httpProtocol = http
    .baseUrl("http://computer-database.gatling.io") // Here is the root for all relative URLs
    .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") // Here are the common headers
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Macintosh; Intel Mac OS X 10.8; rv:16.0) Gecko/20100101 Firefox/16.0")

  val headers_10 = Map("Content-Type" -> "application/x-www-form-urlencoded") // Note the headers specific to a given request

  val scn = scenario("Scenario Name") // A scenario is a chain of requests and pauses
    .exec(http("request_1")
      .get("/"))
    .pause(7) // Note that Gatling has recorded real time pauses
    .exec(http("request_2")
      .get("/computers?f=macbook"))
    .pause(2)
    .exec(http("request_3")
      .get("/computers/6"))
    .pause(3)
    .exec(http("request_4")
      .get("/"))
    .pause(2)
    .exec(http("request_5")
      .get("/computers?p=1"))
    .pause(670 milliseconds)
    .exec(http("request_6")
      .get("/computers?p=2"))
    .pause(629 milliseconds)
    .exec(http("request_7")
      .get("/computers?p=3"))
    .pause(734 milliseconds)
    .exec(http("request_8")
      .get("/computers?p=4"))
    .pause(5)
    .exec(http("request_9")
      .get("/computers/new"))
    .pause(1)
    .exec(http("request_10") // Here's an example of a POST request
      .post("/computers")
      .headers(headers_10)
      .formParam("name", "Beautiful Computer")
      .formParam("introduced", "2012-05-30")
      .formParam("discontinued", "")
      .formParam("company", "37"))

  setUp(scn.inject(atOnceUsers(1)).protocols(httpProtocol))
}`;
    fixture.detectChanges();
  });

  it('should call callback with Scenario completion', () => {
    completer.autoCompleteVariableNames(component.editor, component.editor.session, {row: 0, column: 0}, '', callback);
    const result = _.find(results, {name: 'scenario'});
    expect(result).toBeDefined();
    (result as any).completer.insertMatch(component.editor, result);
    expect(component.editor.session.getLine(0)).toEqual('scenario(name)/*');
  });

  it('should call callback with http completion', () => {
    completer.autoCompleteVariableNames(component.editor, component.editor.session, {row: 23, column: 12}, '', callback);
    const result = _.find(results, {command: 'http(name)'});
    expect(result).toBeDefined();
    component.editor.gotoLine(24, 12, false);
    (result as any).completer.insertMatch(component.editor, result);
    expect(component.editor.session.getLine(23)).toEqual('    .exec(http(name)tp("request_1")');
  });

  it('should call callback with exec completion', () => {
    completer.autoCompleteVariableNames(component.editor, component.editor.session, {row: 25, column: 5}, '', callback);
    const result = _.find(results, {name: 'exec'});
    expect(result).toBeDefined();
    component.editor.gotoLine(26, 5, false);
    (result as any).completer.insertMatch(component.editor, result);
    expect(component.editor.session.getLine(25)).toEqual('    .exec()pause(7) // Note that Gatling has recorded real time pauses');
  });
});
