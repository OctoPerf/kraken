import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {CookiesTableComponent} from 'projects/analysis/src/lib/results/debug/cookies-table/cookies-table.component';

describe('CookiesTableComponent', () => {
  let component: CookiesTableComponent;
  let fixture: ComponentFixture<CookiesTableComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CookiesTableComponent]
    })
      .overrideTemplate(CookiesTableComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CookiesTableComponent);
    component = fixture.componentInstance;
    component.cookies = ['Cookie'];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init dataSource', () => {
    expect(component.dataSource).toBeTruthy();
  });
});
