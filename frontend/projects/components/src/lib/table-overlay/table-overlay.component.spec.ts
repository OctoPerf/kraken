import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {TableOverlayComponent} from 'projects/components/src/lib/table-overlay/table-overlay.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {MatTableDataSource} from '@angular/material/table';

describe('TableOverlayComponent', () => {
  let component: TableOverlayComponent;
  let fixture: ComponentFixture<TableOverlayComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ComponentsModule],
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(TableOverlayComponent);
    component = fixture.componentInstance;
  });

  it('should be loading', () => {
    component.loading = true;
    component.dataSource = new MatTableDataSource([1, 2]);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.loading-shade')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('.no-data-shade')).toBeFalsy();
  });

  it('should be empty default label', () => {
    component.loading = false;
    component.dataSource = new MatTableDataSource([]);
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.loading-shade')).toBeFalsy();
    expect(fixture.nativeElement.querySelector('.no-data-shade')).toBeTruthy();
  });

  it('should be empty default label no dataSource', () => {
    component.loading = false;
    component.dataSource = undefined;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.loading-shade')).toBeFalsy();
    expect(fixture.nativeElement.querySelector('.no-data-shade')).toBeTruthy();
  });

  it('should be empty custom label', () => {
    component.loading = false;
    component.dataSource = new MatTableDataSource([]);
    component.noDataLabel = 'DATASS';
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('.loading-shade')).toBeFalsy();
    expect(fixture.nativeElement.querySelector('.no-data-shade')).toBeTruthy();
  });
});
