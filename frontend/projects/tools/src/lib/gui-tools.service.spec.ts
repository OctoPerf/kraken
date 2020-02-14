import {ComponentFixture, inject, TestBed} from '@angular/core/testing';

import {GuiToolsService} from './gui-tools.service';
import {By} from '@angular/platform-browser';
import {LoadingIconComponent} from 'projects/components/src/lib/loading-icon/loading-icon.component';

class TestComponent {
}

fdescribe('GuiToolsService', () => {

  // let fixture: ComponentFixture<TestComponent>;

  const scrollableTreeSpy = () => {
    const spy = jasmine.createSpyObj('ElementRef', ['']);
    spy.nativeElement = '<div>' +
      '  <div id="username" >Hello</div>' +
      '  <button id="button" />' +
      '</div>';
    return spy;
  };


  const getElement = () => {
    return document.getElementsByClassName('cdk-overlay-backdrop')[0];
  };

  beforeEach(() => TestBed.configureTestingModule({}));

  it('should be created', () => {
    const service: GuiToolsService = TestBed.get(GuiToolsService);
    expect(service).toBeTruthy();
  });
  // TODO à finir
  // it('should be created', () => {
  //   const service: GuiToolsService = TestBed.get(GuiToolsService);
  // const spy = jasmine.createSpyObj('ElementRef', ['nativeElement']);
  // spy.nativeElement = document.createElement('div');
  // jasmine.createSpyObj('nativeElement', ['offsetHeight', 'getBoundingClientRect']);
  //   spy.nativeElement.offsetHeight = 200;
  // spy.nativeElement.getBoundingClientRect() = jasmine.createSpyObj('getBoundingClientRect', ['top']);
  // spyOn(spy.nativeElement, 'getBoundingClientRect').and.returnValue({'top': 100});
  // spy.nativeElement.getBoundingClientRect().top = 100;
  // spyOn(spy.nativeElement, 'getBoundingClientRect').and.callFake(
  //  jasmine.createSpy('getBoundingClientRect').and.returnValue({top: 1, height: 100, left: 2, width: 200, right: 202})
  // );

  // service.scrollTo(spy, getElement);
  // });

});
