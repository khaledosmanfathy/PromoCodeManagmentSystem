import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({ providedIn: 'root' })
export class PromoCodeService {
  private apiUrl = 'http://localhost:8082/api/promo-codes';

  constructor(private http: HttpClient) {}

  getPromoCodes(): Observable<any[]> {
    return this.http.get<any[]>(this.apiUrl);
  }

  createPromoCode(data: any): Observable<any> {
    return this.http.post(this.apiUrl, data);
  }

  applyPromoCode(code: string): Observable<any> {
    return this.http.post(`${this.apiUrl}/apply/${code}`, {});
  }
}