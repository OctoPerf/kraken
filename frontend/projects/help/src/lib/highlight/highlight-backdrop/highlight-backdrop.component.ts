import {Component, Inject, InjectionToken, OnInit} from '@angular/core';
import {transition, trigger, useAnimation} from '@angular/animations';
import {fadeInAnimation, fadeOutAnimation} from 'projects/commons/src/lib/animations';

export const HIGHLIGHT_RECT_DATA = new InjectionToken<any>('HighlightRectData');
export const HIGHLIGHT_DURATION_DATA = new InjectionToken<number>('HighlightDurationData');

@Component({
  selector: 'lib-highlight-backdrop',
  templateUrl: './highlight-backdrop.component.html',
  styleUrls: ['./highlight-backdrop.component.scss'],
  animations: [
    trigger('fadeIn', [
        transition(':enter', useAnimation(fadeInAnimation, {params: {duration: '150ms'}})),
        transition(':leave', useAnimation(fadeOutAnimation, {params: {duration: '150ms'}})),
      ]
    )
  ],
})
export class HighlightBackdropComponent implements OnInit {

  private static readonly FADE_DURATION = 150;

  window = window;
  fadeIn = true;

  constructor(@Inject(HIGHLIGHT_RECT_DATA) public rect: any, @Inject(HIGHLIGHT_DURATION_DATA) public duration: number) {
  }

  ngOnInit() {
    setTimeout(() => this.fadeIn = false, this.duration - HighlightBackdropComponent.FADE_DURATION);
  }

}
