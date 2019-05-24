import {RestServerError} from './rest-server-error';
import {HttpErrorResponse} from '@angular/common/http';

export const testHttpErrorResponse: () => HttpErrorResponse = () => new HttpErrorResponse({
  status: 404,
  error: {type: 'ItemNotFound', message: 'message'}
});
export const testErrorEvent: () => ErrorEvent = () => new ErrorEvent('ItemNotFound', {
  error: testHttpErrorResponse(),
  message: 'errorMessage'
});

describe('RestServerError', () => {

  it('should create', () => {
    expect(new RestServerError('title', 'message')).toBeTruthy();
  });

  it('should toString', () => {
    expect(new RestServerError('title', 'message').toString()).toBe('title: message');
  });

  it('should create from unexpected error', () => {
    const error = RestServerError.fromError({});
    expect(error.title).toBe('Unexpected error');
    expect(error.message).toBeUndefined();
  });

  it('should create from backend string RestServerError', () => {
    const error = RestServerError.fromError({status: 500, error: JSON.stringify({message: 'message', trace: 'trace'})});
    expect(error.title).toBe('Error 500');
    expect(error.message).toBe('message');
    expect(error.trace).toBe('trace');
  });

  it('should create from backend RestServerError', () => {
    const error = RestServerError.fromError({status: 500, error: {message: 'message', trace: 'trace'}});
    expect(error.title).toBe('Error 500');
    expect(error.message).toBe('message');
    expect(error.trace).toBe('trace');
  });

  it('should create from backend other error', () => {
    const error = RestServerError.fromError({status: 404, message: 'message'});
    expect(error.title).toBe('Error 404');
    expect(error.message).toBe('message');
  });

});
