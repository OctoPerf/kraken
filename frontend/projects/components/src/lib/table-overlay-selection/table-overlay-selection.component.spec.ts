import {async, ComponentFixture, fakeAsync, TestBed, tick} from '@angular/core/testing';
import {TableOverlayComponent} from 'projects/components/src/lib/table-overlay/table-overlay.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';
import {MatTableDataSource} from '@angular/material/table';
import {TableOverlaySelectionComponent} from 'projects/components/src/lib/table-overlay-selection/table-overlay-selection.component';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';
import {SelectionModel} from '@angular/cdk/collections';
import {GuiToolsService} from 'projects/tools/src/lib/gui-tools.service';
import {guiToolsServiceSpy} from 'projects/tools/src/lib/gui-tools.service.spec';

describe('TableOverlaySelectionComponent', () => {
  let component: TableOverlaySelectionComponent<any>;
  let fixture: ComponentFixture<TableOverlaySelectionComponent<any>>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [ComponentsModule],
      providers: [
        {provide: GuiToolsService, useValue: guiToolsServiceSpy()}
      ],
    })
      .compileComponents();
    fixture = TestBed.createComponent(TableOverlaySelectionComponent);
    component = fixture.componentInstance;
    component.selection = new SelectionModel<StorageNode>();
  }));

  beforeEach(() => {
    component.ngOnInit();
    component.dataSource = new MatTableDataSource([]);
    const datas: () => any[] = () => {
      return [{id: '1'},
        {id: '2'},
        {id: '3'},
        {id: '4'},
      ];
    };
    component.dataSource.data = datas();
  });

  afterEach(() => {
    component.ngOnDestroy();
  });

  it('should be up selection', () => {
    component.selection.select(component.dataSource.data[3]);
    expect(component.upSelection()).toBe(true);
    expect(component.selection.selected).toEqual([component.dataSource.data[2]]);
  });

  it('should be not up selection', () => {
    component.selection.select(component.dataSource.data[0]);
    expect(component.upSelection()).toBe(false);
  });

  it('should be down selection', () => {
    component.selection.select(component.dataSource.data[1]);
    expect(component.downSelection()).toBe(true);
    expect(component.selection.selected).toEqual([component.dataSource.data[2]]);
  });

  it('should be not down selection', () => {
    component.selection.select(component.dataSource.data[3]);
    expect(component.downSelection()).toBe(false);
  });

  it('should _deleteSelectionEmit', () => {
    const spy = spyOn(component.deleteSelection, 'emit');
    component._deleteSelectionEmit({ctrlKey: true} as any);
    expect(spy).toHaveBeenCalledWith(true);
  });

  it('should _enterSelectionEmit', () => {
    const spy = spyOn(component.enterSelection, 'emit');
    component._enterSelectionEmit();
    expect(spy).toHaveBeenCalled();
  });

});
