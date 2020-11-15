import {ComponentFixture, TestBed, waitForAsync} from '@angular/core/testing';

import {HeadersTableComponent} from 'projects/analysis/src/lib/results/debug/headers-table/headers-table.component';

describe('HeadersTableComponent', () => {
  let component: HeadersTableComponent;
  let fixture: ComponentFixture<HeadersTableComponent>;

  beforeEach(waitForAsync(() => {
    TestBed.configureTestingModule({
      declarations: [HeadersTableComponent]
    })
      .overrideTemplate(HeadersTableComponent, '')
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(HeadersTableComponent);
    component = fixture.componentInstance;
    component.headers = [
      {key: 'key1', value: 'value1'},
      {key: 'key2', value: 'value2'},
    ];
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should init dataSource', () => {
    expect(component.dataSource).toBeTruthy();
    expect(component.dataSource.data).toBe(component.headers);
  });
});
