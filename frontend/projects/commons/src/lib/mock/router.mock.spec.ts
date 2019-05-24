import {ActivatedRoute, Params} from '@angular/router';
import {cold} from 'jasmine-marbles';

export const routerSpy = () => jasmine.createSpyObj('Router', ['navigate']);

export const activatedRouteSpy = (params: Params): ActivatedRoute => {
  return {params: cold('---x|', {x: params})} as any;
};
