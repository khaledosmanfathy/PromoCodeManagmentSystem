import { Injectable } from '@angular/core';
import { HttpRequest, HttpHandler, HttpEvent, HttpInterceptor } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable()
export class AuthInterceptor implements HttpInterceptor {
  intercept(request: HttpRequest<any>, next: HttpHandler): Observable<HttpEvent<any>> {
    const token = localStorage.getItem('access_token'); // Or use a service
    const tenantId = localStorage.getItem('tenant_id') || 'tenant1';

    let headers = request.headers;
    if (token) {
      headers = headers.set('Authorization', `Bearer ${token}`);
    }
    headers = headers.set('X-Tenant-ID', tenantId);

    const cloned = request.clone({ headers });
    return next.handle(cloned);
  }
}