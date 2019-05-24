import {async, ComponentFixture, TestBed} from '@angular/core/testing';

import {IconComponent} from './icon.component';
import {IconModule} from '../icon.module';
import {IconFa} from '../icon-fa';
import {faAsterisk} from '@fortawesome/free-solid-svg-icons/faAsterisk';
import {IconDynamic} from '../icon-dynamic';
import {library} from '@fortawesome/fontawesome-svg-core';
import {IconFaCounter} from 'projects/icon/src/lib/icon-fa-counter';
import {IconFaAddon} from 'projects/icon/src/lib/icon-fa-addon';

library.add(faAsterisk);

describe('IconComponent', () => {
  let component: IconComponent;
  let fixture: ComponentFixture<IconComponent>;
  const iconFa = new IconFa(faAsterisk, 'error');
  const iconFaAddon = new IconFaAddon(iconFa, new IconFa(faAsterisk, 'success', 'shrink-3 up-6 right-5'));
  const iconFaCounter = new IconFaCounter(iconFa, '10', 'error');
  const iconDynamic = new IconDynamic(iconFa, {'test': iconFaAddon});

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      imports: [IconModule]
    })
      .compileComponents();
    fixture = TestBed.createComponent(IconComponent);
    component = fixture.componentInstance;
  }));

  it('should create font-awesome icon', () => {
    component.icon = iconFa;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('lib-icon-fa')).toBeTruthy();
    expect(fixture.nativeElement.querySelector('.text-error')).toBeTruthy();
  });

  it('should create layered icon', () => {
    component.icon = iconFaAddon;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('lib-icon-fa-addon')).toBeTruthy();
  });

  it('should create layered counter icon', () => {
    component.icon = iconFaCounter;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('lib-icon-fa-counter')).toBeTruthy();
  });

  it('should create dynamic default icon', () => {
    component.icon = iconDynamic;
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('lib-icon-dynamic')).toBeTruthy();
  });

  it('should create dynamic state icon', () => {
    component.icon = iconDynamic;
    component.state = 'test';
    fixture.detectChanges();
    expect(fixture.nativeElement.querySelector('lib-icon-dynamic')).toBeTruthy();
  });
});
