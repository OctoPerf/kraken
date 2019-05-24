import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {Component} from '@angular/core';
import {By} from '@angular/platform-browser';
import {LoadingIconComponent} from 'projects/components/src/lib/loading-icon/loading-icon.component';
import {ComponentsModule} from 'projects/components/src/lib/components.module';

@Component({
  selector: 'lib-test',
  template: `
    <lib-loading-icon [loading]="false">
      <span>Not Loading!</span>
    </lib-loading-icon>
  `
})
class TestComponent {
}

describe('LoadingIconComponent', () => {
  let component: LoadingIconComponent;
  let fixture: ComponentFixture<LoadingIconComponent>;
  let notLoadingComponent: TestComponent;
  let notLoadingFixture: ComponentFixture<TestComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [TestComponent],
      imports: [ComponentsModule]
    }).compileComponents();
  }));

  it('should not be loading', () => {
    notLoadingFixture = TestBed.createComponent(TestComponent);
    notLoadingComponent = notLoadingFixture.componentInstance;
    notLoadingFixture.detectChanges();
    expect(notLoadingFixture.debugElement.query(By.css('span')).nativeElement.innerHTML).toBe('Not Loading!');
  });

  it('should be loading', () => {
    fixture = TestBed.createComponent(LoadingIconComponent);
    component = fixture.componentInstance;
    component.loading = true;
    fixture.detectChanges();
    expect(fixture.debugElement.query(By.css('.loading-spinner')).nativeElement).toBeDefined();
  });
});
