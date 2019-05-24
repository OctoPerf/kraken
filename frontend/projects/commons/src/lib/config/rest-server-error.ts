import * as _ from 'lodash';

export class RestServerError {

  public static fromError(error: any) {
    const title = error.status ? `Error ${error.status}` : 'Unexpected error';
    let trace: string;
    let message = '';
    try {
      let serverError = error.error;
      if (_.isString(error.error)) {
        serverError = JSON.parse(error.error);
      }
      message = serverError.message;
      trace = serverError.trace;
    } catch (e) {
      message = error.message;
    }
    return new RestServerError(title, message, trace);
  }

  constructor(public readonly title: string,
              public readonly message: string,
              public readonly trace?: string) {
  }

  public toString(): string {
    return `${this.title}: ${_.truncate(this.message, {length: 300})}`;
  }

}
