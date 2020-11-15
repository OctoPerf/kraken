import {ValidatorFn, Validators} from '@angular/forms';

export class ValidatorTools {

  static gitUrl(): ValidatorFn {
    return Validators.pattern('^git@[^\\:]+[\\:][^\\/:]+\\/.+\\.git$');
  }

}
