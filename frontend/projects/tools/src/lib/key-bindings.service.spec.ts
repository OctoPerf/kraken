import {TestBed} from '@angular/core/testing';

import {KeyBinding, KeyBindingsService} from './key-bindings.service';
import {eventSpy} from 'projects/commons/src/lib/mock/event.mock.spec';
import Spy = jasmine.Spy;
import {testStorageNodesSorted} from 'projects/storage/src/lib/storage-tree/storage-tree-data-source.service.spec';
import {BehaviorSubject} from 'rxjs';
import {EventEmitter} from '@angular/core';
import {StorageNode} from 'projects/storage/src/lib/entities/storage-node';

export const keyBindingsServiceSpy = () => {
  const spy = jasmine.createSpyObj('KeyBindingsService', [
    'add',
    'remove',
  ]);
  return spy;
};

describe('KeyBindingsService', () => {

  let service: KeyBindingsService;
  let document;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.get(KeyBindingsService);
    document = jasmine.createSpyObj('document', ['hasFocus']);
    document.activeElement = {tagName: 'INPUT'} as any;
    service.document = document;
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
    service.ngOnDestroy();
  });

  it('should generate event key', () => {
    expect(service._eventToKey({
      shiftKey: true,
      ctrlKey: true,
      altKey: true,
      key: 'c',
    } as any)).toBe('shift + ctrl + alt + c');
  });

  it('should generate event key no modifiers', () => {
    expect(service._eventToKey({
      shiftKey: false,
      ctrlKey: false,
      altKey: false,
      key: 'c',
    } as any)).toBe('c');
  });

  it('should handle event', () => {
    const binding = new KeyBinding(['ctrl + a'], jasmine.createSpy('binding'), null, true, true, ['TEXTAREA', 'SELECT']);
    (binding.binding as Spy).and.returnValue(true);
    const bindings = [binding];
    const event = eventSpy();
    spyOn(service, '_eventToKey').and.returnValue('ctrl + a');
    document.hasFocus.and.returnValue(false);

    service.add(bindings);
    service._handleEvent(event);
    service.remove(bindings);

    expect(binding.binding).toHaveBeenCalled();
    expect(event.preventDefault).toHaveBeenCalled();
    expect(event.stopPropagation).toHaveBeenCalled();
  });

  it('should not handle event (no binding found)', () => {
    const binding = new KeyBinding(['ctrl + a'], jasmine.createSpy('binding'));
    (binding.binding as Spy).and.returnValue(true);
    const bindings = [binding];
    const event = eventSpy();
    spyOn(service, '_eventToKey').and.returnValue('other');

    service.add(bindings);
    service._handleEvent(event);
    service.remove(bindings);

    expect(binding.binding).not.toHaveBeenCalled();
    expect(event.preventDefault).not.toHaveBeenCalled();
    expect(event.stopPropagation).not.toHaveBeenCalled();
  });

  it('should handle event binding return false', () => {
    const binding = new KeyBinding(['ctrl + a'], jasmine.createSpy('binding'), null, true, true, []);
    (binding.binding as Spy).and.returnValue(false);
    const bindings = [binding];
    const event = eventSpy();
    spyOn(service, '_eventToKey').and.returnValue('ctrl + a');
    document.hasFocus.and.returnValue(false);

    service.add(bindings);
    service._handleEvent(event);
    service.remove(bindings);

    expect(binding.binding).toHaveBeenCalled();
    expect(event.preventDefault).not.toHaveBeenCalled();
    expect(event.stopPropagation).not.toHaveBeenCalled();
  });

  it('should not handle event prevent tag name', () => {
    const binding = new KeyBinding(['ctrl + a'], jasmine.createSpy('binding'));
    (binding.binding as Spy).and.returnValue(true);
    const bindings = [binding];
    const event = eventSpy();
    spyOn(service, '_eventToKey').and.returnValue('ctrl + a');
    document.hasFocus.and.returnValue(false);

    service.add(bindings);
    service._handleEvent(event);
    service.remove(bindings);

    expect(binding.binding).not.toHaveBeenCalled();
    expect(event.preventDefault).not.toHaveBeenCalled();
    expect(event.stopPropagation).not.toHaveBeenCalled();
  });

  it('should handle event no prevent default & stop prop', () => {
    const binding = new KeyBinding(['ctrl + a'], jasmine.createSpy('binding'), null, false, false, ['', 'TEXTAREA', 'SELECT']);
    (binding.binding as Spy).and.returnValue(true);
    const bindings = [binding];
    const event = eventSpy();
    spyOn(service, '_eventToKey').and.returnValue('ctrl + a');
    document.hasFocus.and.returnValue(true);

    service.add(bindings);
    service._handleEvent(event);
    service.remove(bindings);

    expect(binding.binding).toHaveBeenCalled();
    expect(event.preventDefault).not.toHaveBeenCalled();
    expect(event.stopPropagation).not.toHaveBeenCalled();
  });


});
