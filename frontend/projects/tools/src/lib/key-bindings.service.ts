import {Injectable, OnDestroy} from '@angular/core';
import {fromEvent, Subscription} from 'rxjs';
import * as _ from 'lodash';

/**
 * Key example: 'shift + ctrl + alt + s' the order of the modifiers must be respected.
 * preventDefault if true, $event.preventDefault() is called.
 * stopPropagation if true, $event.stopPropagation() is called.
 * preventTagNames binding is not called if the focused element is in the tag names.
 */
export class KeyBinding {
  constructor(
    public readonly keys: string[],
    public readonly binding: ($event: KeyboardEvent) => boolean,
    public readonly focusedId: string = null,
    public readonly preventDefault = true,
    public readonly stopPropagation = true,
    public readonly preventTagNames = ['UNFOCUSED', 'TEXTAREA', 'INPUT', 'SELECT'],
  ) {
  }
}

@Injectable({
  providedIn: 'root'
})
export class KeyBindingsService implements OnDestroy {

  document = document;
  private bindings: { [key in string]: KeyBinding[] } = {};
  private subscription: Subscription;

  constructor() {
    this.subscription = fromEvent(this.document, 'keydown').subscribe(this._handleEvent.bind(this));
  }

  ngOnDestroy() {
    this.subscription.unsubscribe();
  }

  public add(bindings: KeyBinding[]) {
    _.forEach(bindings, (binding) => {
      _.forEach(binding.keys, (key) => {
        if (!this.bindings[key]) {
          this.bindings[key] = [];
        }
        this.bindings[key].push(binding);
      });
    });
  }

  public remove(bindings: KeyBinding[]) {
    _.forEach(bindings, (binding) => {
      _.forEach(binding.keys, (key) => {
        delete this.bindings[key];
      });
    });
  }

  _handleEvent($event: KeyboardEvent) {
    const key = this._eventToKey($event);
    const bindings = this.bindings[key];
    if (bindings) {
      let tagName = 'UNFOCUSED';
      let focusedId = null;
      if (this.document.hasFocus() && this.document.activeElement) {
        tagName = this.document.activeElement.tagName;
        focusedId = this.document.activeElement.id;
      }
      for (let i = 0; i < bindings.length; i++) {
        const binding = bindings[i];
        if (binding.preventTagNames.indexOf(tagName) === -1 && (!binding.focusedId || binding.focusedId === focusedId)) {
          if (binding.binding($event)) {
            if (binding.preventDefault) {
              $event.preventDefault();
            }
            if (binding.stopPropagation) {
              $event.stopPropagation();
            }
            break;
          }
        }
      }
    }
  }

  _eventToKey($event: KeyboardEvent) {
    let key = '';
    if ($event.shiftKey) {
      key += 'shift + ';
    }
    if ($event.ctrlKey) {
      key += 'ctrl + ';
    }
    if ($event.altKey) {
      key += 'alt + ';
    }
    key += $event.key;
    return key;
  }
}
