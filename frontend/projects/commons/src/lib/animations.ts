import {animate, animation, style} from '@angular/animations';

export const fadeInScaleUpAnimation = animation([
  style({opacity: 0, transform: 'scale(0.5)'}),
  animate('{{ duration }} ease-in-out', style({opacity: 1, transform: 'scale(1)'})),
]);

export const fadeInAnimation = animation([
  style({opacity: 0}),
  animate('{{ duration }} ease-in-out', style({opacity: 1})),
]);

export const fadeOutAnimation = animation([
  style({opacity: 1}),
  animate('{{ duration }} ease-in-out', style({opacity: 0})),
]);
