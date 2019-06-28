import {async, ComponentFixture, TestBed} from '@angular/core/testing';
import {CodeDiffComponent} from 'projects/editor/src/lib/code-diff/code-diff.component';


describe('CodeDiffComponent', () => {
  let component: CodeDiffComponent;
  let fixture: ComponentFixture<CodeDiffComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [CodeDiffComponent]
    })
      .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(CodeDiffComponent);
    component = fixture.componentInstance;
    component.left = 'toto';
    component.right = 'tata';
  });

  it('should create', () => {
    fixture.detectChanges();
    expect(component).toBeTruthy();
  });

  it('should resize fail', () => {
    fixture.detectChanges();
    spyOn(component.mergeView, 'rightOriginal').and.returnValue({} as any);
    spyOn(component.mergeView, 'editor').and.returnValue({} as any);
    component.resize();
  });
});
