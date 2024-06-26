import { HttpClient } from '@angular/common/http';
import { HttpClientHelper } from './../http-client';
import { Request } from '../request';

describe('HttpClientHelper', () => {

  let httpClientHelper: HttpClientHelper;
  let httpClientMock: jasmine.SpyObj<HttpClient>;

  beforeEach(() => {
    httpClientMock = jasmine.createSpyObj('httpClientMock', ['get', 'post']);
    httpClientHelper = new HttpClientHelper(httpClientMock);
    httpClientHelper.init = '/api/test'
  });

  it('should throw when helper was not initiated', () => {
    const httpClientHelper = new HttpClientHelper(httpClientMock);
    expect(() => httpClientHelper.get(Request.of({ endpoint: 'view' }))).toThrowError('Resource not defined');
  });

  it('should request with endpoint', () => {
    httpClientHelper.get<any, any>(Request.of({ endpoint: 'view' }));
    expect(httpClientMock.get).toHaveBeenCalledWith('/api/test/view');
  });

  it('should only get without endpoint', () => {
    httpClientHelper.get<any, any>(Request.of({}));
    expect(httpClientMock.get).toHaveBeenCalledWith('/api/test');
  });

  it('should post with body and endpoint', () => {
    httpClientHelper.post<any, any>(Request.of({ endpoint: 'view', body: 'teste' }));
    expect(httpClientMock.post).toHaveBeenCalledWith('/api/test/view', 'teste');
  });

});
