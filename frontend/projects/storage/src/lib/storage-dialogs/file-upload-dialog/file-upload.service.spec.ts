import {TestBed} from '@angular/core/testing';

import {FileUploadService} from './file-upload.service';
import {HttpTestingController} from '@angular/common/http/testing';
import {CoreTestModule} from 'projects/commons/src/lib/core/core.module.spec';
import {EMPTY, of} from 'rxjs';
import {HttpEventType, HttpResponse} from '@angular/common/http';
import {testErrorEvent} from 'projects/commons/src/lib/config/rest-server-error.spec';

export const fileUploadServiceSpy = () => {
  const spy = jasmine.createSpyObj('FileUploadService', [
    'upload',
  ]);
  spy.upload.and.returnValue(of(0, 10, 50, 100));
  return spy;
};


describe('FileUploadService', () => {
  let service: FileUploadService;
  let httpTestingController: HttpTestingController;

  beforeEach(() => {
    TestBed.configureTestingModule({
      imports: [CoreTestModule],
      providers: [FileUploadService]
    });
    service = TestBed.get(FileUploadService);
    httpTestingController = TestBed.get(HttpTestingController);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should upload', () => {
    const spy = spyOn(service, '_uploadFile');
    spy.and.returnValue(EMPTY);
    service.upload(['file'] as any, 'endpoint');
    expect(spy).toHaveBeenCalledWith('file' as any, 'endpoint');
  });

  it('should upload file', () => {
    const formData = jasmine.createSpyObj('formData', ['append']);
    spyOn(service, '_newFormData').and.returnValue(formData);
    const values = [];
    service._uploadFile({name: 'filename'} as any, 'endpoint').subscribe((value) => values.push(value));
    const req = httpTestingController.expectOne('endpoint');
    expect(req.request.method).toBe('POST');
    req.event({type: HttpEventType.UploadProgress, loaded: 25, total: 100});
    req.event({type: HttpEventType.UploadProgress, loaded: 50, total: 100});
    req.event({type: HttpEventType.UploadProgress, loaded: 100, total: 100});
    req.event(new HttpResponse({}));
    req.flush([true]);
    httpTestingController.verify();
    expect(values).toEqual([0, 25, 50, 100]);
  });

  it('should upload fail', () => {
    const formData = jasmine.createSpyObj('formData', ['append']);
    spyOn(service, '_newFormData').and.returnValue(formData);
    const values = [];
    service._uploadFile({name: 'filename'} as any, 'endpoint').subscribe((value) => values.push(value));
    const req = httpTestingController.expectOne('endpoint');
    expect(req.request.method).toBe('POST');
    req.error(testErrorEvent());
    httpTestingController.verify();
    expect(values).toEqual([0]);
  });

  it('should _newFormData', () => {
    expect(service._newFormData()).toBeDefined();
  });
});
